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
import de.platen.steganograph.uniformat.UniFormatAudio;
import de.platen.steganograph.utils.DateiUtils;
import de.platen.steganograph.verteilregelgenerierung.Verteilregelgenerierung;

public class AktionHolenAusAudio {

    private static final String FEHLER_PARAMETER_NULL = "Einer der Parameter ist null.";
    private static final String FEHLER_PARAMETER_LEER = "Einer der Parameter ist leer.";

    public void holeNutzdatenAusAudio(String dateinameVerteilregel, String dateinameQuelle, String dateinameNutzdaten)
            throws IOException {
        pruefeParameter(dateinameVerteilregel, dateinameQuelle, dateinameNutzdaten);
        byte[] verteilregel = DateiUtils.leseDatei(dateinameVerteilregel);
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
            String dateinameNutzdaten) {
        pruefeParameter(uniFormatAudio, wavFile, anzahlNutzdaten, dateinameNutzdaten);
        // TODO
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
}
