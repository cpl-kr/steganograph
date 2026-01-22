package de.platen.steganograph.datentypen;

import de.platen.objekt.Objekt;

public class Y extends Objekt<Integer> {

    private static final String FEHLER_PARAMETER_Y = "Parameter muss größer gleich 0 sein.";

    public Y(int wert) {
        super(wert);
        if (wert < 0) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_Y);
        }
    }
}
