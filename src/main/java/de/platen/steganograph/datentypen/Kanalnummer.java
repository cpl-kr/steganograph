package de.platen.steganograph.datentypen;

import de.platen.objekt.Objekt;

public class Kanalnummer extends Objekt<Integer> {

    private static final String FEHLER_PARAMETER_KANALNUMMER = "Parameter muss größer 0 sein.";

    public Kanalnummer(int wert) {
        super(wert);
        if (wert < 1) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_KANALNUMMER);
        }
    }
}
