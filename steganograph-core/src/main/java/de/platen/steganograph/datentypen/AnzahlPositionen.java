package de.platen.steganograph.datentypen;

import de.platen.objekt.Objekt;

public class AnzahlPositionen extends Objekt<Integer> {

    private static final String FEHLER_PARAMETER_ANZAHL_POSITIONEN = "Parameter muss größer 0 sein.";

    public AnzahlPositionen(int wert) {
        super(wert);
        if (wert < 1) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_ANZAHL_POSITIONEN);
        }
    }
}
