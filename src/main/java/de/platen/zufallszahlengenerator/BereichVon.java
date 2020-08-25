package de.platen.zufallszahlengenerator;

import de.platen.objekt.Objekt;

public class BereichVon extends Objekt<Integer> {

    private static final String FEHLER_PARAMETER_BEREICH_VON = "Parameter muss größer gleich 0 sein.";

    public BereichVon(int wert) {
        super(wert);
        if (wert < 0) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_BEREICH_VON);
        }
    }
}
