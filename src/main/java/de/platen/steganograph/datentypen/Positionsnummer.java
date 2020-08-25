package de.platen.steganograph.datentypen;

import de.platen.objekt.Objekt;

public class Positionsnummer extends Objekt<Integer> {

    private static final String FEHLER_PARAMETER_POSITIONSNUMMER = "Parameter muss größer 0 sein.";

    public Positionsnummer(int wert) {
        super(wert);
        if (wert < 1) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_POSITIONSNUMMER);
        }
    }
}
