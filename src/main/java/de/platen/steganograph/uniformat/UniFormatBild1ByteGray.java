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

public class UniFormatBild1ByteGray extends UniFormatBild {

    public UniFormatBild1ByteGray(AnzahlPositionen anzahlPositionen, AnzahlKanaele anzahlKanaele, Bittiefe bittiefe,
            List<Eintrag> eintraege) {
        super(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
    }

    @Override
    public void uebertrageBildpunktZuUniFormat(BufferedImage bufferedImage, PositionXY positionXY,
            Positionsnummer positionsnummer) {
        checkParameter(bufferedImage, positionXY, positionsnummer);
        Positionsinhalt positionsinhalt = new Positionsinhalt(anzahlKanaele);
        int farbe[] = new int[1];
        bufferedImage.getRaster().getPixel(positionXY.getX().get(), positionXY.getY().get(), farbe);
        System.out.println("Zu Uniformat: " + farbe[0]);
        BildpunktInteger bildpunktInteger = new BildpunktInteger(farbe[0]);
        Kanalnummer kanalnummer = new Kanalnummer(1);
        positionsinhalt.setzeWert(kanalnummer, bildpunktInteger.getByte1());
        positionsinhalte.put(positionsnummer, positionsinhalt);
    }

    @Override
    public void uebertrageBildpunktVonUniFormat(BufferedImage bufferedImage, PositionXY positionXY,
            Positionsnummer positionsnummer) {
        checkParameter(bufferedImage, positionXY, positionsnummer);
        Positionsinhalt positionsinhalt = positionsinhalte.get(positionsnummer);
        int wert = BildpunktInteger.getByte1(positionsinhalt.holeWert(new Kanalnummer(1)));
        int farbe[] = new int[1];
        farbe[0] = wert;
        bufferedImage.getRaster().setPixel(positionXY.getX().get(), positionXY.getY().get(), farbe);
    }

    @Override
    public void checkAnzahlKanaele(AnzahlKanaele anzahlKanaele) {
        if (anzahlKanaele == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
        if (anzahlKanaele.get() > 1) {
            throw new IllegalArgumentException(FEHLER_BILD_ANZAHL_KANAELE);
        }
    }

    @Override
    public void checkBildtyp(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
        if (bufferedImage.getType() != BufferedImage.TYPE_BYTE_GRAY) {
            throw new IllegalArgumentException(FEHLER_BILDTYP);
        }
    }

    private void checkParameter(BufferedImage bufferedImage, PositionXY positionXY, Positionsnummer positionsnummer) {
        if (bufferedImage == null || positionXY == null || positionsnummer == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
    }
}
