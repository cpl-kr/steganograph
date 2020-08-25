package de.platen.zufallszahlengenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BereichVonTest {

    @Test
    public void testParameterOk() {
        new BereichVon(Integer.valueOf(1));
    }

    @Test
    public void testParameterWertKleiner0() {
        try {
            new BereichVon(Integer.valueOf(-1));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter muss größer gleich 0 sein.", e.getMessage());
        }
    }
}
