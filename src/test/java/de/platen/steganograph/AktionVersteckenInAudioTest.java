package de.platen.steganograph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

import de.platen.extern.wavfile.WavFile;
import de.platen.extern.wavfile.WavFileException;
import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Blockgroesse;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Kanalnummer;
import de.platen.steganograph.datentypen.Positionsnummer;
import de.platen.steganograph.datentypen.Samples;
import de.platen.steganograph.uniformat.UniFormatAudio;
import de.platen.steganograph.utils.ByteUtils;
import de.platen.steganograph.utils.DateiUtils;
import de.platen.steganograph.verteilregelgenerierung.KonfigurationVerteilregeln;
import de.platen.steganograph.verteilregelgenerierung.Verteilregelgenerierung;

public class AktionVersteckenInAudioTest {

    private static final String VERZEICHNIS = "src/test/resources/daten";
    private static final String FILEDELIMITER = FileSystems.getDefault().getSeparator();
    private static final String DATEINAME_VERTEILREGEl = "verteilregeln";
    private static final String DATEINAME_NUTZDATEN = "nutzdatenOriginal";
    private static final String DATEINAME_AUDIO_ORIGINAL = "audiooriginal.wav";
    private static final String DATEINAME_AUDIO_VERSTECK = "audioversteck.wav";

    @BeforeClass
    public static void erzeugeVerzeichnis() {
        File file = new File(VERZEICHNIS);
        if (!file.mkdirs()) {
            System.out.println("Verzeichnis " + VERZEICHNIS + " konnte nicht erzeugt werden.");
        } else {
            System.out.println("Verzeichnis " + VERZEICHNIS + " erzeugt.");
        }
    }

    @AfterClass
    public static void loescheVerzeichnis() throws IOException {
        File file = new File(VERZEICHNIS);
        FileUtils.deleteDirectory(file);
    }

    @Test
    public void testVersteckeNutzdatenInAudioParameterDateinameNull() throws IOException {
        String dateinameVerteilregel = "verteilregel";
        String dateinameNutzdaten = "dateinameNutzdaten";
        String dateinameQuelle = "dateinameQuelle";
        String dateinameZiel = "dateinameZiel";
        Verrauschoption verrauschoption = Verrauschoption.OHNE;
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(null, dateinameNutzdaten, dateinameQuelle,
                    dateinameZiel, verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(dateinameVerteilregel, null, dateinameQuelle,
                    dateinameZiel, verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten, null,
                    dateinameZiel, verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten,
                    dateinameQuelle, null, verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten,
                    dateinameQuelle, dateinameZiel, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testVersteckeNutzdatenInAudioParameterDateinameLeer() throws IOException {
        String dateinameVerteilregel = "verteilregel";
        String dateinameNutzdaten = "dateinameNutzdaten";
        String dateinameQuelle = "dateinameQuelle";
        String dateinameZiel = "dateinameZiel";
        Verrauschoption verrauschoption = Verrauschoption.OHNE;
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio("", dateinameNutzdaten, dateinameQuelle,
                    dateinameZiel, verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(dateinameVerteilregel, "", dateinameQuelle,
                    dateinameZiel, verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten, "",
                    dateinameZiel, verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten,
                    dateinameQuelle, "", verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
    }

    @Test
    public void testVersteckeNutzdatenInAudioFehlerAnzahlKanaele() throws IOException, WavFileException {
        Blockgroesse blockgroesse = new Blockgroesse(200);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(10);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(4);
        Bittiefe bittiefe = new Bittiefe(2);
        final String verzeichnis = "test1";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        DateiUtils.schreibeDatei(pfad + FILEDELIMITER + DATEINAME_VERTEILREGEl, verteilregel);
        byte[] nutzdaten = erzeugeNutzdaten(5);
        DateiUtils.schreibeDatei(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN, nutzdaten);
        WavFile wavFile = erzeugeAudiodatei(10, pfad);
        wavFile.close();
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(pfad + FILEDELIMITER + DATEINAME_VERTEILREGEl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN,
                    pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Die Anzahl der Kanäle von den Versteckregeln ist größer als die Anzahl der Kanäle von Audio.",
                    e.getMessage());
        }
    }

    @Test
    public void testVersteckeNutzdatenInAudioFehlerDatenmenge() throws IOException, WavFileException {
        Blockgroesse blockgroesse = new Blockgroesse(200);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(10);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(2);
        Bittiefe bittiefe = new Bittiefe(2);
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        final String verzeichnis = "test2";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        DateiUtils.schreibeDatei(pfad + FILEDELIMITER + DATEINAME_VERTEILREGEl, verteilregel);
        byte[] nutzdaten = erzeugeNutzdaten(100000);
        DateiUtils.schreibeDatei(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN, nutzdaten);
        WavFile wavFile = erzeugeAudiodatei(1, pfad);
        wavFile.close();
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(pfad + FILEDELIMITER + DATEINAME_VERTEILREGEl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN,
                    pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Es können nicht alle Nutzdaten in Audio untergebracht werden.", e.getMessage());
        }
    }

    @Test
    public void testVersteckeNutzdatenInAudioFuer1BlockAlsTeilblock() throws IOException, WavFileException {
        final String verzeichnis = "test3";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        WavFile wavFileOriginal = erzeugeAudiodatei(1, pfad);
        int numChannels = wavFileOriginal.getNumChannels();
        long numFrames = wavFileOriginal.getNumFrames();
        long sampleRate = wavFileOriginal.getSampleRate();
        int validBits = wavFileOriginal.getValidBits();
        wavFileOriginal.close();
        int anzahlBytes = 5;
        Blockgroesse blockgroesse = new Blockgroesse(200);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(50);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(wavFileOriginal.getNumChannels());
        Bittiefe bittiefe = new Bittiefe(2);
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        List<Eintrag> eintraege = Verteilregelgenerierung.konvertiereEintraege(verteilregel);
        DateiUtils.schreibeDatei(pfad + FILEDELIMITER + DATEINAME_VERTEILREGEl, verteilregel);
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        DateiUtils.schreibeDatei(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN, nutzdaten);
        new AktionVersteckenInAudio().versteckeNutzdatenInAudio(pfad + FILEDELIMITER + DATEINAME_VERTEILREGEl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN,
                pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        WavFile wavFileVersteck = WavFile.openWavFile(new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK));
        assertEquals(numChannels, wavFileVersteck.getNumChannels());
        assertEquals(numFrames, wavFileVersteck.getNumFrames());
        assertEquals(sampleRate, wavFileVersteck.getSampleRate());
        assertEquals(validBits, wavFileVersteck.getValidBits());
        int[][] sampleBuffer = new int[wavFileVersteck.getNumChannels()][(int) wavFileVersteck.getNumFrames()];
        wavFileVersteck.readFrames(sampleBuffer, (int) wavFileVersteck.getNumFrames());
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(blockgroesse.get());
        UniFormatAudio uniFormat = new UniFormatAudio(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
        pruefeStartblock(nutzdaten.length, "nutzdatenOriginal", sampleBuffer, blockgroesse, uniFormat);
        pruefeDatenblock(1, nutzdaten, sampleBuffer, blockgroesse, uniFormat, anzahlNutzdaten);
        wavFileVersteck.close();
    }

    @Test
    public void testVersteckeNutzdatenInAudioFuer1BlockAlsKomplettblock() throws IOException, WavFileException {
        final String verzeichnis = "test4";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        WavFile wavFileOriginal = erzeugeAudiodatei(1, pfad);
        int numChannels = wavFileOriginal.getNumChannels();
        long numFrames = wavFileOriginal.getNumFrames();
        long sampleRate = wavFileOriginal.getSampleRate();
        int validBits = wavFileOriginal.getValidBits();
        wavFileOriginal.close();
        int anzahlBytes = 50;
        Blockgroesse blockgroesse = new Blockgroesse(200);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(50);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(wavFileOriginal.getNumChannels());
        Bittiefe bittiefe = new Bittiefe(2);
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        List<Eintrag> eintraege = Verteilregelgenerierung.konvertiereEintraege(verteilregel);
        DateiUtils.schreibeDatei(pfad + FILEDELIMITER + DATEINAME_VERTEILREGEl, verteilregel);
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        DateiUtils.schreibeDatei(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN, nutzdaten);
        new AktionVersteckenInAudio().versteckeNutzdatenInAudio(pfad + FILEDELIMITER + DATEINAME_VERTEILREGEl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN,
                pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        WavFile wavFileVersteck = WavFile.openWavFile(new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK));
        assertEquals(numChannels, wavFileVersteck.getNumChannels());
        assertEquals(numFrames, wavFileVersteck.getNumFrames());
        assertEquals(sampleRate, wavFileVersteck.getSampleRate());
        assertEquals(validBits, wavFileVersteck.getValidBits());
        int[][] sampleBuffer = new int[wavFileVersteck.getNumChannels()][(int) wavFileVersteck.getNumFrames()];
        wavFileVersteck.readFrames(sampleBuffer, (int) wavFileVersteck.getNumFrames());
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(blockgroesse.get());
        UniFormatAudio uniFormat = new UniFormatAudio(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
        pruefeStartblock(nutzdaten.length, "nutzdatenOriginal", sampleBuffer, blockgroesse, uniFormat);
        pruefeDatenblock(1, nutzdaten, sampleBuffer, blockgroesse, uniFormat, anzahlNutzdaten);
        wavFileVersteck.close();
    }

    @Test
    public void testVersteckeNutzdatenInAudioFuerMehrereBloeckeKompletteBloecke() throws IOException, WavFileException {
        final String verzeichnis = "test5";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        WavFile wavFileOriginal = erzeugeAudiodatei(1, pfad);
        int numChannels = wavFileOriginal.getNumChannels();
        long numFrames = wavFileOriginal.getNumFrames();
        long sampleRate = wavFileOriginal.getSampleRate();
        int validBits = wavFileOriginal.getValidBits();
        wavFileOriginal.close();
        int anzahlBytes = 150;
        Blockgroesse blockgroesse = new Blockgroesse(200);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(50);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(wavFileOriginal.getNumChannels());
        Bittiefe bittiefe = new Bittiefe(2);
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        List<Eintrag> eintraege = Verteilregelgenerierung.konvertiereEintraege(verteilregel);
        DateiUtils.schreibeDatei(pfad + FILEDELIMITER + DATEINAME_VERTEILREGEl, verteilregel);
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        DateiUtils.schreibeDatei(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN, nutzdaten);
        new AktionVersteckenInAudio().versteckeNutzdatenInAudio(pfad + FILEDELIMITER + DATEINAME_VERTEILREGEl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN,
                pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        WavFile wavFileVersteck = WavFile.openWavFile(new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK));
        assertEquals(numChannels, wavFileVersteck.getNumChannels());
        assertEquals(numFrames, wavFileVersteck.getNumFrames());
        assertEquals(sampleRate, wavFileVersteck.getSampleRate());
        assertEquals(validBits, wavFileVersteck.getValidBits());
        int[][] sampleBuffer = new int[wavFileVersteck.getNumChannels()][(int) wavFileVersteck.getNumFrames()];
        wavFileVersteck.readFrames(sampleBuffer, (int) wavFileVersteck.getNumFrames());
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(blockgroesse.get());
        UniFormatAudio uniFormat = new UniFormatAudio(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
        pruefeStartblock(nutzdaten.length, "nutzdatenOriginal", sampleBuffer, blockgroesse, uniFormat);
        pruefeDatenblock(1, nutzdaten, sampleBuffer, blockgroesse, uniFormat, anzahlNutzdaten);
        pruefeDatenblock(2, nutzdaten, sampleBuffer, blockgroesse, uniFormat, anzahlNutzdaten);
        pruefeDatenblock(3, nutzdaten, sampleBuffer, blockgroesse, uniFormat, anzahlNutzdaten);
        wavFileVersteck.close();
    }

    @Test
    public void testVersteckeNutzdatenInAudioFuerMehrereBloeckeLetzterBlockTeilblock()
            throws IOException, WavFileException {
        final String verzeichnis = "test6";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        WavFile wavFileOriginal = erzeugeAudiodatei(1, pfad);
        int numChannels = wavFileOriginal.getNumChannels();
        long numFrames = wavFileOriginal.getNumFrames();
        long sampleRate = wavFileOriginal.getSampleRate();
        int validBits = wavFileOriginal.getValidBits();
        wavFileOriginal.close();
        int anzahlBytes = 125;
        Blockgroesse blockgroesse = new Blockgroesse(200);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(50);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(wavFileOriginal.getNumChannels());
        Bittiefe bittiefe = new Bittiefe(2);
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        List<Eintrag> eintraege = Verteilregelgenerierung.konvertiereEintraege(verteilregel);
        DateiUtils.schreibeDatei(pfad + FILEDELIMITER + DATEINAME_VERTEILREGEl, verteilregel);
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        DateiUtils.schreibeDatei(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN, nutzdaten);
        new AktionVersteckenInAudio().versteckeNutzdatenInAudio(pfad + FILEDELIMITER + DATEINAME_VERTEILREGEl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN,
                pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        WavFile wavFileVersteck = WavFile.openWavFile(new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK));
        assertEquals(numChannels, wavFileVersteck.getNumChannels());
        assertEquals(numFrames, wavFileVersteck.getNumFrames());
        assertEquals(sampleRate, wavFileVersteck.getSampleRate());
        assertEquals(validBits, wavFileVersteck.getValidBits());
        int[][] sampleBuffer = new int[wavFileVersteck.getNumChannels()][(int) wavFileVersteck.getNumFrames()];
        wavFileVersteck.readFrames(sampleBuffer, (int) wavFileVersteck.getNumFrames());
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(blockgroesse.get());
        UniFormatAudio uniFormat = new UniFormatAudio(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
        pruefeStartblock(nutzdaten.length, "nutzdatenOriginal", sampleBuffer, blockgroesse, uniFormat);
        pruefeDatenblock(1, nutzdaten, sampleBuffer, blockgroesse, uniFormat, anzahlNutzdaten);
        pruefeDatenblock(2, nutzdaten, sampleBuffer, blockgroesse, uniFormat, anzahlNutzdaten);
        pruefeDatenblock(3, nutzdaten, sampleBuffer, blockgroesse, uniFormat, anzahlNutzdaten);
        wavFileVersteck.close();
    }

    @Test
    public void testVersteckeNutzdatenInAudioParameterNull() throws IOException {
        WavFile wavFileQuelle = Mockito.mock(WavFile.class);
        WavFile wavFileZiel = Mockito.mock(WavFile.class);
        byte[] verteilregel = new byte[10];
        byte[] nutzdaten = new byte[10];
        Verrauschoption verrauschoption = Verrauschoption.ALLES;
        UniFormatAudio uniFormatAudio = Mockito.mock(UniFormatAudio.class);
        String dateinameNutzdaten = "dateinameNutzdaten";
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(null, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, null, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, null, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, null,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    null, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, null, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testVersteckeNutzdatenInAudioParameterLeer() throws IOException {
        WavFile wavFileQuelle = Mockito.mock(WavFile.class);
        WavFile wavFileZiel = Mockito.mock(WavFile.class);
        byte[] verteilregel = new byte[10];
        byte[] nutzdaten = new byte[10];
        Verrauschoption verrauschoption = Verrauschoption.ALLES;
        UniFormatAudio uniFormatAudio = Mockito.mock(UniFormatAudio.class);
        String dateinameNutzdaten = "dateinameNutzdaten";
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(new byte[0], nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, new byte[0], wavFileQuelle,
                    wavFileZiel, verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, "");
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            Mockito.when(wavFileQuelle.getNumFrames()).thenReturn(0L);
            Mockito.when(wavFileZiel.getNumFrames()).thenReturn(10L);
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            Mockito.when(wavFileQuelle.getNumFrames()).thenReturn(10L);
            Mockito.when(wavFileZiel.getNumFrames()).thenReturn(0L);
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
    }

    @Test
    public void testVersteckeNutzdatenInAudioParameterUngleich() throws IOException, WavFileException {
        final String verzeichnis = "test7";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        WavFile wavFileQuelle = WavFile.newWavFile(new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK), 2, 10, 16, 1000);
        byte[] verteilregel = new byte[10];
        byte[] nutzdaten = new byte[10];
        Verrauschoption verrauschoption = Verrauschoption.ALLES;
        UniFormatAudio uniFormatAudio = Mockito.mock(UniFormatAudio.class);
        String dateinameNutzdaten = "dateinameNutzdaten";
        WavFile wavFileZiel = null;
        try {
            wavFileZiel = WavFile.newWavFile(new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK + "1"), 3, 10, 16, 1000);
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Die Audiokonfiguratuion ist ungleich.", e.getMessage());
        } finally {
            wavFileQuelle.close();
            wavFileZiel.close();
        }
        try {
            wavFileZiel = WavFile.newWavFile(new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK + "2"), 2, 20, 16, 1000);
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Die Audiokonfiguratuion ist ungleich.", e.getMessage());
        } finally {
            wavFileQuelle.close();
            wavFileZiel.close();
        }
        try {
            wavFileZiel = WavFile.newWavFile(new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK + "3"), 2, 10, 8, 1000);
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Die Audiokonfiguratuion ist ungleich.", e.getMessage());
        } finally {
            wavFileQuelle.close();
            wavFileZiel.close();
        }
        try {
            wavFileZiel = WavFile.newWavFile(new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK + "4"), 2, 10, 16, 2000);
            new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Die Audiokonfiguratuion ist ungleich.", e.getMessage());
        } finally {
            wavFileQuelle.close();
            wavFileZiel.close();
        }
    }

    @Test
    public void testVerrauscheRest() throws IOException, WavFileException {
        final String verzeichnis = "test8";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        WavFile wavFileQuelle = erzeugeAudiodatei(1, pfad);
        WavFile wavFileZiel = WavFile.newWavFile(new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK), wavFileQuelle.getNumChannels(),
                wavFileQuelle.getNumFrames(), wavFileQuelle.getValidBits(), wavFileQuelle.getSampleRate());
        Blockgroesse blockgroesse = new Blockgroesse(200);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(50);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(wavFileQuelle.getNumChannels());
        Bittiefe bittiefe = new Bittiefe(2);
        int anzahlBytes = 5;
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        Verrauschoption verrauschoption = Verrauschoption.ALLES;
        UniFormatAudio uniFormatAudio = Mockito.mock(UniFormatAudio.class);
        String dateinameNutzdaten = "dateinameNutzdaten";
        new AktionVersteckenInAudio().versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                verrauschoption, uniFormatAudio, dateinameNutzdaten);
        Mockito.verify(uniFormatAudio, Mockito.times(221)).verrausche();
    }

    private WavFile erzeugeAudiodatei(int anzahlSekunden, String verzeichnis) throws IOException, WavFileException {
        int numChannels = 2;
        int validBits = 16;
        int sampleRate = 44100;
        int numFrames = sampleRate * anzahlSekunden;
        String datei = verzeichnis + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL;
        WavFile wavFile = WavFile.newWavFile(new File(datei), numChannels, numFrames, validBits,
                sampleRate);
        int[][] sampleBuffer = new int[numChannels][sampleRate];
        for (int sekunde = 1; sekunde <= 10; sekunde++) {
            for (int sample = 0; sample < sampleRate; sample++) {
                for (int kanal = 0; kanal < numChannels; kanal++) {
                    sampleBuffer[kanal][sample] = sample;
                }

            }
            wavFile.writeFrames(sampleBuffer, sampleRate);
        }
        wavFile.close();
        return WavFile.openWavFile(new File(datei));
    }

    private byte[] erzeugeNutzdaten(int anzahl) {
        boolean ende = false;
        int position = 0;
        byte[] daten = new byte[anzahl];
        while (!ende) {
            for (int index = 0; index < 255; index++) {
                daten[position] = (byte) index;
                position++;
                if (position == anzahl) {
                    return daten;
                }
            }
        }
        return daten;
    }

    private byte[] generiere(Blockgroesse blockgroesse, AnzahlNutzdaten anzahlNutzdaten, AnzahlKanaele anzahlKanaele,
            Bittiefe bittiefe) {
        KonfigurationVerteilregeln konfigurationGenerierung = new KonfigurationVerteilregeln(blockgroesse,
                anzahlNutzdaten, bittiefe, anzahlKanaele);
        Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        int anzahlPositionen = (anzahlNutzdaten.get() * (8 / bittiefe.get())) / anzahlKanaele.get();
        List<Eintrag> eintraege = erzeugeEintraege(anzahlPositionen, anzahlKanaele.get());
        return verteilregelgenerierung.generiereMitVorgabe(eintraege);
    }

    private List<Eintrag> erzeugeEintraege(int anzahlPositionen, int anzahlKanaele) {
        List<Eintrag> eintraege = new ArrayList<>();
        for (int positionsnummer = 1; positionsnummer <= anzahlPositionen; positionsnummer++) {
            for (int kanalnummer = 1; kanalnummer <= anzahlKanaele; kanalnummer++) {
                eintraege.add(new Eintrag(new Positionsnummer(positionsnummer), new Kanalnummer(kanalnummer)));
            }
        }
        return eintraege;
    }

    private void pruefeStartblock(int anzahlNutzdaten, String dateiname, int[][] sampleBuffer,
            Blockgroesse blockgroesse, UniFormatAudio uniFormat) {
        int[][] block = new int[sampleBuffer.length][blockgroesse.get()];
        for (int indexKanal = 0; indexKanal < sampleBuffer.length; indexKanal++) {
            for (int indexPosition = 0; indexPosition < blockgroesse.get(); indexPosition++) {
                block[indexKanal][indexPosition] = sampleBuffer[indexKanal][indexPosition];
            }
        }
        Samples samples = new Samples(block);
        uniFormat.uebertrageBereichZuUniFormat(samples);
        byte[] nutzdaten = uniFormat.holeNutzdaten(blockgroesse.get());
        byte[] zahl = new byte[4];
        System.arraycopy(nutzdaten, 0, zahl, 0, 4);
        assertEquals(anzahlNutzdaten, ByteUtils.bytesToInt(zahl));
        System.arraycopy(nutzdaten, 4, zahl, 0, 4);
        assertEquals(dateiname.getBytes().length, ByteUtils.bytesToInt(zahl));
        byte[] name = new byte[dateiname.getBytes().length];
        System.arraycopy(nutzdaten, 8, name, 0, dateiname.getBytes().length);
        assertEquals(dateiname, new String(name));
    }

    private void pruefeDatenblock(int datenblocknummer, byte[] nutzdaten, int[][] sampleBuffer,
            Blockgroesse blockgroesse, UniFormatAudio uniFormat, AnzahlNutzdaten anzahlNutzdaten) {
        int[][] block = new int[sampleBuffer.length][blockgroesse.get()];
        for (int indexKanal = 0; indexKanal < sampleBuffer.length; indexKanal++) {
            for (int indexPosition = 0, indexOffset = datenblocknummer
                    * blockgroesse.get(); indexPosition < blockgroesse.get(); indexPosition++, indexOffset++) {
                block[indexKanal][indexPosition] = sampleBuffer[indexKanal][indexOffset];
            }
        }
        Samples samples = new Samples(block);
        uniFormat.uebertrageBereichZuUniFormat(samples);
        int offsetNutzdaten = (datenblocknummer - 1) * anzahlNutzdaten.get();
        int laenge = anzahlNutzdaten.get();
        if (offsetNutzdaten + laenge > nutzdaten.length) {
            laenge = nutzdaten.length - offsetNutzdaten;
        }
        byte[] nutzdatenBlock = uniFormat.holeNutzdaten(laenge);
        byte[] nutzdatenErwartet = new byte[laenge];
        System.arraycopy(nutzdaten, offsetNutzdaten, nutzdatenErwartet, 0, laenge);
        assertArrayEquals(nutzdatenErwartet, nutzdatenBlock);
    }
}
