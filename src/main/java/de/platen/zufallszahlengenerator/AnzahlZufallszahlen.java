package de.platen.zufallszahlengenerator;

import de.platen.objekt.Objekt;

public class AnzahlZufallszahlen extends Objekt<Integer> {

    private static final String FEHLER_PARAMETER_ANZAHL_ZUFALLSZAHLEN = "Parameter muss größer 0 sein.";

    public AnzahlZufallszahlen(int wert) {
        super(wert);
        if (wert <= 0) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_ANZAHL_ZUFALLSZAHLEN);
        }
    }
}
