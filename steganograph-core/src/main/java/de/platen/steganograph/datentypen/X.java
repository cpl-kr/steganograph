package de.platen.steganograph.datentypen;

import de.platen.objekt.Objekt;

public class X extends Objekt<Integer> {

    private static final String FEHLER_PARAMETER_X = "Parameter muss größer gleich 0 sein.";

    public X(int wert) {
        super(wert);
        if (wert < 0) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_X);
        }
    }
}
