package de.platen.steganograph.datentypen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BlockgroesseTest {

    @Test
    public void testParameterOk() {
        new Blockgroesse(Integer.valueOf(1));
    }

    @Test
    public void testParameterWertGleich0() {
        try {
            new Blockgroesse(Integer.valueOf(0));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter hat den Wert 0.", e.getMessage());
        }
    }

    @Test
    public void testParameterWertKleiner0() {
        try {
            new Blockgroesse(Integer.valueOf(-1));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter hat den Wert 0.", e.getMessage());
        }
    }
}
