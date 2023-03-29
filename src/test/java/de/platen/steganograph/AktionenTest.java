package de.platen.steganograph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.platen.crypt.KeyVerwaltung;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.platen.extern.wavfile.WavFile;
import de.platen.extern.wavfile.WavFileException;
import de.platen.steganograph.utils.DateiUtils;

public class AktionenTest {

    private static final String DATEINAME_VERTEILREGELl = "src/test/resources/verteilregeln";
    private static final String DATEINAME_NUTZDATEN_ORIGINAL = "src/test/resources/nutzdatenOriginal";
    private static final String DATEINAME_BILD_ORIGINAL = "src/test/resources/bildoriginal.png";
    private static final String DATEINAME_BILD_VERSTECK = "src/test/resources/bildversteck.png";
    private static final String DATEINAME_AUDIO_ORIGINAL = "src/test/resources/audiooriginal.wav";
    private static final String DATEINAME_AUDIO_VERSTECK = "src/test/resources/audioversteck.wav";
    private static final String DATEINAME_NUTZDATEN_NEU = "src/test/resources/nutzdatenneu";
    private static final String VERZEICHNIS_NUTZDATEN_NEU = "src/test/resources/";
    private static final String DATEINAME_PUBLIC_KEY = "src/test/resources/public.pgp";
    private static final String DATEINAME_PRIVATE_KEY = "src/test/resources/private.pgp";
    private static final String ID = "person";
    private static final String PASSWORT = "passwort";

    @Before
    public void before() {
        loescheDateien();
    }

    @After
    public void after() {
        loescheDateien();
    }

    @Test
    public void testGeneriereVersteckeHoleBildFarbe() throws IOException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBildFarbe(DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_4BYTE_ABGR);
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_BILD_VERSTECK, DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(DATEINAME_BILD_VERSTECK);
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleBildGrau() throws IOException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "1000";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "1";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBildGrau(DATEINAME_BILD_ORIGINAL, 150, 150);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_BYTE_GRAY);
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_BILD_VERSTECK, DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(DATEINAME_BILD_VERSTECK);
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleBildDateinameAusStartblock() throws IOException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBildFarbe(DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        assertTrue(fileNutzdatenOriginal.exists());
        fileNutzdatenOriginal.delete();
        pruefeVersteckbild(DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_4BYTE_ABGR);
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_BILD_VERSTECK, VERZEICHNIS_NUTZDATEN_NEU);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileBildOriginal = new File(DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(DATEINAME_BILD_VERSTECK);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        byte[] nutzdaten = DateiUtils.leseDatei(DATEINAME_NUTZDATEN_ORIGINAL);
        assertEquals(10, nutzdaten.length);
        byte[] vergleichsdaten = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
        assertArrayEquals(vergleichsdaten, nutzdaten);
    }

    @Test
    public void testGeneriereVersteckeHoleBildFuer1BlockAlsKomplettblock() throws IOException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 50);
        erzeugeBildFarbe(DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_4BYTE_ABGR);
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_BILD_VERSTECK, DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(DATEINAME_BILD_VERSTECK);
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleBildFuerMehrereBloeckeKompletteBloecke() throws IOException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 500);
        erzeugeBildFarbe(DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_4BYTE_ABGR);
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_BILD_VERSTECK, DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(DATEINAME_BILD_VERSTECK);
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleBildFuerMehrereBloeckeLetzterBlockTeilblock() throws IOException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 525);
        erzeugeBildFarbe(DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_4BYTE_ABGR);
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_BILD_VERSTECK, DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(DATEINAME_BILD_VERSTECK);
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeBildZuvielNutzdaten() throws IOException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 1201);
        erzeugeBildFarbe(DATEINAME_BILD_ORIGINAL, 50, 50);
        try {
            aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                    DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Es können nicht alle Nutzdaten im Bild untergebracht werden.", e.getMessage());
        }
    }

    @Test
    public void testGeneriereVersteckeBildZuvielDatenImBlock() throws IOException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "100";
        String anzahlNutzdaten = "24";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBildFarbe(DATEINAME_BILD_ORIGINAL, 50, 50);
        try {
            aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                    DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Es können nicht alle Daten im Block untergebracht werden.", e.getMessage());
        }
    }

    @Test
    public void testGeneriereVersteckeHoleAudio() throws IOException, WavFileException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "1000";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";

        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeAudiodatei(5000);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_AUDIO_ORIGINAL,
                DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_AUDIO_VERSTECK, DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileAudioOriginal = new File(DATEINAME_AUDIO_ORIGINAL);
        File fileAudioVersteck = new File(DATEINAME_AUDIO_VERSTECK);
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileAudioOriginal.exists());
        assertTrue(fileAudioVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleAudioDateinameAusStartblock() throws IOException, WavFileException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "1000";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeAudiodatei(5000);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_AUDIO_ORIGINAL,
                DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        assertTrue(fileNutzdatenOriginal.exists());
        fileNutzdatenOriginal.delete();
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_AUDIO_VERSTECK, VERZEICHNIS_NUTZDATEN_NEU);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileBildOriginal = new File(DATEINAME_AUDIO_ORIGINAL);
        File fileBildVersteck = new File(DATEINAME_AUDIO_VERSTECK);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        byte[] nutzdaten = DateiUtils.leseDatei(DATEINAME_NUTZDATEN_ORIGINAL);
        assertEquals(10, nutzdaten.length);
        byte[] vergleichsdaten = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
        assertArrayEquals(vergleichsdaten, nutzdaten);
    }

    @Test
    public void testGeneriereVersteckeHoleAudioFuer1BlockAlsKomplettblock() throws IOException, WavFileException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "1000";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 50);
        erzeugeAudiodatei(5000);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_AUDIO_ORIGINAL,
                DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_AUDIO_VERSTECK, DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileAudioOriginal = new File(DATEINAME_AUDIO_ORIGINAL);
        File fileAudioVersteck = new File(DATEINAME_AUDIO_VERSTECK);
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileAudioOriginal.exists());
        assertTrue(fileAudioVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleAudioFuerMehrereBloeckeKompletteBloecke()
            throws IOException, WavFileException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "200";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 150);
        erzeugeAudiodatei(5000);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_AUDIO_ORIGINAL,
                DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_AUDIO_VERSTECK, DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileAudioOriginal = new File(DATEINAME_AUDIO_ORIGINAL);
        File fileAudioVersteck = new File(DATEINAME_AUDIO_VERSTECK);
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileAudioOriginal.exists());
        assertTrue(fileAudioVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleAudioFuerMehrereBloeckeLetzterBlockTeilblock()
            throws IOException, WavFileException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "200";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 125);
        erzeugeAudiodatei(5000);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_AUDIO_ORIGINAL,
                DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_AUDIO_VERSTECK, DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileAudioOriginal = new File(DATEINAME_AUDIO_ORIGINAL);
        File fileAudioVersteck = new File(DATEINAME_AUDIO_VERSTECK);
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileAudioOriginal.exists());
        assertTrue(fileAudioVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleBildMitEncrypt() throws IOException {
        final KeyVerwaltung keyVerwaltung = new KeyVerwaltung();
        keyVerwaltung.erzeugeUndSpeichereKeyPaar(DATEINAME_PUBLIC_KEY, DATEINAME_PRIVATE_KEY, ID);
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        List<String> dateinamenPublicKey = new ArrayList<>();
        dateinamenPublicKey.add(DATEINAME_PUBLIC_KEY);
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl, dateinamenPublicKey, null);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBildFarbe(DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES, DATEINAME_PRIVATE_KEY, null);
        pruefeVersteckbild(DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_4BYTE_ABGR);
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_BILD_VERSTECK, DATEINAME_NUTZDATEN_NEU, DATEINAME_PRIVATE_KEY, null);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(DATEINAME_BILD_VERSTECK);
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_NUTZDATEN_NEU);
        File filePublicKey = new File(DATEINAME_PUBLIC_KEY);
        if (filePublicKey.exists()) {
            filePublicKey.delete();
        }
        File filePrivateKey = new File(DATEINAME_PRIVATE_KEY);
        if (filePrivateKey.exists()) {
            filePrivateKey.delete();
        }
    }

    @Test
    public void testGeneriereVersteckeHoleAudioMitEncrypt() throws IOException, WavFileException {
        final KeyVerwaltung keyVerwaltung = new KeyVerwaltung();
        keyVerwaltung.erzeugeUndSpeichereKeyPaar(DATEINAME_PUBLIC_KEY, DATEINAME_PRIVATE_KEY, ID);
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio());
        String blockgroesse = "1000";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";
        List<String> dateinamenPublicKey = new ArrayList<>();
        dateinamenPublicKey.add(DATEINAME_PUBLIC_KEY);
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl, dateinamenPublicKey, null);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeAudiodatei(5000);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_AUDIO_ORIGINAL,
                DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES, DATEINAME_PRIVATE_KEY, null);
        aktionen.hole(DATEINAME_VERTEILREGELl, DATEINAME_AUDIO_VERSTECK, DATEINAME_NUTZDATEN_NEU, DATEINAME_PRIVATE_KEY, null);
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileAudioOriginal = new File(DATEINAME_AUDIO_ORIGINAL);
        File fileAudioVersteck = new File(DATEINAME_AUDIO_VERSTECK);
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileAudioOriginal.exists());
        assertTrue(fileAudioVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_NUTZDATEN_NEU);
        File filePublicKey = new File(DATEINAME_PUBLIC_KEY);
        if (filePublicKey.exists()) {
            filePublicKey.delete();
        }
        File filePrivateKey = new File(DATEINAME_PRIVATE_KEY);
        if (filePrivateKey.exists()) {
            filePrivateKey.delete();
        }
    }

    @Test
    public void testErzeugeKeyPaarOhnePasswort() {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(), new AktionHolenAusBild(), new AktionHolenAusAudio());
        aktionen.erzeugeKeyPaar(ID, DATEINAME_PUBLIC_KEY, DATEINAME_PRIVATE_KEY);
        File filePublicKey = new File(DATEINAME_PUBLIC_KEY);
        File filePrivateKey = new File(DATEINAME_PRIVATE_KEY);
        assertTrue(filePublicKey.exists());
        assertTrue(filePrivateKey.exists());
        filePublicKey.delete();
        filePrivateKey.delete();
    }

    @Test
    public void testErzeugeKeyPaarMitPasswort() {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(), new AktionHolenAusBild(), new AktionHolenAusAudio());
        aktionen.erzeugeKeyPaar(ID, DATEINAME_PUBLIC_KEY, DATEINAME_PRIVATE_KEY, PASSWORT);
        File filePublicKey = new File(DATEINAME_PUBLIC_KEY);
        File filePrivateKey = new File(DATEINAME_PRIVATE_KEY);
        assertTrue(filePublicKey.exists());
        assertTrue(filePrivateKey.exists());
        filePublicKey.delete();
        filePrivateKey.delete();
    }

    private void loescheDateien() {
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(DATEINAME_BILD_VERSTECK);
        File fileAudioOriginal = new File(DATEINAME_AUDIO_ORIGINAL);
        File fileAudioVersteck = new File(DATEINAME_AUDIO_VERSTECK);
        File fileNutzdatenNeu = new File(DATEINAME_NUTZDATEN_NEU);
        if (fileVerteilregel.exists()) {
            fileVerteilregel.delete();
        }
        if (fileNutzdatenOriginal.exists()) {
            fileNutzdatenOriginal.delete();
        }
        if (fileBildOriginal.exists()) {
            fileBildOriginal.delete();
        }
        if (fileBildVersteck.exists()) {
            fileBildVersteck.delete();
        }
        if (fileAudioOriginal.exists()) {
            fileAudioOriginal.delete();
        }
        if (fileAudioVersteck.exists()) {
            fileAudioVersteck.delete();
        }
        if (fileNutzdatenNeu.exists()) {
            fileNutzdatenNeu.delete();
        }

    }

    private void erzeugeNutzdaten(String dateiname, int anzahl) throws IOException {
        boolean ende = false;
        int position = 0;
        byte[] daten = new byte[anzahl];
        while (!ende) {
            for (int index = 0; index < 255; index++) {
                daten[position] = (byte) index;
                position++;
                if (position == anzahl) {
                    DateiUtils.schreibeDatei(dateiname, daten);
                    return;
                }
            }
        }
    }

    private void erzeugeBildFarbe(String dateiname, int breite, int hoehe) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(breite, hoehe, BufferedImage.TYPE_4BYTE_ABGR);
        int rgb = 0xFF000000;
        for (int x = 0; x < breite; x++) {
            for (int y = 0; y < hoehe; y++) {
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        DateiUtils.schreibeBild(dateiname, bufferedImage);
    }

    private void erzeugeBildGrau(String dateiname, int breite, int hoehe) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(breite, hoehe, BufferedImage.TYPE_BYTE_GRAY);
        int rgb = 0xFF000000;
        for (int x = 0; x < breite; x++) {
            for (int y = 0; y < hoehe; y++) {
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        DateiUtils.schreibeBild(dateiname, bufferedImage);
    }

    private void pruefeVersteckbild(String dateiname, int bildtyp) throws IOException {
        BufferedImage bufferedImage = DateiUtils.leseBild(dateiname);
        int breite = bufferedImage.getWidth();
        int hoehe = bufferedImage.getHeight();
        assertEquals(bildtyp, bufferedImage.getType());
        boolean alleGleich = true;
        for (int x = 0; x < breite; x++) {
            for (int y = 0; y < hoehe; y++) {
                if (bufferedImage.getRGB(x, y) != 0xFF000000) {
                    alleGleich = false;
                }
            }
        }
        assertFalse(alleGleich);
    }

    // private void gebeBildAus(String dateiname) throws IOException {
    // BufferedImage bufferedImage = DateiUtils.leseBild(dateiname);
    // int breite = bufferedImage.getWidth();
    // int hoehe = bufferedImage.getHeight();
    // for (int y = 0; y < breite; y++) {
    // System.out.println("Zeile " + y);
    // for (int x = 0; x < hoehe; x++) {
    // String s = Integer.toBinaryString(bufferedImage.getRGB(x, y));
    // System.out.println(s);
    // }
    // }
    // }

    private void vergleicheNutzdaten(String dateinameNutzdatenOriginal, String dateinameNutzdatenNeu)
            throws IOException {
        byte[] datenOriginal = DateiUtils.leseDatei(dateinameNutzdatenOriginal);
        byte[] datenNeu = DateiUtils.leseDatei(dateinameNutzdatenNeu);
        // if (datenOriginal.length == datenNeu.length) {
        // System.out.println("Nutzdatenvergleich:");
        // for (int index = 0; index < datenOriginal.length; index++) {
        // String s = String.format("%2x %2x", datenOriginal[index], datenNeu[index]);
        // System.out.println(s);
        // }
        // }
        assertArrayEquals(datenOriginal, datenNeu);
    }

    private void erzeugeAudiodatei(int maximalFrames) throws IOException, WavFileException {
        int sekunden = 10;
        int numChannels = 2;
        int validBits = 16;
        int sampleRate = 44100;
        int numFrames = sampleRate * sekunden;
        if (maximalFrames > 0) {
            numFrames = maximalFrames;
        }
        WavFile wavFile = WavFile.newWavFile(new File(DATEINAME_AUDIO_ORIGINAL), numChannels, numFrames, validBits,
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
    }
}
