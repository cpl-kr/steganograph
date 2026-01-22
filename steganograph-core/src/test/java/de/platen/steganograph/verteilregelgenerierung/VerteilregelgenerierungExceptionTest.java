package de.platen.steganograph.verteilregelgenerierung;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VerteilregelgenerierungExceptionTest {

    @Test
    public void testNachricht() {
        String nachricht = "nachricht";
        VerteilregelgenerierungException exception = new VerteilregelgenerierungException(nachricht);
        assertEquals(nachricht, exception.getMessage());
    }
}
