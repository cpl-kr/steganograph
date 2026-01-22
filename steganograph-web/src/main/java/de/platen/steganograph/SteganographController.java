package de.platen.steganograph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Controller
public class SteganographController {

    @Autowired
    private Aktionen aktionen;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/generate")
    public String generate() {
        return "generate";
    }

    @PostMapping("/generate")
    public ResponseEntity<Resource> doGenerate(
            @RequestParam("blockgroesse") String blockgroesse,
            @RequestParam("nutzdaten") String nutzdaten,
            @RequestParam("kanale") String kanale,
            @RequestParam("bittiefe") String bittiefe,
            @RequestParam(value = "publicKeys", required = false) MultipartFile[] publicKeys,
            @RequestParam(value = "passwort", required = false) String passwort) throws IOException {
        Path tempDir = Files.createTempDirectory("stega_gen_");
        String fileName = "verteilregel_" + System.currentTimeMillis() + ".dat";
        Path filePath = tempDir.resolve(fileName);

        List<String> keyPaths = new java.util.ArrayList<>();
        if (publicKeys != null) {
            for (MultipartFile key : publicKeys) {
                if (!key.isEmpty()) {
                    Path kp = tempDir.resolve(key.getOriginalFilename());
                    key.transferTo(kp);
                    keyPaths.add(kp.toAbsolutePath().toString());
                }
            }
        }

        aktionen.generiere(blockgroesse, nutzdaten, kanale, bittiefe, filePath.toAbsolutePath().toString(),
                keyPaths.isEmpty() ? null : keyPaths, (passwort == null || passwort.isEmpty()) ? null : passwort);

        Resource resource = new FileSystemResource(filePath.toFile());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping("/hide")
    public String hide() {
        return "hide";
    }

    @PostMapping("/hide")
    public ResponseEntity<Resource> doHide(
            @RequestParam("verteilregel") MultipartFile verteilregel,
            @RequestParam("nutzdaten") MultipartFile nutzdaten,
            @RequestParam("quelle") MultipartFile quelle,
            @RequestParam("verrauschen") String verrauschen,
            @RequestParam(value = "privateKey", required = false) MultipartFile privateKey,
            @RequestParam(value = "passwort", required = false) String passwort) throws IOException {
        Path tempDir = Files.createTempDirectory("stega_hide_");

        Path rulePath = tempDir.resolve(verteilregel.getOriginalFilename());
        verteilregel.transferTo(rulePath);

        Path dataPath = tempDir.resolve(nutzdaten.getOriginalFilename());
        nutzdaten.transferTo(dataPath);

        Path sourcePath = tempDir.resolve(quelle.getOriginalFilename());
        quelle.transferTo(sourcePath);

        String outName = "hidden_" + sourcePath.getFileName().toString();
        Path outPath = tempDir.resolve(outName);

        String privKeyPath = null;
        if (privateKey != null && !privateKey.isEmpty()) {
            Path pkp = tempDir.resolve(privateKey.getOriginalFilename());
            privateKey.transferTo(pkp);
            privKeyPath = pkp.toAbsolutePath().toString();
        }

        Verrauschoption vOpt = Verrauschoption.OHNE;
        if ("nutzdatenbereich".equals(verrauschen))
            vOpt = Verrauschoption.NUTZDATENBEREICH;
        if ("alles".equals(verrauschen))
            vOpt = Verrauschoption.ALLES;

        aktionen.verstecke(
                rulePath.toAbsolutePath().toString(),
                dataPath.toAbsolutePath().toString(),
                sourcePath.toAbsolutePath().toString(),
                outPath.toAbsolutePath().toString(),
                vOpt,
                privKeyPath,
                (passwort == null || passwort.isEmpty()) ? null : passwort);

        Resource resource = new FileSystemResource(outPath.toFile());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outName + "\"")
                .body(resource);
    }

    @GetMapping("/extract")
    public String extract() {
        return "extract";
    }

    @PostMapping("/extract")
    public ResponseEntity<Resource> doExtract(
            @RequestParam("verteilregel") MultipartFile verteilregel,
            @RequestParam("quelle") MultipartFile quelle,
            @RequestParam(value = "privateKey", required = false) MultipartFile privateKey,
            @RequestParam(value = "passwort", required = false) String passwort) throws IOException {
        Path tempDir = Files.createTempDirectory("stega_extract_");

        Path rulePath = tempDir.resolve(verteilregel.getOriginalFilename());
        verteilregel.transferTo(rulePath);

        Path sourcePath = tempDir.resolve(quelle.getOriginalFilename());
        quelle.transferTo(sourcePath);

        String privKeyPath = null;
        if (privateKey != null && !privateKey.isEmpty()) {
            Path pkp = tempDir.resolve(privateKey.getOriginalFilename());
            privateKey.transferTo(pkp);
            privKeyPath = pkp.toAbsolutePath().toString();
        }

        // We use a temporary filename, the extraction logic might overwrite it or use
        // it as a base.
        String tempOutName = "extracted_data_" + System.currentTimeMillis();
        Path outPath = tempDir.resolve(tempOutName);

        aktionen.hole(
                rulePath.toAbsolutePath().toString(),
                sourcePath.toAbsolutePath().toString(),
                outPath.toAbsolutePath().toString(),
                privKeyPath,
                (passwort == null || passwort.isEmpty()) ? null : passwort);

        // After extraction, we should check if the file exists and what its name is.
        // The extraction logic for images (AktionHolenAusBild) might create a file with
        // the original name if outPath is a directory.
        // But here outPath is a file path. Let's see if it works as a file path.

        Resource resource = new FileSystemResource(outPath.toFile());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tempOutName + "\"")
                .body(resource);
    }

    @GetMapping("/keys")
    public String keys() {
        return "keys";
    }

    @PostMapping("/keys")
    public ResponseEntity<Resource> doGenerateKeys(
            @RequestParam("id") String id,
            @RequestParam(value = "passwort", required = false) String passwort) throws IOException {
        Path tempDir = Files.createTempDirectory("stega_keys_");
        String pubName = "public.asc";
        String privName = "private.asc";
        Path pubPath = tempDir.resolve(pubName);
        Path privPath = tempDir.resolve(privName);

        aktionen.erzeugeKeyPaar(id, pubPath.toAbsolutePath().toString(), privPath.toAbsolutePath().toString(),
                (passwort == null || passwort.isEmpty()) ? null : passwort);

        // In a real app we might want to return a zip, but for now let's just return
        // the private key or a simple response.
        // Actually, let's return a zip containing both.
        Path zipPath = tempDir.resolve("keys.zip");
        try (java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(Files.newOutputStream(zipPath))) {
            addToZip(zos, pubPath, pubName);
            addToZip(zos, privPath, privName);
        }

        Resource resource = new FileSystemResource(zipPath.toFile());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"keys.zip\"")
                .body(resource);
    }

    private void addToZip(java.util.zip.ZipOutputStream zos, Path file, String name) throws IOException {
        java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry(name);
        zos.putNextEntry(entry);
        Files.copy(file, zos);
        zos.closeEntry();
    }

    @GetMapping("/files")
    public String files() {
        return "files";
    }

    @PostMapping("/files/random")
    public ResponseEntity<Resource> doGenerateRandom(
            @RequestParam("name") String name,
            @RequestParam("laenge") String laenge) throws IOException {
        Path tempDir = Files.createTempDirectory("stega_rand_");
        Path filePath = tempDir.resolve(name);

        aktionen.erzeuge(filePath.toAbsolutePath().toString(), laenge);

        Resource resource = new FileSystemResource(filePath.toFile());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                .body(resource);
    }

    @PostMapping("/files/hide")
    public ResponseEntity<Resource> doFileHide(
            @RequestParam("quelle") MultipartFile quelle,
            @RequestParam("ziel") MultipartFile ziel,
            @RequestParam("laenge") String laenge,
            @RequestParam("offset") String offset,
            @RequestParam(value = "erzeugung", defaultValue = "false") boolean erzeugung) throws IOException {
        Path tempDir = Files.createTempDirectory("stega_file_hide_");

        Path qPath = tempDir.resolve(quelle.getOriginalFilename());
        quelle.transferTo(qPath);

        Path zPath = tempDir.resolve(ziel.getOriginalFilename());
        ziel.transferTo(zPath);

        aktionen.verstecke(qPath.toAbsolutePath().toString(), zPath.toAbsolutePath().toString(), laenge, offset,
                String.valueOf(erzeugung));

        Resource resource = new FileSystemResource(zPath.toFile());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + ziel.getOriginalFilename() + "\"")
                .body(resource);

    }

    @PostMapping("/files/extract")
    public ResponseEntity<Resource> doFileExtract(
            @RequestParam("quelle") MultipartFile quelle,
            @RequestParam("zieldateiname") String zieldateiname,
            @RequestParam("laenge") String laenge,
            @RequestParam("offset") String offset) throws IOException {
        Path tempDir = Files.createTempDirectory("stega_file_extract_");

        Path qPath = tempDir.resolve(quelle.getOriginalFilename());
        quelle.transferTo(qPath);

        Path zPath = tempDir.resolve(zieldateiname);

        aktionen.hole(qPath.toAbsolutePath().toString(), zPath.toAbsolutePath().toString(), offset, laenge);

        Resource resource = new FileSystemResource(zPath.toFile());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zieldateiname + "\"")
                .body(resource);
    }
}
