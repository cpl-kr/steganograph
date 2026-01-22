package de.platen.steganograph.datentypen;

import de.platen.objekt.Objekt;

public class AnzahlNutzdaten extends Objekt<Integer> {

    private static final String PARAMETERFEHLER_NUTZDATEN = "Parameter muss größer 0 sein.";

    public AnzahlNutzdaten(int wert) {
        super(wert);
        if (wert <= 0L) {
            throw new IllegalArgumentException(PARAMETERFEHLER_NUTZDATEN);
        }
    }
}
