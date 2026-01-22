package de.platen.steganograph.datentypen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class AnzahlKanaeleTest {

    @Test
    public void testParameterOk() {
        new AnzahlKanaele(Integer.valueOf(1));
    }

    @Test
    public void testParameterWertGleich0() {
        try {
            new AnzahlKanaele(Integer.valueOf(0));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter muss zwischen 1 und 255 liegen.", e.getMessage());
        }
    }

    @Test
    public void testParameterWertKleiner0() {
        try {
            new AnzahlKanaele(Integer.valueOf(-1));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter muss zwischen 1 und 255 liegen.", e.getMessage());
        }
    }

    @Test
    public void testParameterWertGroesser255() {
        try {
            new AnzahlKanaele(Integer.valueOf(256));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter muss zwischen 1 und 255 liegen.", e.getMessage());
        }
    }
}
