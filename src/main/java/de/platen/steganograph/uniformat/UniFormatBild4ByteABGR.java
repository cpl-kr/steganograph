package de.platen.steganograph.uniformat;

import java.awt.image.BufferedImage;
import java.util.List;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Kanalnummer;
import de.platen.steganograph.datentypen.PositionXY;
import de.platen.steganograph.datentypen.Positionsinhalt;
import de.platen.steganograph.datentypen.Positionsnummer;
import de.platen.steganograph.utils.Bildpunkt4ByteABGR;

public class UniFormatBild4ByteABGR extends UniFormatBild {

    private static final int KANAL_BLAU = 1;
    private static final int KANAL_GRUEN = 2;
    private static final int KANAL_ROT = 3;
    private static final int KANAL_ALPHA = 4;

    public UniFormatBild4ByteABGR(AnzahlPositionen anzahlPositionen, AnzahlKanaele anzahlKanaele, Bittiefe bittiefe,
            List<Eintrag> eintraege) {
        super(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
    }

    @Override
    public void uebertrageBildpunktZuUniFormat(BufferedImage bufferedImage, PositionXY positionXY,
            Positionsnummer positionsnummer) {
        checkParameter(bufferedImage, positionXY, positionsnummer);
        Positionsinhalt positionsinhalt = new Positionsinhalt(anzahlKanaele);
        int farbe = bufferedImage.getRGB(positionXY.getX().get(), positionXY.getY().get());
        Bildpunkt4ByteABGR bildpunkt = new Bildpunkt4ByteABGR(farbe);
        Kanalnummer kanalnummer = new Kanalnummer(KANAL_ROT);
        positionsinhalt.setzeWert(kanalnummer, bildpunkt.getRot());
        kanalnummer = new Kanalnummer(KANAL_GRUEN);
        positionsinhalt.setzeWert(kanalnummer, bildpunkt.getGruen());
        kanalnummer = new Kanalnummer(KANAL_BLAU);
        positionsinhalt.setzeWert(kanalnummer, bildpunkt.getBlau());
        kanalnummer = new Kanalnummer(KANAL_ALPHA);
        positionsinhalt.setzeWert(kanalnummer, bildpunkt.getAlpha());
        positionsinhalte.put(positionsnummer, positionsinhalt);
    }

    @Override
    public void uebertrageBildpunktVonUniFormat(BufferedImage bufferedImage, PositionXY positionXY,
            Positionsnummer positionsnummer) {
        checkParameter(bufferedImage, positionXY, positionsnummer);
        Positionsinhalt positionsinhalt = positionsinhalte.get(positionsnummer);
        int rgb = 0;
        rgb += Bildpunkt4ByteABGR.getBlau(positionsinhalt.holeWert(new Kanalnummer(KANAL_BLAU)));
        rgb += Bildpunkt4ByteABGR.getGruen(positionsinhalt.holeWert(new Kanalnummer(KANAL_GRUEN)));
        rgb += Bildpunkt4ByteABGR.getRot(positionsinhalt.holeWert(new Kanalnummer(KANAL_ROT)));
        rgb += Bildpunkt4ByteABGR.getAlpha(positionsinhalt.holeWert(new Kanalnummer(KANAL_ALPHA)));
        bufferedImage.setRGB(positionXY.getX().get(), positionXY.getY().get(), rgb);
    }

    @Override
    public void checkAnzahlKanaele(AnzahlKanaele anzahlKanaele) {
        if (anzahlKanaele == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
        if (anzahlKanaele.get() != 4) {
            throw new IllegalArgumentException(FEHLER_BILD_ANZAHL_KANAELE);
        }
    }

    @Override
    public void checkBildtyp(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
        if (bufferedImage.getType() != BufferedImage.TYPE_4BYTE_ABGR) {
            throw new IllegalArgumentException(FEHLER_BILDTYP);
        }
        if (bufferedImage.getAlphaRaster() == null) {
            throw new IllegalArgumentException(FEHLER_BILDTYP);
        }
    }

    private void checkParameter(BufferedImage bufferedImage, PositionXY positionXY, Positionsnummer positionsnummer) {
        if (bufferedImage == null || positionXY == null || positionsnummer == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
    }
}
