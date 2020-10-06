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
import de.platen.steganograph.utils.DateiUtils;
import de.platen.steganograph.verteilregelgenerierung.Verteilregelgenerierung;

public class AktionVersteckenInAudio {

    private static final String FEHLER_PARAMETER_NULL = "Einer der Parameter ist null.";
    private static final String FEHLER_PARAMETER_LEER = "Einer der Parameter ist leer.";
    private static final String FEHLER_PARAMETER_AUDIO = "Die Audiokonfiguratuion ist ungleich.";
    private static final String FEHLER_DATENMENGE = "Es können nicht alle Nutzdaten in Audio untergebracht werden.";
    private static final String FEHLER_ANZAHL_KANAELE = "Die Anzahl der Kanäle von den Versteckregeln ist größer als die Anzahl der Kanäle von Audio.";

    public void versteckeNutzdatenInAudio(String dateinameVerteilregel, String dateinameNutzdaten,
            String dateinameQuelle, String dateinameZiel, Verrauschoption verrauschoption) throws IOException {
        pruefeParameter(dateinameVerteilregel, dateinameNutzdaten, dateinameQuelle, dateinameZiel, verrauschoption);
        byte[] verteilregel = DateiUtils.leseDatei(dateinameVerteilregel);
        List<Eintrag> eintraege = Verteilregelgenerierung.konvertiereEintraege(verteilregel);
        AnzahlPositionen anzahlPositionen = Verteilregelgenerierung.ermittleAnzahlPositionen(verteilregel);
        AnzahlNutzdaten anzahlNutzdaten = Verteilregelgenerierung.ermittleAnzahlNutzdaten(verteilregel);
        AnzahlKanaele anzahlKanaele = Verteilregelgenerierung.ermittleAnzahlKanaele(verteilregel);
        Bittiefe bittiefe = Verteilregelgenerierung.ermittleBittiefe(verteilregel);
        byte[] nutzdaten = DateiUtils.leseDatei(dateinameNutzdaten);
        WavFile wavFileQuelle = null;
        WavFile wavFileZiel = null;
        try {
            wavFileQuelle = WavFile.openWavFile(new File(dateinameQuelle));
            if (anzahlKanaele.get() > wavFileQuelle.getNumChannels()) {
                throw new RuntimeException(FEHLER_ANZAHL_KANAELE);
            }
            long anzahlBloeckeGesamt = wavFileQuelle.getNumFrames();
            if (((anzahlBloeckeGesamt - 1) * anzahlNutzdaten.get()) < nutzdaten.length) {
                throw new RuntimeException(FEHLER_DATENMENGE);
            }
            wavFileZiel = WavFile.newWavFile(new File(dateinameZiel), wavFileQuelle.getNumChannels(),
                    wavFileQuelle.getNumFrames(), wavFileQuelle.getValidBits(), wavFileQuelle.getSampleRate());
            UniFormatAudio uniFormatAudio = new UniFormatAudio(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
            versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel, verrauschoption,
                    uniFormatAudio, dateinameNutzdaten);
        } catch (WavFileException e) {
            throw new RuntimeException(e);
        } finally {
            if (wavFileQuelle != null) {
                wavFileQuelle.close();
            }
            if (wavFileZiel != null) {
                wavFileZiel.close();
            }
        }
    }

    public void versteckeNutzdatenInAudio(byte[] verteilregel, byte[] nutzdaten, WavFile wavFileQuelle,
            WavFile wavFileZiel, Verrauschoption verrauschoption, UniFormatAudio uniFormatAudio,
            String dateinameNutzdaten) throws IOException {
        pruefeParameter(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel, verrauschoption, uniFormatAudio,
                dateinameNutzdaten);
        AnzahlPositionen anzahlPositionen = Verteilregelgenerierung.ermittleAnzahlPositionen(verteilregel);
        AnzahlNutzdaten anzahlNutzdaten = Verteilregelgenerierung.ermittleAnzahlNutzdaten(verteilregel);
        AnzahlKanaele anzahlKanaele = Verteilregelgenerierung.ermittleAnzahlKanaele(verteilregel);
        int offsetNutzdaten = 0;
        int[][] sampleBuffer = new int[anzahlKanaele.get()][anzahlPositionen.get()];
        Samples samples = new Samples(sampleBuffer);
        int anzahlFramesGelesen = 0;
        boolean istStartblock = true;
        boolean sindNutzdatenBehandelt = false;
        int laenge = anzahlNutzdaten.get();
        byte[] nutzdatenblock = new byte[laenge];
        boolean istletzterBlock = false;
        try {
            do {
                anzahlFramesGelesen = wavFileQuelle.readFrames(sampleBuffer, 0, anzahlPositionen.get());
                if (anzahlFramesGelesen < anzahlPositionen.get()) {
                    int[][] sampleBufferNeu = new int[anzahlKanaele.get()][anzahlFramesGelesen];
                    for (int indexKanal = 0; indexKanal < anzahlKanaele.get(); indexKanal++) {
                        System.arraycopy(sampleBuffer[indexKanal], 0, sampleBufferNeu[indexKanal], 0,
                                anzahlFramesGelesen);
                    }
                    samples = new Samples(sampleBufferNeu);
                }
                uniFormatAudio.uebertrageBereichZuUniFormat(samples);
                if (istStartblock) {
                    if (verrauschoption != Verrauschoption.OHNE) {
                        uniFormatAudio.verrausche();
                    }
                    byte[] datenStartblock = AktionUtil.bereiteStartblockdaten(nutzdaten.length, anzahlNutzdaten,
                            dateinameNutzdaten);
                    uniFormatAudio.versteckeNutzdaten(datenStartblock);
                    istStartblock = false;
                } else {
                    if (!sindNutzdatenBehandelt) {
                        if (verrauschoption != Verrauschoption.OHNE) {
                            uniFormatAudio.verrausche();
                        }
                        if (laenge > (nutzdaten.length - offsetNutzdaten)) {
                            laenge = nutzdaten.length - offsetNutzdaten;
                            istletzterBlock = true;
                        }
                        System.arraycopy(nutzdaten, offsetNutzdaten, nutzdatenblock, 0, laenge);
                        uniFormatAudio.versteckeNutzdaten(nutzdatenblock);
                        if (!istletzterBlock) {
                            if ((offsetNutzdaten + anzahlNutzdaten.get()) < nutzdaten.length) {
                                offsetNutzdaten += anzahlNutzdaten.get();
                            } else {
                                laenge = nutzdaten.length - offsetNutzdaten;
                                nutzdatenblock = new byte[laenge];
                                offsetNutzdaten += laenge;
                                istletzterBlock = true;
                            }
                        } else {
                            sindNutzdatenBehandelt = true;
                        }
                    } else {
                        if (verrauschoption == Verrauschoption.ALLES) {
                            uniFormatAudio.verrausche();
                        }
                    }
                }
                uniFormatAudio.uebertrageBereichVonUniFormat(samples);
                if (anzahlFramesGelesen > 0) {
                    wavFileZiel.writeFrames(sampleBuffer, anzahlFramesGelesen);
                }
            } while (anzahlFramesGelesen == anzahlPositionen.get());
        } catch (WavFileException e) {
            throw new RuntimeException(e);
        } finally {
            wavFileQuelle.close();
            wavFileZiel.close();
        }
    }

    private static void pruefeParameter(String dateinameVerteilregel, String dateinameNutzdaten, String dateinameQuelle,
            String dateinameZiel, Verrauschoption verrauschoption) {
        if ((dateinameVerteilregel == null) || (dateinameNutzdaten == null) || (dateinameQuelle == null)
                || (dateinameZiel == null) || (verrauschoption == null)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        if (dateinameVerteilregel.isEmpty() || dateinameNutzdaten.isEmpty() || dateinameQuelle.isEmpty()
                || dateinameZiel.isEmpty()) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_LEER);
        }
    }

    private static void pruefeParameter(byte[] verteilregel, byte[] nutzdaten, WavFile wavFileQuelle,
            WavFile wavFileZiel, Verrauschoption verrauschoption, UniFormatAudio uniFormatAudio,
            String dateinameNutzdaten) {
        if ((verteilregel == null) || (nutzdaten == null) || (wavFileQuelle == null) || (wavFileZiel == null)
                || (verrauschoption == null) || (uniFormatAudio == null) || (dateinameNutzdaten == null)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        if ((wavFileQuelle.getNumFrames() == 0) || (wavFileZiel.getNumFrames() == 0)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_LEER);
        }
        if ((verteilregel.length == 0) || (nutzdaten.length == 0) || (dateinameNutzdaten.isEmpty())) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_LEER);
        }
        if (wavFileQuelle.getNumChannels() != wavFileZiel.getNumChannels()) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_AUDIO);
        }
        if (wavFileQuelle.getNumFrames() != wavFileZiel.getNumFrames()) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_AUDIO);
        }
        if (wavFileQuelle.getSampleRate() != wavFileZiel.getSampleRate()) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_AUDIO);
        }
        if (wavFileQuelle.getValidBits() != wavFileZiel.getValidBits()) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_AUDIO);
        }
    }
}
