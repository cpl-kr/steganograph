package de.platen.steganograph.datentypen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class XTest {

    @Test
    public void testParameterOk() {
        new X(Integer.valueOf(0));
    }

    @Test
    public void testParameterWertKleiner0() {
        try {
            new X(Integer.valueOf(-1));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter muss größer gleich 0 sein.", e.getMessage());
        }
    }
}
