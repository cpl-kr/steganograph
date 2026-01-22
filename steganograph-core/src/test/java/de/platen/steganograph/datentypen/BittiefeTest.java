package de.platen.steganograph.datentypen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BittiefeTest {

    @Test
    public void testParameterOk() {
        new Bittiefe(Integer.valueOf(1));
    }

    @Test
    public void testParameterWertGleich0() {
        try {
            new Bittiefe(Integer.valueOf(0));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter hat nicht einen der Werte 1, 2, 4 oder 8.", e.getMessage());
        }
    }

    @Test
    public void testParameterWertKleiner0() {
        try {
            new Bittiefe(Integer.valueOf(-1));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter hat nicht einen der Werte 1, 2, 4 oder 8.", e.getMessage());
        }
    }

    @Test
    public void testParameterWertGroesser8() {
        try {
            new Bittiefe(Integer.valueOf(9));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter hat nicht einen der Werte 1, 2, 4 oder 8.", e.getMessage());
        }
    }

    @Test
    public void testParameterWertModuloUngleich0() {
        try {
            new Bittiefe(Integer.valueOf(3));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter hat nicht einen der Werte 1, 2, 4 oder 8.", e.getMessage());
        }
    }
}
