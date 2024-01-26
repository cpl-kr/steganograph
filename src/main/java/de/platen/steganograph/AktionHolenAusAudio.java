package de.platen.steganograph;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.platen.extern.wavfile.WavFile;
import de.platen.extern.wavfile.WavFileException;
import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Samples;
import de.platen.steganograph.uniformat.UniFormatAudio;
import de.platen.steganograph.utils.ByteUtils;
import de.platen.steganograph.utils.DateiUtils;
import de.platen.steganograph.verteilregelgenerierung.Verteilregelgenerierung;

public class AktionHolenAusAudio {

    private static final String FEHLER_PARAMETER_NULL = "Einer der Parameter ist null.";
    private static final String FEHLER_PARAMETER_LEER = "Einer der Parameter ist leer.";

    public void holeNutzdatenAusAudio(String dateinameVerteilregel, String dateinameQuelle, String dateinameNutzdaten)
            throws IOException {
        holeNutzdatenAusAudio(dateinameVerteilregel, dateinameQuelle, dateinameNutzdaten, null, null);
    }

    public void holeNutzdatenAusAudio(String dateinameVerteilregel, String dateinameQuelle, String dateinameNutzdaten, String dateiPrivateKey, String passwort)
            throws IOException {
        pruefeParameter(dateinameVerteilregel, dateinameQuelle, dateinameNutzdaten);
        byte[] verteilregel;
        if ((dateiPrivateKey != null) && !dateiPrivateKey.isEmpty()) {
            verteilregel = DateiUtils.leseDatei(dateinameVerteilregel, dateiPrivateKey, passwort);
        } else {
            verteilregel = DateiUtils.leseDatei(dateinameVerteilregel);
        }
        List<Eintrag> eintraege = Verteilregelgenerierung.konvertiereEintraege(verteilregel);
        AnzahlPositionen anzahlPositionen = Verteilregelgenerierung.ermittleAnzahlPositionen(verteilregel);
        AnzahlNutzdaten anzahlNutzdaten = Verteilregelgenerierung.ermittleAnzahlNutzdaten(verteilregel);
        AnzahlKanaele anzahlKanaele = Verteilregelgenerierung.ermittleAnzahlKanaele(verteilregel);
        Bittiefe bittiefe = Verteilregelgenerierung.ermittleBittiefe(verteilregel);
        UniFormatAudio uniFormatAudio = new UniFormatAudio(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
        WavFile wavFile = null;
        try {
            wavFile = WavFile.openWavFile(new File(dateinameQuelle));
            holeNutzdatenAusAudio(uniFormatAudio, wavFile, anzahlNutzdaten, dateinameNutzdaten);
        } catch (WavFileException e) {
            throw new RuntimeException(e);
        } finally {
            if (wavFile != null) {
                wavFile.close();
            }
        }
    }

    public void holeNutzdatenAusAudio(UniFormatAudio uniFormatAudio, WavFile wavFile, AnzahlNutzdaten anzahlNutzdaten,
            String dateinameNutzdaten) throws IOException {
        pruefeParameter(uniFormatAudio, wavFile, anzahlNutzdaten, dateinameNutzdaten);
        byte[] startblock = new byte[0];
        try {
            startblock = leseBlock(uniFormatAudio, wavFile, anzahlNutzdaten.get());
        } catch (WavFileException e) {
            wavFile.close();
            throw new RuntimeException(e);
        }
        byte[] zahl = new byte[4];
        System.arraycopy(startblock, 0, zahl, 0, 4);
        int anzahlBytes = ByteUtils.bytesToInt(zahl);
        System.arraycopy(startblock, 4, zahl, 0, 4);
        int laengeDateiname = ByteUtils.bytesToInt(zahl);
        byte[] datei = new byte[laengeDateiname];
        System.arraycopy(startblock, 8, datei, 0, laengeDateiname);
        String dateinameAusBlock = new String(datei);
        String dateinameZiel = dateinameNutzdaten;
        File file = new File(dateinameNutzdaten);
        if (file.isDirectory()) {
            String trenner = "/";
            if (dateinameNutzdaten.endsWith("/")) {
                trenner = "";
            }
            if (dateinameNutzdaten.endsWith("\\")) {
                trenner = "";
            }
            dateinameZiel = dateinameNutzdaten + trenner + dateinameAusBlock;
        }
        byte[] nutzdaten = new byte[anzahlBytes];
        byte[] blockdaten = null;
        int offset = 0;
        int maximalanzahl = anzahlNutzdaten.get();
        int anzahlGelesen = 0;
        try {
            do {
                if (anzahlBytes < anzahlNutzdaten.get()) {
                    maximalanzahl = anzahlBytes;
                } else {
                    if ((anzahlBytes - anzahlGelesen) < maximalanzahl) {
                        maximalanzahl = anzahlBytes - anzahlGelesen;
                    }
                }
                blockdaten = leseBlock(uniFormatAudio, wavFile, maximalanzahl);
                System.arraycopy(blockdaten, 0, nutzdaten, offset, blockdaten.length);
                anzahlGelesen += blockdaten.length;
                if (offset + blockdaten.length < anzahlBytes) {
                    offset += blockdaten.length;
                } else {
                    offset = anzahlBytes - offset;
                }
            } while (anzahlGelesen < anzahlBytes);
        } catch (WavFileException e) {
            throw new RuntimeException(e);
        } finally {
            wavFile.close();
        }
        DateiUtils.schreibeDatei(dateinameZiel, nutzdaten);
    }

    private static void pruefeParameter(String dateinameVerteilregel, String dateinameQuelle,
            String dateinameNutzdaten) {
        if ((dateinameVerteilregel == null) || (dateinameQuelle == null) || (dateinameNutzdaten == null)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        if (dateinameVerteilregel.isEmpty() || dateinameQuelle.isEmpty() || dateinameNutzdaten.isEmpty()) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_LEER);
        }
    }

    private static void pruefeParameter(UniFormatAudio uniFormatAudio, WavFile wavFile, AnzahlNutzdaten anzahlNutzdaten,
            String dateinameNutzdaten) {
        if ((uniFormatAudio == null) || (wavFile == null) || (anzahlNutzdaten == null)
                || (dateinameNutzdaten == null)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        if (dateinameNutzdaten.isEmpty() || (wavFile.getNumFrames() == 0)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_LEER);
        }
    }

    private static byte[] leseBlock(UniFormatAudio uniFormatAudio, WavFile wavFile, int maximalanzahl)
            throws IOException, WavFileException {
        int[][] sampleWerte = new int[uniFormatAudio.getAnzahlKanaele()][uniFormatAudio.getAnzahlPositionen()];
        int anzahlGelesen = wavFile.readFrames(sampleWerte, uniFormatAudio.getAnzahlPositionen());
        if (anzahlGelesen < uniFormatAudio.getAnzahlPositionen()) {
            int[][] sampleWerteNeu = new int[uniFormatAudio.getAnzahlKanaele()][anzahlGelesen];
            for (int indexKanal = 0; indexKanal < sampleWerte.length; indexKanal++) {
                for (int indexPosition = 0; indexPosition < anzahlGelesen; indexPosition++) {
                    sampleWerteNeu[indexKanal][indexPosition] = sampleWerte[indexKanal][indexPosition];
                }
            }
            sampleWerte = sampleWerteNeu;
        }
        Samples samples = new Samples(sampleWerte);
        uniFormatAudio.uebertrageBereichZuUniFormat(samples);
        return uniFormatAudio.holeNutzdaten(maximalanzahl);
    }
}
