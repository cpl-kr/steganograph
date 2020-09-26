package de.platen.steganograph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.platen.steganograph.utils.DateiUtils;

public class AktionenTest {

    private static final String DATEINAME_VERTEILREGELl = "src/test/resources/verteilregeln";
    private static final String DATEINAME_NUTZDATEN_ORIGINAL = "src/test/resources/nutzdatenOriginal";
    private static final String DATEINAME_BILD_ORIGINAL = "src/test/resources/bildoriginal";
    private static final String DATEINAME_BILD_VERSTECK = "src/test/resources/bildversteck";
    private static final String DATEINAME_NUTZDATEN_NEU = "src/test/resources/nutzdatenneu";
    private static final String VERZEICHNIS_NUTZDATEN_NEU = "src/test/resources/";

    @Before
    public void before() {
        loescheDateien();
    }

    @After
    public void after() {
        loescheDateien();
    }

    @Test
    public void testGeneriereVersteckeHoleFuer1BlockAlsTeilblock() throws IOException {
        Aktionen aktionen = new Aktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBild(DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(DATEINAME_BILD_VERSTECK);
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
    public void testGeneriereVersteckeHoleFuer1BlockAlsKomplettblock() throws IOException {
        Aktionen aktionen = new Aktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 50);
        erzeugeBild(DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(DATEINAME_BILD_VERSTECK);
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
    public void testGeneriereVersteckeHoleFuerMehrereBloeckeKompletteBloecke() throws IOException {
        Aktionen aktionen = new Aktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 500);
        erzeugeBild(DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(DATEINAME_BILD_VERSTECK);
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
    public void testGeneriereVersteckeHoleFuerMehrereBloeckeLetzterBlockTeilblock() throws IOException {
        Aktionen aktionen = new Aktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 525);
        erzeugeBild(DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        pruefeVersteckbild(DATEINAME_BILD_VERSTECK);
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
    public void testGeneriereVersteckeHoleFuer1BlockMitDateinameAusStartblock() throws IOException {
        Aktionen aktionen = new Aktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBild(DATEINAME_BILD_ORIGINAL, 50, 50);
        aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        assertTrue(fileNutzdatenOriginal.exists());
        fileNutzdatenOriginal.delete();
        pruefeVersteckbild(DATEINAME_BILD_VERSTECK);
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
    public void testGeneriereVersteckeZuvielNutzdaten() throws IOException {
        Aktionen aktionen = new Aktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "50";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 1201);
        erzeugeBild(DATEINAME_BILD_ORIGINAL, 50, 50);
        try {
            aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                    DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Es können nicht alle Nutzdaten im Bild untergebracht werden.", e.getMessage());
        }

    }

    @Test
    public void testGeneriereVersteckeZuvielDatenImBlock() throws IOException {
        Aktionen aktionen = new Aktionen();
        String blockgroesse = "100";
        String anzahlNutzdaten = "24";
        String anzahlKanaele = "4";
        String bittiefe = "2";
        aktionen.generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, DATEINAME_VERTEILREGELl);
        erzeugeNutzdaten(DATEINAME_NUTZDATEN_ORIGINAL, 10);
        erzeugeBild(DATEINAME_BILD_ORIGINAL, 50, 50);
        try {
            aktionen.verstecke(DATEINAME_VERTEILREGELl, DATEINAME_NUTZDATEN_ORIGINAL, DATEINAME_BILD_ORIGINAL,
                    DATEINAME_BILD_VERSTECK, Verrauschoption.ALLES);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Es können nicht alle Daten im Block untergebracht werden.", e.getMessage());
        }

    }

    private void loescheDateien() {
        File fileVerteilregel = new File(DATEINAME_VERTEILREGELl);
        File fileNutzdatenOriginal = new File(DATEINAME_NUTZDATEN_ORIGINAL);
        File fileBildOriginal = new File(DATEINAME_BILD_ORIGINAL);
        File fileBildVersteck = new File(DATEINAME_BILD_VERSTECK);
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

    private void erzeugeBild(String dateiname, int breite, int hoehe) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(breite, hoehe, BufferedImage.TYPE_4BYTE_ABGR);
        int rgb = 0xFF000000;
        for (int x = 0; x < breite; x++) {
            for (int y = 0; y < hoehe; y++) {
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        DateiUtils.schreibeBild(dateiname, bufferedImage);
    }

    private void pruefeVersteckbild(String dateiname) throws IOException {
        BufferedImage bufferedImage = DateiUtils.leseBild(dateiname);
        int breite = bufferedImage.getWidth();
        int hoehe = bufferedImage.getHeight();
        assertEquals(BufferedImage.TYPE_4BYTE_ABGR, bufferedImage.getType());
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
}
