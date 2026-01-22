package de.platen.steganograph.datentypen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class PositionsinhaltTest {

    @Test
    public void testKonstruktorParameterOk() {
        new Positionsinhalt(new AnzahlKanaele(Integer.valueOf(1)));
    }

    @Test
    public void testKonstruktorParameterNull() {
        try {
            new Positionsinhalt(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testSetzeWertOK() {
        Positionsinhalt positionsinhalt = new Positionsinhalt(new AnzahlKanaele(Integer.valueOf(1)));
        positionsinhalt.setzeWert(new Kanalnummer(1), 1);
    }

    @Test
    public void testSetzeWertKanalnummerNull() {
        Positionsinhalt positionsinhalt = new Positionsinhalt(new AnzahlKanaele(Integer.valueOf(1)));
        try {
            positionsinhalt.setzeWert(null, 1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testSetzeWertKanalnummerNichtEnthalten() {
        Positionsinhalt positionsinhalt = new Positionsinhalt(new AnzahlKanaele(Integer.valueOf(1)));
        try {
            positionsinhalt.setzeWert(new Kanalnummer(2), 1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Kanalnummer ist nicht in den Vorhandenen enthalten.", e.getMessage());
        }
    }

    @Test
    public void testHoleWertOK() {
        Positionsinhalt positionsinhalt = new Positionsinhalt(new AnzahlKanaele(Integer.valueOf(1)));
        positionsinhalt.setzeWert(new Kanalnummer(1), 1);
        positionsinhalt.holeWert(new Kanalnummer(1));
    }

    @Test
    public void testHoleWertKanalnummerNull() {
        Positionsinhalt positionsinhalt = new Positionsinhalt(new AnzahlKanaele(Integer.valueOf(1)));
        try {
            positionsinhalt.holeWert(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testHoleWertKanalnummerNichtEnthalten() {
        Positionsinhalt positionsinhalt = new Positionsinhalt(new AnzahlKanaele(Integer.valueOf(1)));
        try {
            positionsinhalt.holeWert(new Kanalnummer(2));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Kanalnummer ist nicht in den Vorhandenen enthalten.", e.getMessage());
        }
    }
}
