package de.platen.steganograph.verteilregelgenerierung;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Blockgroesse;

public class KonfigurationVerteilregeln {

    private static final String WERTFEHLER = "Die Nutzdaten passen nicht in den Block.";
    private static final String PARAMETER_NULL = "Einer der Parameter ist null.";

    private final Blockgroesse blockgroesse;
    private final AnzahlNutzdaten nutzdaten;
    private final Bittiefe bittiefe;
    private final AnzahlKanaele anzahlKanaele;

    public KonfigurationVerteilregeln(final Blockgroesse blockgroesse, final AnzahlNutzdaten nutzdaten,
            final Bittiefe bittiefe, final AnzahlKanaele anzahlKanaele) {
        this.blockgroesse = blockgroesse;
        this.nutzdaten = nutzdaten;
        this.bittiefe = bittiefe;
        this.anzahlKanaele = anzahlKanaele;
        checkParameter();
    }

    public Blockgroesse getBlockgroesse() {
        return blockgroesse;
    }

    public AnzahlNutzdaten getNutzdaten() {
        return nutzdaten;
    }

    public Bittiefe getBittiefe() {
        return bittiefe;
    }

    public AnzahlKanaele getAnzahlKanaele() {
        return anzahlKanaele;
    }

    private void checkParameter() {
        if (blockgroesse == null || nutzdaten == null || bittiefe == null || anzahlKanaele == null) {
            throw new IllegalArgumentException(PARAMETER_NULL);
        }
        long maxBitsBlock = blockgroesse.get() * bittiefe.get() * anzahlKanaele.get();
        long maxBitsNutzdaten = nutzdaten.get() * 8;
        if (maxBitsNutzdaten > maxBitsBlock) {
            throw new IllegalArgumentException(WERTFEHLER);
        }
    }
}
