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

public class UniFormatBildGrauTest {

    @Test
    public void testUebertrageBereichZuUniFormatMitAnzahlKanaeleGroesser1() {
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(2);
        BufferedImage bufferedImage = new BufferedImage(10, 20, BufferedImage.TYPE_BYTE_GRAY);
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
    public void testUebertrageBereichVonUniFormatMitAnzahlKanaeleGroesser1() {
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(2);
        BufferedImage bufferedImage = new BufferedImage(10, 20, BufferedImage.TYPE_BYTE_GRAY);
        PositionXY abPosition = new PositionXY(new X(0), new Y(0));
        try {
            uniFormatBild.uebertrageBereichVonUniFormat(bufferedImage, abPosition);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Die Anzahl der Kanäle ist nicht passend.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBildpunktZuUniFormatParameterBufferedImageNull() {
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(1);
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
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(1);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
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
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(1);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
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
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(1);
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
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(1);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
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
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(1);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
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
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(1);
        BufferedImage bufferedImageQuelle = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
        int[] farbe = new int[1];
        farbe[0] = 0x000F;
        bufferedImageQuelle.getRaster().setPixel(1, 1, farbe);
        PositionXY positionXY = new PositionXY(new X(1), new Y(1));
        Positionsnummer positionsnummer = new Positionsnummer(1);
        uniFormatBild.uebertrageBildpunktZuUniFormat(bufferedImageQuelle, positionXY, positionsnummer);
        BufferedImage bufferedImageZiel = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
        bufferedImageZiel.setRGB(1, 1, 0x00);
        uniFormatBild.uebertrageBildpunktVonUniFormat(bufferedImageZiel, positionXY, positionsnummer);
        bufferedImageZiel.getRaster().getPixel(1, 1, farbe);
        System.out.println("Von Uniformat: " + farbe[0]);
        assertEquals(0x000F, farbe[0]);
    }

    @Test
    public void testCheckAnzahlKanaeleParameterNull() {
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(1);
        try {
            uniFormatBild.checkAnzahlKanaele(null, BufferedImage.TYPE_BYTE_GRAY);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testCheckAnzahlKanaeleAnzahlRichtig() {
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(1);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(1);
        uniFormatBild.checkAnzahlKanaele(anzahlKanaele, BufferedImage.TYPE_BYTE_GRAY);
    }

    @Test
    public void testCheckAnzahlKanaeleAnzahlFalsch() {
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(2);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(5);
        try {
            uniFormatBild.checkAnzahlKanaele(anzahlKanaele, BufferedImage.TYPE_BYTE_GRAY);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Anzahl der Kanäle ist nicht passend.", e.getMessage());
        }
    }

    @Test
    public void testCheckBildtypParameterNull() {
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(1);
        try {
            uniFormatBild.checkBildtyp(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testCheckBildtypBildTypRichtig() {
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
        uniFormatBild.checkBildtyp(bufferedImage);
    }

    @Test
    public void testCheckBildtypBildTypFalsch() {
        UniFormatBildGrau uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        try {
            uniFormatBild.checkBildtyp(bufferedImage);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Der Bildtyp wird nicht unterstützt.", e.getMessage());
        }

    }

    private UniFormatBildGrau erzeugeUniFormatBild(int kanalanzahl) {
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(512);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(kanalanzahl);
        Bittiefe bittiefe = new Bittiefe(2);
        List<Eintrag> eintraege = new ArrayList<>();
        return new UniFormatBildGrau(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
    }
}
