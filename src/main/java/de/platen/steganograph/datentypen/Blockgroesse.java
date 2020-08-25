package de.platen.steganograph.datentypen;

import de.platen.objekt.Objekt;

public class Blockgroesse extends Objekt<Integer> {

    private static final String PARAMETERFEHLER_BLOCKGROESSE = "Parameter hat den Wert 0.";

    public Blockgroesse(int wert) {
        super(wert);
        if (wert <= 0) {
            throw new IllegalArgumentException(PARAMETERFEHLER_BLOCKGROESSE);
        }
    }
}
