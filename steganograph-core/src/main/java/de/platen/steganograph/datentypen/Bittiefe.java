package de.platen.steganograph.datentypen;

import de.platen.objekt.Objekt;

public class Bittiefe extends Objekt<Integer> {

    private static final String PARAMETERFEHLER_BITTIEFE = "Parameter hat nicht einen der Werte 1, 2, 4 oder 8.";

    public Bittiefe(int wert) {
        super(wert);
        if (wert <= 0) {
            throw new IllegalArgumentException(PARAMETERFEHLER_BITTIEFE);
        }
        if (wert > 8) {
            throw new IllegalArgumentException(PARAMETERFEHLER_BITTIEFE);
        }
        if (8 % wert != 0) {
            throw new IllegalArgumentException(PARAMETERFEHLER_BITTIEFE);
        }
    }
}
