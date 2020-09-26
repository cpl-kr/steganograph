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
    public void testUebertrageBildpunktZuUndVonUniFormat() {
        UniFormatBildFarbe uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImageQuelle = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        bufferedImageQuelle.setRGB(1, 1, 0x0F0F0F0F);
        PositionXY positionXY = new PositionXY(new X(1), new Y(1));
        Positionsnummer positionsnummer = new Positionsnummer(1);
        uniFormatBild.uebertrageBildpunktZuUniFormat(bufferedImageQuelle, positionXY, positionsnummer);
        BufferedImage bufferedImageZiel = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        bufferedImageZiel.setRGB(1, 1, 0xAFAFAFAF);
        uniFormatBild.uebertrageBildpunktVonUniFormat(bufferedImageZiel, positionXY, positionsnummer);
        assertEquals(0x0F0F0F0F, bufferedImageZiel.getRGB(1, 1));
    }

    @Test
    public void testCheckAnzahlKanaeleParameterNull() {
        UniFormatBildFarbe uniformatBild4ByteABGR = erzeugeUniFormatBild(4);
        try {
            uniformatBild4ByteABGR.checkAnzahlKanaele(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testtestCheckAnzahlKanaeleAnzahlRichtig() {
        UniFormatBildFarbe uniformatBild4ByteABGR = erzeugeUniFormatBild(4);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(4);
        uniformatBild4ByteABGR.checkAnzahlKanaele(anzahlKanaele);
    }

    @Test
    public void testtestCheckAnzahlKanaeleAnzahlFalsch() {
        UniFormatBildFarbe uniformatBild4ByteABGR = erzeugeUniFormatBild(4);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(5);
        try {
            uniformatBild4ByteABGR.checkAnzahlKanaele(anzahlKanaele);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Anzahl der Kanäle ist nicht passend.", e.getMessage());
        }
    }

    @Test
    public void testCheckBildtypParameterNull() {
        UniFormatBildFarbe uniformatBild4ByteABGR = erzeugeUniFormatBild(4);
        try {
            uniformatBild4ByteABGR.checkBildtyp(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testCheckBildtypBildTypRichtig() {
        UniFormatBildFarbe uniformatBild4ByteABGR = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        uniformatBild4ByteABGR.checkBildtyp(bufferedImage);
    }

    @Test
    public void testCheckBildtypBildTypFalsch() {
        UniFormatBildFarbe uniformatBild4ByteABGR = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
        try {
            uniformatBild4ByteABGR.checkBildtyp(bufferedImage);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Der Bildtyp wird nicht unterstützt.", e.getMessage());
        }

    }

    private UniFormatBildFarbe erzeugeUniFormatBild(int kanalanzahl) {
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(512);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(kanalanzahl);
        Bittiefe bittiefe = new Bittiefe(2);
        List<Eintrag> eintraege = new ArrayList<>();
        return new UniFormatBildFarbe(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
    }
}