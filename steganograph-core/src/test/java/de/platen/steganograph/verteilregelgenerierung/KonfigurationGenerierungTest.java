package de.platen.steganograph.verteilregelgenerierung;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Blockgroesse;

public class KonfigurationGenerierungTest {

    private static final String WERTFEHLER = "Die Nutzdaten passen nicht in den Block.";
    private static final String PARAMETER_NULL = "Einer der Parameter ist null.";

    @Test
    public void testParameterOk() {
        Blockgroesse blockgroesse = new Blockgroesse(Integer.valueOf(100));
        AnzahlNutzdaten nutzdaten = new AnzahlNutzdaten(Integer.valueOf(10));
        Bittiefe bittiefe = new Bittiefe(Integer.valueOf(2));
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(Integer.valueOf(4));
        new KonfigurationVerteilregeln(blockgroesse, nutzdaten, bittiefe, anzahlKanaele);
    }

    @Test
    public void testParameterNutzdatenZuGross() {
        Blockgroesse blockgroesse = new Blockgroesse(Integer.valueOf(100));
        AnzahlNutzdaten nutzdaten = new AnzahlNutzdaten(Integer.valueOf(1000));
        Bittiefe bittiefe = new Bittiefe(Integer.valueOf(2));
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(Integer.valueOf(4));
        try {
            new KonfigurationVerteilregeln(blockgroesse, nutzdaten, bittiefe, anzahlKanaele);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(WERTFEHLER, e.getMessage());
        }
    }

    @Test
    public void testParameterBlockgroesseNull() {
        Blockgroesse blockgroesse = null;
        AnzahlNutzdaten nutzdaten = new AnzahlNutzdaten(Integer.valueOf(10));
        Bittiefe bittiefe = new Bittiefe(Integer.valueOf(2));
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(Integer.valueOf(4));
        try {
            new KonfigurationVerteilregeln(blockgroesse, nutzdaten, bittiefe, anzahlKanaele);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(PARAMETER_NULL, e.getMessage());
        }
    }

    @Test
    public void testParameterNutzdatenNull() {
        Blockgroesse blockgroesse = new Blockgroesse(Integer.valueOf(100));
        AnzahlNutzdaten nutzdaten = null;
        Bittiefe bittiefe = new Bittiefe(Integer.valueOf(2));
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(Integer.valueOf(4));
        try {
            new KonfigurationVerteilregeln(blockgroesse, nutzdaten, bittiefe, anzahlKanaele);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(PARAMETER_NULL, e.getMessage());
        }
    }

    @Test
    public void testParameterBittiefeNull() {
        Blockgroesse blockgroesse = new Blockgroesse(Integer.valueOf(100));
        AnzahlNutzdaten nutzdaten = new AnzahlNutzdaten(Integer.valueOf(10));
        Bittiefe bittiefe = null;
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(Integer.valueOf(4));
        try {
            new KonfigurationVerteilregeln(blockgroesse, nutzdaten, bittiefe, anzahlKanaele);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(PARAMETER_NULL, e.getMessage());
        }
    }

    @Test
    public void testParameterAnzahlKanaeleNull() {
        Blockgroesse blockgroesse = new Blockgroesse(Integer.valueOf(100));
        AnzahlNutzdaten nutzdaten = new AnzahlNutzdaten(Integer.valueOf(10));
        Bittiefe bittiefe = new Bittiefe(Integer.valueOf(2));
        AnzahlKanaele anzahlKanaele = null;
        try {
            new KonfigurationVerteilregeln(blockgroesse, nutzdaten, bittiefe, anzahlKanaele);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(PARAMETER_NULL, e.getMessage());
        }
    }
}
