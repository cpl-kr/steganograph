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
import de.platen.steganograph.datentypen.Kanalnummer;
import de.platen.steganograph.datentypen.PositionXY;
import de.platen.steganograph.datentypen.Positionsinhalt;
import de.platen.steganograph.datentypen.Positionsnummer;
import de.platen.steganograph.datentypen.X;
import de.platen.steganograph.datentypen.Y;
import de.platen.steganograph.utils.BildpunktInteger;

public class UniFormatBildTest {

    @Test
    public void testUebertrageBereichZuUniFormatParameterNullBufferedImage() {
        UniFormatBild uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = null;
        PositionXY abPosition = new PositionXY(new X(0), new Y(0));
        try {
            uniFormatBild.uebertrageBereichZuUniFormat(bufferedImage, abPosition);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBereichZuUniFormatParameterNullPositionXY() {
        UniFormatBild uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = new BufferedImage(10, 20, BufferedImage.TYPE_4BYTE_ABGR);
        PositionXY abPosition = null;
        try {
            uniFormatBild.uebertrageBereichZuUniFormat(bufferedImage, abPosition);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBereichVonUniFormatParameterNullBufferedImage() {
        UniFormatBild uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = null;
        PositionXY abPosition = new PositionXY(new X(0), new Y(0));
        try {
            uniFormatBild.uebertrageBereichVonUniFormat(bufferedImage, abPosition);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBereichVonUniFormatParameterNullPositionXY() {
        UniFormatBild uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImage = new BufferedImage(10, 20, BufferedImage.TYPE_4BYTE_ABGR);
        PositionXY abPosition = null;
        try {
            uniFormatBild.uebertrageBereichVonUniFormat(bufferedImage, abPosition);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBereichZuUndVonUniFormat() {
        UniFormatBild uniFormatBild = erzeugeUniFormatBild(4);
        BufferedImage bufferedImageZu = new BufferedImage(10, 20, BufferedImage.TYPE_4BYTE_ABGR);
        PositionXY abPosition = new PositionXY(new X(0), new Y(0));
        int rgb = 0xFFFFFFFF;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                bufferedImageZu.setRGB(x, y, rgb);
            }
        }
        uniFormatBild.uebertrageBereichZuUniFormat(bufferedImageZu, abPosition);
        BufferedImage bufferedImageVon = new BufferedImage(10, 20, BufferedImage.TYPE_4BYTE_ABGR);
        uniFormatBild.uebertrageBereichVonUniFormat(bufferedImageVon, abPosition);
        int rgbZu = 0;
        int rgbVon = 0;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                rgbZu = bufferedImageZu.getRGB(x, y);
                rgbVon = bufferedImageVon.getRGB(x, y);
                assertEquals(rgbZu, rgbVon);
            }
        }
    }

    private UniFormatBild erzeugeUniFormatBild(int kanalanzahl) {
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(512);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(kanalanzahl);
        Bittiefe bittiefe = new Bittiefe(2);
        List<Eintrag> eintraege = new ArrayList<>();
        return new UniFormatBildZumTest(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
    }

    private class UniFormatBildZumTest extends UniFormatBild {

        private static final int KANAL_BLAU = 1;
        private static final int KANAL_GRUEN = 2;
        private static final int KANAL_ROT = 3;
        private static final int KANAL_ALPHA = 4;

        public UniFormatBildZumTest(AnzahlPositionen anzahlPositionen, AnzahlKanaele anzahlKanaele, Bittiefe bittiefe,
                List<Eintrag> eintraege) {
            super(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
        }

        @Override
        public void uebertrageBildpunktZuUniFormat(BufferedImage bufferedImage, PositionXY positionXY,
                Positionsnummer positionsnummer) {
            Positionsinhalt positionsinhalt = new Positionsinhalt(anzahlKanaele);
            int farbe = bufferedImage.getRGB(positionXY.getX().get(), positionXY.getY().get());
            BildpunktInteger bildpunkt = new BildpunktInteger(farbe);
            Kanalnummer kanalnummer = new Kanalnummer(KANAL_ROT);
            positionsinhalt.setzeWert(kanalnummer, bildpunkt.getByte3());
            kanalnummer = new Kanalnummer(KANAL_GRUEN);
            positionsinhalt.setzeWert(kanalnummer, bildpunkt.getByte2());
            kanalnummer = new Kanalnummer(KANAL_BLAU);
            positionsinhalt.setzeWert(kanalnummer, bildpunkt.getByte1());
            kanalnummer = new Kanalnummer(KANAL_ALPHA);
            positionsinhalt.setzeWert(kanalnummer, bildpunkt.getByte4());
            positionsinhalte.put(positionsnummer, positionsinhalt);
        }

        @Override
        public void uebertrageBildpunktVonUniFormat(BufferedImage bufferedImage, PositionXY positionXY,
                Positionsnummer positionsnummer) {
            Positionsinhalt positionsinhalt = positionsinhalte.get(positionsnummer);
            int rgb = 0;
            rgb += positionsinhalt.holeWert(new Kanalnummer(KANAL_ROT));
            rgb += positionsinhalt.holeWert(new Kanalnummer(KANAL_GRUEN)) << 8;
            rgb += positionsinhalt.holeWert(new Kanalnummer(KANAL_BLAU)) << 16;
            rgb += positionsinhalt.holeWert(new Kanalnummer(KANAL_ALPHA)) << 24;
            bufferedImage.setRGB(positionXY.getX().get(), positionXY.getY().get(), rgb);
        }

        @Override
        public void checkAnzahlKanaele(AnzahlKanaele anzahlKanaele) {
        }

        @Override
        public void checkBildtyp(BufferedImage bufferedImage) {
        }
    }
}
