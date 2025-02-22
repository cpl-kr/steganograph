package de.platen.steganograph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import de.platen.crypt.KeyVerwaltung;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import de.platen.extern.wavfile.WavFile;
import de.platen.extern.wavfile.WavFileException;
import de.platen.steganograph.utils.DateiUtils;

public class AktionenTest {

    private static final String VERZEICHNIS = "src/test/resources/daten";
    private static final String FILEDELIMITER = FileSystems.getDefault().getSeparator();
    private static final String DATEINAME_VERTEILREGELl = "verteilregeln";
    private static final String DATEINAME_NUTZDATEN_ORIGINAL = "nutzdatenOriginal";
    private static final String DATEINAME_BILD_ORIGINAL = "bildoriginal.png";
    private static final String DATEINAME_BILD_VERSTECK = "bildversteck.png";
    private static final String DATEINAME_AUDIO_ORIGINAL = "audiooriginal.wav";
    private static final String DATEINAME_AUDIO_VERSTECK = "audioversteck.wav";
    private static final String DATEINAME_NUTZDATEN_NEU = "nutzdatenneu";
    private static final String DATEINAME_ZUFALLSDATEI = "zufallsdatei";
    private static final String DATEINAME_VON_ZUFALLSDATEI = "ausZufallsdatei";
    private static final String DATEINAME_PUBLIC_KEY = "public.pgp";
    private static final String DATEINAME_PRIVATE_KEY = "private.pgp";
    private static final String ID = "person";
    private static final String PASSWORT = "passwort";

    @BeforeClass
    public static void before() {
        File file = new File(VERZEICHNIS);
        if (!file.mkdirs()) {
            System.out.println("Verzeichnis " + VERZEICHNIS + " konnte nicht erzeugt werden.");
        } else {
            System.out.println("Verzeichnis " + VERZEICHNIS + " erzeugt.");
        }
    }

    @AfterClass
    public static void after() throws IOException {
        File file = new File(VERZEICHNIS);
        FileUtils.deleteDirectory(file);
    }

    @Test
    public void testGeneriereVersteckeHoleBildFarbe() throws IOException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        final String verzeichnis = "test1";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBildFarbe(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_4BYTE_ABGR);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK);
        File fileNutzdatenNeu = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleBildGrau() throws IOException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "1000";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "1";
        String bittiefe = "2";
        final String verzeichnis = "test2";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBildGrau(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL, 150, 150);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_BYTE_GRAY);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK);
        File fileNutzdatenNeu = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleBildDateinameAusStartblock() throws IOException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        final String verzeichnis = "test3";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBildFarbe(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        assertTrue(fileNutzdatenOriginal.exists());
        fileNutzdatenOriginal.delete();
        pruefeVersteckbild(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_4BYTE_ABGR);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, pfad);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileBildOriginal = new File(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        byte[] nutzdaten = DateiUtils.leseDatei(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        assertEquals(10, nutzdaten.length);
        byte[] vergleichsdaten = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
        assertArrayEquals(vergleichsdaten, nutzdaten);
    }

    @Test
    public void testGeneriereVersteckeHoleBildFuer1BlockAlsKomplettblock() throws IOException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        final String verzeichnis = "test4";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 50);
        erzeugeBildFarbe(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_4BYTE_ABGR);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK);
        File fileNutzdatenNeu = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleBildFuerMehrereBloeckeKompletteBloecke() throws IOException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        final String verzeichnis = "test5";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 500);
        erzeugeBildFarbe(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_4BYTE_ABGR);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK);
        File fileNutzdatenNeu = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleBildFuerMehrereBloeckeLetzterBlockTeilblock() throws IOException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        final String verzeichnis = "test6";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 525);
        erzeugeBildFarbe(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_4BYTE_ABGR);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK);
        File fileNutzdatenNeu = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeBildZuvielNutzdaten() throws IOException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        final String verzeichnis = "test7";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 1201);
        erzeugeBildFarbe(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL, 50, 50);
        try {
            aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL,
                    pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Es können nicht alle Nutzdaten im Bild untergebracht werden.", e.getMessage());
        }
    }

    @Test
    public void testGeneriereVersteckeBildZuvielDatenImBlock() throws IOException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "24";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        final String verzeichnis = "test8";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBildFarbe(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL, 50, 50);
        try {
            aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL,
                    pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Es können nicht alle Daten im Block untergebracht werden.", e.getMessage());
        }
    }

    @Test
    public void testGeneriereVersteckeHoleAudio() throws IOException, WavFileException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "1000";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";
        final String verzeichnis = "test9";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeAudiodatei(5000, pfad);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        File fileAudioOriginal = new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL);
        File fileAudioVersteck = new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK);
        File fileNutzdatenNeu = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileAudioOriginal.exists());
        assertTrue(fileAudioVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleAudioDateinameAusStartblock() throws IOException, WavFileException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "1000";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";
        final String verzeichnis = "test10";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeAudiodatei(5000, pfad);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        assertTrue(fileNutzdatenOriginal.exists());
        fileNutzdatenOriginal.delete();
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, pfad);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileBildOriginal = new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL);
        File fileBildVersteck = new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        byte[] nutzdaten = DateiUtils.leseDatei(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        assertEquals(10, nutzdaten.length);
        byte[] vergleichsdaten = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
        assertArrayEquals(vergleichsdaten, nutzdaten);
    }

    @Test
    public void testGeneriereVersteckeHoleAudioFuer1BlockAlsKomplettblock() throws IOException, WavFileException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "1000";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";
        final String verzeichnis = "test11";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 50);
        erzeugeAudiodatei(5000, pfad);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        File fileAudioOriginal = new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL);
        File fileAudioVersteck = new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK);
        File fileNutzdatenNeu = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileAudioOriginal.exists());
        assertTrue(fileAudioVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleAudioFuerMehrereBloeckeKompletteBloecke()
            throws IOException, WavFileException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "200";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";
        final String verzeichnis = "test12";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 150);
        erzeugeAudiodatei(5000, pfad);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        File fileAudioOriginal = new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL);
        File fileAudioVersteck = new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK);
        File fileNutzdatenNeu = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileAudioOriginal.exists());
        assertTrue(fileAudioVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleAudioFuerMehrereBloeckeLetzterBlockTeilblock()
            throws IOException, WavFileException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "200";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";
        final String verzeichnis = "test13";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 125);
        erzeugeAudiodatei(5000, pfad);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        File fileAudioOriginal = new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL);
        File fileAudioVersteck = new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK);
        File fileNutzdatenNeu = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileAudioOriginal.exists());
        assertTrue(fileAudioVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleBildMitEncrypt() throws IOException {
        final KeyVerwaltung keyVerwaltung = new KeyVerwaltung();
        final String verzeichnis = "test14";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        keyVerwaltung.erzeugeUndSpeichereKeyPaar(pfad + FILEDELIMITER + DATEINAME_PUBLIC_KEY, pfad + FILEDELIMITER + DATEINAME_PRIVATE_KEY, ID);
        Aktionen aktionen = getAktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        List<String> dateinamenPublicKey = new ArrayList<>();
        dateinamenPublicKey.add(pfad + FILEDELIMITER + DATEINAME_PUBLIC_KEY);
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, dateinamenPublicKey, null);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBildFarbe(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES, pfad + FILEDELIMITER + DATEINAME_PRIVATE_KEY, null);
        pruefeVersteckbild(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, BufferedImage.TYPE_4BYTE_ABGR);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU, pfad + FILEDELIMITER + DATEINAME_PRIVATE_KEY, null);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(pfad + FILEDELIMITER + DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(pfad + FILEDELIMITER + DATEINAME_BILD_VERSTECK);
        File fileNutzdatenNeu = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileBildOriginal.exists());
        assertTrue(fileBildVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testGeneriereVersteckeHoleAudioMitEncrypt() throws IOException, WavFileException {
        final KeyVerwaltung keyVerwaltung = new KeyVerwaltung();
        final String verzeichnis = "test15";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        keyVerwaltung.erzeugeUndSpeichereKeyPaar(pfad + FILEDELIMITER + DATEINAME_PUBLIC_KEY, pfad + FILEDELIMITER + DATEINAME_PRIVATE_KEY, ID);
        Aktionen aktionen = getAktionen();
        String blockgroesse = "1000";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";
        List<String> dateinamenPublicKey = new ArrayList<>();
        dateinamenPublicKey.add(pfad + FILEDELIMITER + DATEINAME_PUBLIC_KEY);
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, dateinamenPublicKey, null);
        erzeugeNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeAudiodatei(5000, pfad);
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL,
                pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, Verrauschoption.ALLES, pfad + FILEDELIMITER + DATEINAME_PRIVATE_KEY, null);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU, pfad + FILEDELIMITER + DATEINAME_PRIVATE_KEY, null);
        File fileVerteilregel = new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL);
        File fileAudioOriginal = new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_ORIGINAL);
        File fileAudioVersteck = new File(pfad + FILEDELIMITER + DATEINAME_AUDIO_VERSTECK);
        File fileNutzdatenNeu = new File(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
        assertTrue(fileVerteilregel.exists());
        assertTrue(fileNutzdatenOriginal.exists());
        assertTrue(fileAudioOriginal.exists());
        assertTrue(fileAudioVersteck.exists());
        assertTrue(fileNutzdatenNeu.exists());
        vergleicheNutzdaten(pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_ORIGINAL, pfad + FILEDELIMITER + DATEINAME_NUTZDATEN_NEU);
    }

    @Test
    public void testErzeugeKeyPaarOhnePasswort() {
        Aktionen aktionen = getAktionen();
        final String verzeichnis = "test16";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.erzeugeKeyPaar(ID, pfad + FILEDELIMITER + DATEINAME_PUBLIC_KEY, pfad + FILEDELIMITER + DATEINAME_PRIVATE_KEY);
        File filePublicKey = new File(pfad + FILEDELIMITER + DATEINAME_PUBLIC_KEY);
        File filePrivateKey = new File(pfad + FILEDELIMITER + DATEINAME_PRIVATE_KEY);
        assertTrue(filePublicKey.exists());
        assertTrue(filePrivateKey.exists());
    }

    @Test
    public void testErzeugeKeyPaarMitPasswort() {
        Aktionen aktionen = getAktionen();
        final String verzeichnis = "test17";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.erzeugeKeyPaar(ID, pfad + FILEDELIMITER + DATEINAME_PUBLIC_KEY, pfad + FILEDELIMITER + DATEINAME_PRIVATE_KEY, PASSWORT);
        File filePublicKey = new File(pfad + FILEDELIMITER + DATEINAME_PUBLIC_KEY);
        File filePrivateKey = new File(pfad + FILEDELIMITER + DATEINAME_PRIVATE_KEY);
        assertTrue(filePublicKey.exists());
        assertTrue(filePrivateKey.exists());
    }

    @Test
    public void testInDateAusDatei() throws IOException {
        Aktionen aktionen = getAktionen();
        String blockgroesse = "200";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "2";
        String bittiefe = "2";
        final String verzeichnis = "test18";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl);
        byte[] bytesOriginal = FileUtils.readFileToByteArray(new File(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl));
        String laengeZufallsdatei = "100000";
        String offsetInZufallsdatei = "1000";
        String mitErzeugung = "true";
        aktionen.verstecke(pfad + FILEDELIMITER + DATEINAME_VERTEILREGELl, pfad + FILEDELIMITER + DATEINAME_ZUFALLSDATEI, laengeZufallsdatei, offsetInZufallsdatei, mitErzeugung);
        String laengeDaten = String.valueOf(bytesOriginal.length);
        aktionen.hole(pfad + FILEDELIMITER + DATEINAME_ZUFALLSDATEI, pfad + FILEDELIMITER + DATEINAME_VON_ZUFALLSDATEI, offsetInZufallsdatei, laengeDaten);
        byte[] bytesAusZufallsdatei = FileUtils.readFileToByteArray(new File(pfad + FILEDELIMITER + DATEINAME_VON_ZUFALLSDATEI));
        assertArrayEquals(bytesOriginal, bytesAusZufallsdatei);
    }

    @Test
    public void testErzeugeZufallsdatei() throws IOException {
        Aktionen aktionen = getAktionen();
        String laenge = "100";
        final String verzeichnis = "test19";
        final String pfad = VERZEICHNIS + FILEDELIMITER + verzeichnis;
        final File file = new File(pfad);
        file.mkdirs();
        aktionen.erzeuge(pfad + FILEDELIMITER + DATEINAME_ZUFALLSDATEI, laenge);
        byte[] bytesAusZufallsdatei = FileUtils.readFileToByteArray(new File(pfad + FILEDELIMITER + DATEINAME_ZUFALLSDATEI));
        assertEquals(100, bytesAusZufallsdatei.length);
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

    private void vergleicheNutzdaten(String dateinameNutzdatenOriginal, String dateinameNutzdatenNeu)
            throws IOException {
        byte[] datenOriginal = DateiUtils.leseDatei(dateinameNutzdatenOriginal);
        byte[] datenNeu = DateiUtils.leseDatei(dateinameNutzdatenNeu);
        assertArrayEquals(datenOriginal, datenNeu);
    }

    private void erzeugeAudiodatei(int maximalFrames, String verzeichnis) throws IOException, WavFileException {
        int sekunden = 10;
        int numChannels = 2;
        int validBits = 16;
        int sampleRate = 44100;
        int numFrames = sampleRate * sekunden;
        if (maximalFrames > 0) {
            numFrames = maximalFrames;
        }
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
    }

    private static @NotNull Aktionen getAktionen() {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio(),
                new AktionZufallsdatei(), new AktionInDatei(), new AktionAusDatei());
        return aktionen;
    }
}
