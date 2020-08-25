package de.platen.zufallszahlengenerator;

import de.platen.objekt.Objekt;

public class BereichBis extends Objekt<Integer> {

    private static final String FEHLER_PARAMETER_BEREICH_BIS = "Parameter muss größer 0 sein.";

    public BereichBis(int wert) {
        super(wert);
        if (wert <= 0) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_BEREICH_BIS);
        }
    }
}
