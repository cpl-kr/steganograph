package de.platen.steganograph.uniformat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.PositionXY;
import de.platen.steganograph.datentypen.Positionsnummer;
import de.platen.steganograph.datentypen.X;
import de.platen.steganograph.datentypen.Y;

public class UniFormatBildFarbeTest {

    @Test
    public void testUebertrageBereichZuUniFormatMitAnzahlKanaeleKleiner4() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(3);
        BufferedImage bufferedImage = new BufferedImage(10, 20, BufferedImage.TYPE_4BYTE_ABGR);
        PositionXY abPosition = new PositionXY(new X(0), new Y(0));
        int rgb = 0xFFFFFFFF;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        try {
            uniFormatBild.uebertrageBereichZuUniFormat(bufferedImage, abPosition);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testUebertrageBereichZuUniFormatMitAnzahlKanaeleGroesser4() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(5);
        BufferedImage bufferedImage = new BufferedImage(10, 20, BufferedImage.TYPE_4BYTE_ABGR);
        PositionXY abPosition = new PositionXY(new X(0), new Y(0));
        int rgb = 0xFFFFFFFF;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        try {
            uniFormatBild.uebertrageBereichZuUniFormat(bufferedImage, abPosition);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Die Anzahl der Kanäle ist nicht passend.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBereichVonUniFormatMitAnzahlKanaeleKleiner4() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(3);
        BufferedImage bufferedImage = new BufferedImage(10, 20, BufferedImage.TYPE_4BYTE_ABGR);
        PositionXY abPosition = new PositionXY(new X(0), new Y(0));
        try {
            uniFormatBild.uebertrageBereichVonUniFormat(bufferedImage, abPosition);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testUebertrageBereichVonUniFormatMitAnzahlKanaeleGroesser4() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(5);
        BufferedImage bufferedImage = new BufferedImage(10, 20, BufferedImage.TYPE_4BYTE_ABGR);
        PositionXY abPosition = new PositionXY(new X(0), new Y(0));
        try {
            uniFormatBild.uebertrageBereichVonUniFormat(bufferedImage, abPosition);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Die Anzahl der Kanäle ist nicht passend.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBereichZuUniFormatOhneAlphaKanal() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = new BufferedImage(10, 20, BufferedImage.TYPE_BYTE_GRAY);
        PositionXY abPosition = new PositionXY(new X(0), new Y(0));
        try {
            uniFormatBild.uebertrageBereichZuUniFormat(bufferedImage, abPosition);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Der Bildtyp wird nicht unterstützt.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBereichVonUniFormatOhneAlphaKanal() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = new BufferedImage(10, 20, BufferedImage.TYPE_BYTE_GRAY);
        PositionXY abPosition = new PositionXY(new X(0), new Y(0));
        try {
            uniFormatBild.uebertrageBereichVonUniFormat(bufferedImage, abPosition);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Der Bildtyp wird nicht unterstützt.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBildpunktZuUniFormatParameterBufferedImageNull() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = null;
        PositionXY positionXY = new PositionXY(new X(1), new Y(1));
        Positionsnummer positionsnummer = new Positionsnummer(1);
        try {
            uniFormatBild.uebertrageBildpunktZuUniFormat(bufferedImage, positionXY, positionsnummer);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBildpunktZuUniFormatParameterPositionXYNull() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        PositionXY positionXY = null;
        Positionsnummer positionsnummer = new Positionsnummer(1);
        try {
            uniFormatBild.uebertrageBildpunktZuUniFormat(bufferedImage, positionXY, positionsnummer);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBildpunktZuUniFormatParameterPositionsnummerNull() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        PositionXY positionXY = new PositionXY(new X(1), new Y(1));
        Positionsnummer positionsnummer = null;
        try {
            uniFormatBild.uebertrageBildpunktZuUniFormat(bufferedImage, positionXY, positionsnummer);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBildpunktVonUniFormatParameterBufferedImageNull() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = null;
        PositionXY positionXY = new PositionXY(new X(1), new Y(1));
        Positionsnummer positionsnummer = new Positionsnummer(1);
        try {
            uniFormatBild.uebertrageBildpunktVonUniFormat(bufferedImage, positionXY, positionsnummer);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBildpunktVonUniFormatParameterPositionXYNull() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        PositionXY positionXY = null;
        Positionsnummer positionsnummer = new Positionsnummer(1);
        try {
            uniFormatBild.uebertrageBildpunktVonUniFormat(bufferedImage, positionXY, positionsnummer);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBildpunktVonUniFormatParameterPositionsnummerNull() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        PositionXY positionXY = new PositionXY(new X(1), new Y(1));
        Positionsnummer positionsnummer = null;
        try {
            uniFormatBild.uebertrageBildpunktVonUniFormat(bufferedImage, positionXY, positionsnummer);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBildpunktZuUndVonUniFormat4Byte() {
        BufferedImage bufferedImageQuelle1 = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage bufferedImageZiel1 = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        uebertrageBildpunktZuUndVonUniFormat4Byte(bufferedImageQuelle1, bufferedImageZiel1);
        BufferedImage bufferedImageQuelle2 = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        BufferedImage bufferedImageZiel2 = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        uebertrageBildpunktZuUndVonUniFormat4Byte(bufferedImageQuelle2, bufferedImageZiel2);
    }

    @Test
    public void testCheckAnzahlKanaeleParameterNull() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        try {
            uniFormatBild.checkAnzahlKanaele(null, BufferedImage.TYPE_4BYTE_ABGR);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testtestCheckAnzahlKanaeleAnzahlRichtig() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(4);
        uniFormatBild.checkAnzahlKanaele(anzahlKanaele, BufferedImage.TYPE_4BYTE_ABGR);
    }

    @Test
    public void testtestCheckAnzahlKanaeleAnzahlFalsch() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(5);
        try {
            uniFormatBild.checkAnzahlKanaele(anzahlKanaele, BufferedImage.TYPE_4BYTE_ABGR);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Anzahl der Kanäle ist nicht passend.", e.getMessage());
        }
    }

    @Test
    public void testCheckBildtypParameterNull() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        try {
            uniFormatBild.checkBildtyp(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testCheckBildtypBildTypRichtig() {
        checkBildtypRichtig(new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR));
        checkBildtypRichtig(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
    }

    @Test
    public void testCheckBildtypBildTypFalsch() {
        checkBildtypFalsch(new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY));
        checkBildtypFalsch(new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR));
        checkBildtypFalsch(new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR_PRE));
        checkBildtypFalsch(new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_BINARY));
        checkBildtypFalsch(new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_INDEXED));
        checkBildtypFalsch(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB_PRE));
        checkBildtypFalsch(new BufferedImage(10, 10, BufferedImage.TYPE_INT_BGR));
        checkBildtypFalsch(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
        checkBildtypFalsch(new BufferedImage(10, 10, BufferedImage.TYPE_USHORT_555_RGB));
        checkBildtypFalsch(new BufferedImage(10, 10, BufferedImage.TYPE_USHORT_565_RGB));
        checkBildtypFalsch(new BufferedImage(10, 10, BufferedImage.TYPE_USHORT_GRAY));
    }

    private UniFormatBildFarbe erzeugeUniFormatBild(int kanalanzahl) {
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(512);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(kanalanzahl);
        Bittiefe bittiefe = new Bittiefe(2);
        List<Eintrag> eintraege = new ArrayList<>();
        return new UniFormatBildFarbe(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
    }

    private void uebertrageBildpunktZuUndVonUniFormat4Byte(BufferedImage bufferedImageQuelle,
            BufferedImage bufferedImageZiel) {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        int farbwert = 0x0A0B0C0D;
        bufferedImageQuelle.setRGB(1, 1, farbwert);
        PositionXY positionXY = new PositionXY(new X(1), new Y(1));
        Positionsnummer positionsnummer = new Positionsnummer(1);
        uniFormatBild.uebertrageBildpunktZuUniFormat(bufferedImageQuelle, positionXY, positionsnummer);
        bufferedImageZiel.setRGB(1, 1, 0xAFAFAFAF);
        uniFormatBild.uebertrageBildpunktVonUniFormat(bufferedImageZiel, positionXY, positionsnummer);
        assertEquals(farbwert, bufferedImageZiel.getRGB(1, 1));
    }

    private void checkBildtypRichtig(BufferedImage bufferedImage) {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        uniFormatBild.checkBildtyp(bufferedImage);
    }

    private void checkBildtypFalsch(BufferedImage bufferedImage) {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        try {
            uniFormatBild.checkBildtyp(bufferedImage);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Der Bildtyp wird nicht unterstützt.", e.getMessage());
        }
    }
}
