package de.platen.steganograph.datentypen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class EintragTest {

    @Test
    public void testParameterOk() {
        new Eintrag(new Positionsnummer(Integer.valueOf(1)), new Kanalnummer(Integer.valueOf(1)));
    }

    @Test
    public void testParameterPositionsnummerNull() {
        try {
            new Eintrag(null, new Kanalnummer(Integer.valueOf(1)));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testParameterKanalnummerNull() {
        try {
            new Eintrag(new Positionsnummer(Integer.valueOf(1)), null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }
}
