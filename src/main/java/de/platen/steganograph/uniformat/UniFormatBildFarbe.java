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
import de.platen.steganograph.utils.BildpunktInteger;

public class UniFormatBildFarbe extends UniFormatBild {

    private static final int KANAL_BLAU = 1;
    private static final int KANAL_GRUEN = 2;
    private static final int KANAL_ROT = 3;
    private static final int KANAL_ALPHA = 4;

    public UniFormatBildFarbe(AnzahlPositionen anzahlPositionen, AnzahlKanaele anzahlKanaele, Bittiefe bittiefe,
            List<Eintrag> eintraege) {
        super(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
    }

    @Override
    public void uebertrageBildpunktZuUniFormat(BufferedImage bufferedImage, PositionXY positionXY,
            Positionsnummer positionsnummer) {
        checkParameter(bufferedImage, positionXY, positionsnummer);
        Positionsinhalt positionsinhalt = new Positionsinhalt(anzahlKanaele);
        int farbe = bufferedImage.getRGB(positionXY.getX().get(), positionXY.getY().get());
        BildpunktInteger bildpunkt = new BildpunktInteger(farbe);
        Kanalnummer kanalnummer = new Kanalnummer(KANAL_ROT);
        positionsinhalt.setzeWert(kanalnummer, bildpunkt.getByte3());
        if (anzahlKanaele.get() > 1) {
            kanalnummer = new Kanalnummer(KANAL_GRUEN);
            positionsinhalt.setzeWert(kanalnummer, bildpunkt.getByte2());
        }
        if (anzahlKanaele.get() > 2) {
            kanalnummer = new Kanalnummer(KANAL_BLAU);
            positionsinhalt.setzeWert(kanalnummer, bildpunkt.getByte1());
        }
        if (anzahlKanaele.get() > 3) {
            kanalnummer = new Kanalnummer(KANAL_ALPHA);
            positionsinhalt.setzeWert(kanalnummer, bildpunkt.getByte4());
        }
        positionsinhalte.put(positionsnummer, positionsinhalt);
    }

    @Override
    public void uebertrageBildpunktVonUniFormat(BufferedImage bufferedImage, PositionXY positionXY,
            Positionsnummer positionsnummer) {
        checkParameter(bufferedImage, positionXY, positionsnummer);
        Positionsinhalt positionsinhalt = positionsinhalte.get(positionsnummer);
        int rgb = 0;
        rgb += BildpunktInteger.getByte3(positionsinhalt.holeWert(new Kanalnummer(KANAL_ROT)));
        if (anzahlKanaele.get() > 1) {
            rgb += BildpunktInteger.getByte2(positionsinhalt.holeWert(new Kanalnummer(KANAL_GRUEN)));
        }
        if (anzahlKanaele.get() > 2) {
            rgb += BildpunktInteger.getByte1(positionsinhalt.holeWert(new Kanalnummer(KANAL_BLAU)));
        }
        if (anzahlKanaele.get() > 3) {
            rgb += BildpunktInteger.getByte4(positionsinhalt.holeWert(new Kanalnummer(KANAL_ALPHA)));
        }
        bufferedImage.setRGB(positionXY.getX().get(), positionXY.getY().get(), rgb);
    }

    @Override
    public void checkAnzahlKanaele(AnzahlKanaele anzahlKanaele) {
        if (anzahlKanaele == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
        if (anzahlKanaele.get() > 4) {
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
