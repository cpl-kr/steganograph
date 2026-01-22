package de.platen.steganograph.datentypen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class PositionXYTest {

    @Test
    public void testParameterOk() {
        new PositionXY(new X(Integer.valueOf(1)), new Y(Integer.valueOf(1)));
    }

    @Test
    public void testParameterXNull() {
        try {
            new PositionXY(null, new Y(Integer.valueOf(1)));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testParameterYNull() {
        try {
            new PositionXY(new X(Integer.valueOf(1)), null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }
}
