package de.platen.steganograph.uniformat;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final Map<Integer, Map<Integer, Integer>> BILDTYP_ZU_KANAL_ABBILDUMG = new HashMap<>();
    private static final Map<Integer, Integer> TYPE_4BYTE_ABGR = new HashMap<>();
    private static final Map<Integer, Integer> TYPE_INT_ARGB = new HashMap<>();

    static {
        TYPE_4BYTE_ABGR.put(KANAL_BLAU, 1);
        TYPE_4BYTE_ABGR.put(KANAL_GRUEN, 2);
        TYPE_4BYTE_ABGR.put(KANAL_ROT, 3);
        TYPE_4BYTE_ABGR.put(KANAL_ALPHA, 4);
        TYPE_INT_ARGB.put(KANAL_BLAU, 3);
        TYPE_INT_ARGB.put(KANAL_GRUEN, 2);
        TYPE_INT_ARGB.put(KANAL_ROT, 1);
        TYPE_INT_ARGB.put(KANAL_ALPHA, 4);
        BILDTYP_ZU_KANAL_ABBILDUMG.put(BufferedImage.TYPE_4BYTE_ABGR, TYPE_4BYTE_ABGR);
        BILDTYP_ZU_KANAL_ABBILDUMG.put(BufferedImage.TYPE_INT_ARGB, TYPE_INT_ARGB);
    }

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
        int nummer = BILDTYP_ZU_KANAL_ABBILDUMG.get(bufferedImage.getType()).get(KANAL_ROT);
        Kanalnummer kanalnummer = new Kanalnummer(nummer);
        positionsinhalt.setzeWert(kanalnummer, bildpunkt.getByte3());
        if (anzahlKanaele.get() > 1) {
            nummer = BILDTYP_ZU_KANAL_ABBILDUMG.get(bufferedImage.getType()).get(KANAL_GRUEN);
            kanalnummer = new Kanalnummer(nummer);
            positionsinhalt.setzeWert(kanalnummer, bildpunkt.getByte2());
        }
        if (anzahlKanaele.get() > 2) {
            nummer = BILDTYP_ZU_KANAL_ABBILDUMG.get(bufferedImage.getType()).get(KANAL_BLAU);
            kanalnummer = new Kanalnummer(nummer);
            positionsinhalt.setzeWert(kanalnummer, bildpunkt.getByte1());
        }
        if (anzahlKanaele.get() > 3) {
            nummer = BILDTYP_ZU_KANAL_ABBILDUMG.get(bufferedImage.getType()).get(KANAL_ALPHA);
            kanalnummer = new Kanalnummer(nummer);
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
        int nummer = BILDTYP_ZU_KANAL_ABBILDUMG.get(bufferedImage.getType()).get(KANAL_ROT);
        rgb += BildpunktInteger.getByte3(positionsinhalt.holeWert(new Kanalnummer(nummer)));
        if (anzahlKanaele.get() > 1) {
            nummer = BILDTYP_ZU_KANAL_ABBILDUMG.get(bufferedImage.getType()).get(KANAL_GRUEN);
            rgb += BildpunktInteger.getByte2(positionsinhalt.holeWert(new Kanalnummer(nummer)));
        }
        if (anzahlKanaele.get() > 2) {
            nummer = BILDTYP_ZU_KANAL_ABBILDUMG.get(bufferedImage.getType()).get(KANAL_BLAU);
            rgb += BildpunktInteger.getByte1(positionsinhalt.holeWert(new Kanalnummer(nummer)));
        }
        if (anzahlKanaele.get() > 3) {
            nummer = BILDTYP_ZU_KANAL_ABBILDUMG.get(bufferedImage.getType()).get(KANAL_ALPHA);
            rgb += BildpunktInteger.getByte4(positionsinhalt.holeWert(new Kanalnummer(nummer)));
        }
        bufferedImage.setRGB(positionXY.getX().get(), positionXY.getY().get(), rgb);
    }

    @Override
    public void checkAnzahlKanaele(AnzahlKanaele anzahlKanaele, int bildtyp) {
        if (anzahlKanaele == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
        if ((bildtyp == BufferedImage.TYPE_4BYTE_ABGR) || (bildtyp == BufferedImage.TYPE_INT_ARGB)) {
            if (anzahlKanaele.get() > 4) {
                throw new IllegalArgumentException(FEHLER_BILD_ANZAHL_KANAELE);
            }
        }
    }

    @Override
    public void checkBildtyp(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
        if (!((bufferedImage.getType() == BufferedImage.TYPE_4BYTE_ABGR)
                || (bufferedImage.getType() == BufferedImage.TYPE_INT_ARGB))) {
            throw new IllegalArgumentException(FEHLER_BILDTYP);
        }
    }

    private void checkParameter(BufferedImage bufferedImage, PositionXY positionXY, Positionsnummer positionsnummer) {
        if (bufferedImage == null || positionXY == null || positionsnummer == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
    }
}
