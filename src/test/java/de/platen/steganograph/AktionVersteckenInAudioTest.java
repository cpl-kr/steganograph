package de.platen.steganograph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

    private static final String DATEINAME_VERTEILREGEl = "src/test/resources/verteilregeln";
    private static final String DATEINAME_NUTZDATEN = "src/test/resources/nutzdatenOriginal";
    private static final String DATEINAME_AUDIO_ORIGINAL = "src/test/resources/audiooriginal.wav";
    private static final String DATEINAME_AUDIO_VERSTECK = "src/test/resources/audioversteck.wav";
    private static final String DATEINAME_NUTZDATEN_NEU = "src/test/resources/nutzdatenneu";

    @Before
    public void before() {
        loescheDateien();
    }

    @After
    public void after() {
        loescheDateien();
    }

    @Test
    public void testVersteckeNutzdatenInAudioParameterDateinameNull() throws IOException {
        String dateinameVerteilregel = "verteilregel";
        String dateinameNutzdaten = "dateinameNutzdaten";
        String dateinameQuelle = "dateinameQuelle";
        String dateinameZiel = "dateinameZiel";
        Verrauschoption verrauschoption = Verrauschoption.OHNE;
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(null, dateinameNutzdaten, dateinameQuelle, dateinameZiel,
                    verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(dateinameVerteilregel, null, dateinameQuelle,
                    dateinameZiel, verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten, null,
                    dateinameZiel, verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten,
                    dateinameQuelle, null, verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten,
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
            AktionVersteckenInAudio.versteckeNutzdatenInAudio("", dateinameNutzdaten, dateinameQuelle, dateinameZiel,
                    verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(dateinameVerteilregel, "", dateinameQuelle, dateinameZiel,
                    verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten, "",
                    dateinameZiel, verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten,
                    dateinameQuelle, "", verrauschoption);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
    }

    @Test
    public void testVersteckeNutzdatenInAudioFehlerAnzahlKanaele() throws IOException {
        Blockgroesse blockgroesse = new Blockgroesse(200);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(10);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(4);
        Bittiefe bittiefe = new Bittiefe(2);
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        DateiUtils.schreibeDatei(DATEINAME_VERTEILREGEl, verteilregel);
        byte[] nutzdaten = erzeugeNutzdaten(5);
        DateiUtils.schreibeDatei(DATEINAME_NUTZDATEN, nutzdaten);
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(DATEINAME_VERTEILREGEl, DATEINAME_NUTZDATEN,
                    DATEINAME_AUDIO_ORIGINAL, DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Die Anzahl der Kanäle von den Versteckregeln ist größer als die Anzahl der Kanäle von Audio.",
                    e.getMessage());
        }
    }

    @Test
    public void testVersteckeNutzdatenInAudioFehlerDatenmenge() throws IOException {
        Blockgroesse blockgroesse = new Blockgroesse(200);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(10);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(2);
        Bittiefe bittiefe = new Bittiefe(2);
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        DateiUtils.schreibeDatei(DATEINAME_VERTEILREGEl, verteilregel);
        byte[] nutzdaten = erzeugeNutzdaten(50000);
        DateiUtils.schreibeDatei(DATEINAME_NUTZDATEN, nutzdaten);
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(DATEINAME_VERTEILREGEl, DATEINAME_NUTZDATEN,
                    DATEINAME_AUDIO_ORIGINAL, DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Es können nicht alle Nutzdaten in Audio untergebracht werden.", e.getMessage());
        }
    }

    @Test
    public void testVersteckeNutzdatenInAudioFuer1BlockAlsTeilblock() throws IOException, WavFileException {
        WavFile wavFileOriginal = WavFile.openWavFile(new File(DATEINAME_AUDIO_ORIGINAL));
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
        DateiUtils.schreibeDatei(DATEINAME_VERTEILREGEl, verteilregel);
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        DateiUtils.schreibeDatei(DATEINAME_NUTZDATEN, nutzdaten);
        AktionVersteckenInAudio.versteckeNutzdatenInAudio(DATEINAME_VERTEILREGEl, DATEINAME_NUTZDATEN,
                DATEINAME_AUDIO_ORIGINAL, DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        WavFile wavFileVersteck = WavFile.openWavFile(new File(DATEINAME_AUDIO_VERSTECK));
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
        WavFile wavFileOriginal = WavFile.openWavFile(new File(DATEINAME_AUDIO_ORIGINAL));
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
        DateiUtils.schreibeDatei(DATEINAME_VERTEILREGEl, verteilregel);
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        DateiUtils.schreibeDatei(DATEINAME_NUTZDATEN, nutzdaten);
        AktionVersteckenInAudio.versteckeNutzdatenInAudio(DATEINAME_VERTEILREGEl, DATEINAME_NUTZDATEN,
                DATEINAME_AUDIO_ORIGINAL, DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        WavFile wavFileVersteck = WavFile.openWavFile(new File(DATEINAME_AUDIO_VERSTECK));
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
        WavFile wavFileOriginal = WavFile.openWavFile(new File(DATEINAME_AUDIO_ORIGINAL));
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
        DateiUtils.schreibeDatei(DATEINAME_VERTEILREGEl, verteilregel);
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        DateiUtils.schreibeDatei(DATEINAME_NUTZDATEN, nutzdaten);
        AktionVersteckenInAudio.versteckeNutzdatenInAudio(DATEINAME_VERTEILREGEl, DATEINAME_NUTZDATEN,
                DATEINAME_AUDIO_ORIGINAL, DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        WavFile wavFileVersteck = WavFile.openWavFile(new File(DATEINAME_AUDIO_VERSTECK));
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
        WavFile wavFileOriginal = WavFile.openWavFile(new File(DATEINAME_AUDIO_ORIGINAL));
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
        DateiUtils.schreibeDatei(DATEINAME_VERTEILREGEl, verteilregel);
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        DateiUtils.schreibeDatei(DATEINAME_NUTZDATEN, nutzdaten);
        AktionVersteckenInAudio.versteckeNutzdatenInAudio(DATEINAME_VERTEILREGEl, DATEINAME_NUTZDATEN,
                DATEINAME_AUDIO_ORIGINAL, DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        WavFile wavFileVersteck = WavFile.openWavFile(new File(DATEINAME_AUDIO_VERSTECK));
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
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(null, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, null, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, nutzdaten, null, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, null,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel, null,
                    uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, null, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
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
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(new byte[0], nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, new byte[0], wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, "");
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
    }

    @Test
    public void testVersteckeNutzdatenInAudioParameterUngleich() throws IOException, WavFileException {
        WavFile wavFileQuelle = WavFile.newWavFile(new File(DATEINAME_AUDIO_VERSTECK), 2, 10, 16, 1000);
        byte[] verteilregel = new byte[10];
        byte[] nutzdaten = new byte[10];
        Verrauschoption verrauschoption = Verrauschoption.ALLES;
        UniFormatAudio uniFormatAudio = Mockito.mock(UniFormatAudio.class);
        String dateinameNutzdaten = "dateinameNutzdaten";
        WavFile wavFileZiel = null;
        try {
            wavFileZiel = WavFile.newWavFile(new File(DATEINAME_AUDIO_VERSTECK + "1"), 3, 10, 16, 1000);
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Die Audiokonfiguratuion ist ungleich.", e.getMessage());
        } finally {
            wavFileQuelle.close();
            wavFileZiel.close();
        }
        try {
            wavFileZiel = WavFile.newWavFile(new File(DATEINAME_AUDIO_VERSTECK + "2"), 2, 20, 16, 1000);
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Die Audiokonfiguratuion ist ungleich.", e.getMessage());
        } finally {
            wavFileQuelle.close();
            wavFileZiel.close();
        }
        try {
            wavFileZiel = WavFile.newWavFile(new File(DATEINAME_AUDIO_VERSTECK + "3"), 2, 10, 8, 1000);
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                    verrauschoption, uniFormatAudio, dateinameNutzdaten);
        } catch (IllegalArgumentException e) {
            assertEquals("Die Audiokonfiguratuion ist ungleich.", e.getMessage());
        } finally {
            wavFileQuelle.close();
            wavFileZiel.close();
        }
        try {
            wavFileZiel = WavFile.newWavFile(new File(DATEINAME_AUDIO_VERSTECK + "4"), 2, 10, 16, 2000);
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
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
        WavFile wavFileQuelle = WavFile.openWavFile(new File(DATEINAME_AUDIO_ORIGINAL));
        WavFile wavFileZiel = WavFile.newWavFile(new File(DATEINAME_AUDIO_VERSTECK), wavFileQuelle.getNumChannels(),
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
        AktionVersteckenInAudio.versteckeNutzdatenInAudio(verteilregel, nutzdaten, wavFileQuelle, wavFileZiel,
                verrauschoption, uniFormatAudio, dateinameNutzdaten);
        Mockito.verify(uniFormatAudio, Mockito.times(26)).verrausche();
    }

    private void loescheDateien() {
        File fileVerteilregel = new File(DATEINAME_VERTEILREGEl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN);
        // File fileOriginal = new File(DATEINAME_AUDIO_ORIGINAL);
        File fileVersteck = new File(DATEINAME_AUDIO_VERSTECK);
        File fileVersteck1 = new File(DATEINAME_AUDIO_VERSTECK + "1");
        File fileVersteck2 = new File(DATEINAME_AUDIO_VERSTECK + "2");
        File fileVersteck3 = new File(DATEINAME_AUDIO_VERSTECK + "3");
        File fileVersteck4 = new File(DATEINAME_AUDIO_VERSTECK + "4");
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        if (fileVerteilregel.exists()) {
            fileVerteilregel.delete();
        }
        if (fileNutzdatenOriginal.exists()) {
            fileNutzdatenOriginal.delete();
        }
        // if (fileOriginal.exists()) {
        // fileOriginal.delete();
        // }
        if (fileVersteck.exists()) {
            fileVersteck.delete();
        }
        if (fileVersteck1.exists()) {
            fileVersteck1.delete();
        }
        if (fileVersteck2.exists()) {
            fileVersteck2.delete();
        }
        if (fileVersteck3.exists()) {
            fileVersteck3.delete();
        }
        if (fileVersteck4.exists()) {
            fileVersteck4.delete();
        }
        if (fileNutzdatenNeu.exists()) {
            fileNutzdatenNeu.delete();
        }
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
