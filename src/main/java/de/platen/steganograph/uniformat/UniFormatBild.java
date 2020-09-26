package de.platen.steganograph.uniformat;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Breite;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Hoehe;
import de.platen.steganograph.datentypen.PositionXY;
import de.platen.steganograph.datentypen.Positionsnummer;
import de.platen.steganograph.utils.Bildpunktposition;

public abstract class UniFormatBild extends UniFormat {

    protected static final String FEHLER_BILD_ANZAHL_KANAELE = "Die Anzahl der Kanäle ist nicht passend.";
    protected static final String FEHLER_BILDTYP = "Der Bildtyp wird nicht unterstützt.";

    protected final Map<Positionsnummer, PositionXY> nummerZuKoordinate = new HashMap<>();
    protected final Map<PositionXY, Positionsnummer> koordinateZuNummer = new HashMap<>();

    protected UniFormatBild(AnzahlPositionen anzahlPositionen, AnzahlKanaele anzahlKanaele, Bittiefe bittiefe,
            List<Eintrag> eintraege) {
        super(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
    }

    public void uebertrageBereichZuUniFormat(BufferedImage bufferedImage, PositionXY abPosition) {
        checkParameter(bufferedImage, abPosition);
        checkAnzahlKanaele(anzahlKanaele, bufferedImage.getType());
        checkBildtyp(bufferedImage);
        boolean ende = false;
        int position = 1;
        Positionsnummer positionsnummer = null;
        PositionXY positionXY = new PositionXY(abPosition.getX(), abPosition.getY());
        Bildpunktposition bildpunktposition = new Bildpunktposition(new Breite(bufferedImage.getWidth()),
                new Hoehe(bufferedImage.getHeight()));
        while (!ende) {
            positionsnummer = new Positionsnummer(position);
            nummerZuKoordinate.put(positionsnummer, positionXY);
            koordinateZuNummer.put(positionXY, positionsnummer);
            uebertrageBildpunktZuUniFormat(bufferedImage, positionXY, positionsnummer);
            positionXY = bildpunktposition.ermittleNaechstenBildpunkt(positionXY);
            if (positionXY != null) {
                position++;
                if (position > anzahlPositionen.get()) {
                    ende = true;
                }
            } else {
                ende = true;
            }
        }
    }

    public void uebertrageBereichVonUniFormat(BufferedImage bufferedImage, PositionXY abPosition) {
        checkParameter(bufferedImage, abPosition);
        checkAnzahlKanaele(anzahlKanaele, bufferedImage.getType());
        checkBildtyp(bufferedImage);
        boolean ende = false;
        Positionsnummer positionsnummer = null;
        PositionXY positionXY = new PositionXY(abPosition.getX(), abPosition.getY());
        Bildpunktposition bildpunktposition = new Bildpunktposition(new Breite(bufferedImage.getWidth()),
                new Hoehe(bufferedImage.getHeight()));
        while (!ende) {
            positionsnummer = koordinateZuNummer.get(positionXY);
            if (positionsnummer != null) {
                uebertrageBildpunktVonUniFormat(bufferedImage, positionXY, positionsnummer);
                positionXY = bildpunktposition.ermittleNaechstenBildpunkt(positionXY);
                if (positionXY == null) {
                    ende = true;
                }
            } else {
                ende = true;
            }
        }
    }

    protected abstract void uebertrageBildpunktZuUniFormat(BufferedImage bufferedImage, PositionXY positionXY,
            Positionsnummer positionsnummer);

    protected abstract void uebertrageBildpunktVonUniFormat(BufferedImage bufferedImage, PositionXY positionXY,
            Positionsnummer positionsnummer);

    protected abstract void checkAnzahlKanaele(AnzahlKanaele anzahlKanaele, int bildtyp);

    protected abstract void checkBildtyp(BufferedImage bufferedImage);

    private static void checkParameter(BufferedImage bufferedImage, PositionXY abPosition) {
        if ((bufferedImage == null) || (abPosition == null)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
    }
}
