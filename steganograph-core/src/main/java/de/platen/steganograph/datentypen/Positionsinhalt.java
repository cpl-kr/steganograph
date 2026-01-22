package de.platen.steganograph.datentypen;

import java.util.HashMap;
import java.util.Map;

public class Positionsinhalt {

    private static final String FEHLER_PARAMETER_NULL = "Der Parameter ist null.";
    private static final String FEHLER_PARAMETER_INHALT = "Die Kanalnummer ist nicht in den Vorhandenen enthalten.";

    private final AnzahlKanaele anzahlKanaele;
    private final Map<Kanalnummer, Integer> werte = new HashMap<>();

    public Positionsinhalt(AnzahlKanaele anzahlKanaele) {
        if (anzahlKanaele == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        this.anzahlKanaele = anzahlKanaele;
    }

    public void setzeWert(Kanalnummer kanalnummer, int wert) {
        checkKanalnummer(kanalnummer);
        werte.put(kanalnummer, wert);
    }

    public int holeWert(Kanalnummer kanalnummer) {
        checkKanalnummer(kanalnummer);
        return werte.get(kanalnummer);
    }

    private void checkKanalnummer(Kanalnummer kanalnummer) {
        if (kanalnummer == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        if ((kanalnummer.get() < 1) || (kanalnummer.get() > anzahlKanaele.get())) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_INHALT);
        }
    }
}
