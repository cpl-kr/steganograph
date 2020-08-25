package de.platen.steganograph.datentypen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BreiteTest {

    @Test
    public void testParameterOk() {
        new Breite(Integer.valueOf(1));
    }

    @Test
    public void testParameterWerGleich0() {
        try {
            new Breite(Integer.valueOf(0));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter muss größer 0 sein.", e.getMessage());
        }
    }

    @Test
    public void testParameterWertKleiner0() {
        try {
            new Breite(Integer.valueOf(-1));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter muss größer 0 sein.", e.getMessage());
        }
    }
}
