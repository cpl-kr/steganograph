package de.platen.steganograph.datentypen;

import de.platen.objekt.Objekt;

public class AnzahlKanaele extends Objekt<Integer> {

    private static final String FEHLER_PARAMETER_ANZAHL_KANAELE = "Parameter muss zwischen 1 und 255 liegen.";

    public AnzahlKanaele(int wert) {
        super(wert);
        if (wert <= 0) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_ANZAHL_KANAELE);
        }
        if (wert > 255) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_ANZAHL_KANAELE);
        }
    }
}
