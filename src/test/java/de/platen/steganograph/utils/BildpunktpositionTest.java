package de.platen.steganograph.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.platen.steganograph.datentypen.Breite;
import de.platen.steganograph.datentypen.Hoehe;
import de.platen.steganograph.datentypen.PositionXY;
import de.platen.steganograph.datentypen.X;
import de.platen.steganograph.datentypen.Y;

public class BildpunktpositionTest {

    private static final String PARAMETERFEHLER = "Einer der Parameter ist null.";
    private static final int BREITE = 10;
    private static final int HOEHE = 20;

    private Bildpunktposition bildpunktposition;

    @Before
    public void init() {
        bildpunktposition = new Bildpunktposition(new Breite(BREITE), new Hoehe(HOEHE));
    }

    @Test
    public void testParameterBreiteNull() {
        try {
            new Bildpunktposition(null, new Hoehe(1));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(PARAMETERFEHLER, e.getMessage());
        }
    }

    @Test
    public void testParameterHoeheNull() {
        try {
            new Bildpunktposition(new Breite(1), null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(PARAMETERFEHLER, e.getMessage());
        }
    }

    @Test
    public void testNeuesXVonAnfang() {
        PositionXY positionXY = new PositionXY(new X(0), new Y(0));
        PositionXY neuePosition = bildpunktposition.ermittleNaechstenBildpunkt(positionXY);
        assertNotNull(neuePosition);
        assertEquals(1, neuePosition.getX().get().intValue());
        assertEquals(0, neuePosition.getY().get().intValue());
    }

    @Test
    public void testNeuesXVonInnerhalb() {
        PositionXY positionXY = new PositionXY(new X(8), new Y(18));
        PositionXY neuePosition = bildpunktposition.ermittleNaechstenBildpunkt(positionXY);
        assertNotNull(neuePosition);
        assertEquals(9, neuePosition.getX().get().intValue());
        assertEquals(18, neuePosition.getY().get().intValue());
    }

    @Test
    public void testNeuesY() {
        PositionXY positionXY = new PositionXY(new X(9), new Y(18));
        PositionXY neuePosition = bildpunktposition.ermittleNaechstenBildpunkt(positionXY);
        assertNotNull(neuePosition);
        assertEquals(0, neuePosition.getX().get().intValue());
        assertEquals(19, neuePosition.getY().get().intValue());
    }

    @Test
    public void testNullXGleichBreite() {
        PositionXY positionXY = new PositionXY(new X(10), new Y(18));
        PositionXY neuePosition = bildpunktposition.ermittleNaechstenBildpunkt(positionXY);
        assertNull(neuePosition);
    }

    @Test
    public void testNullYGleichHoehe() {
        PositionXY positionXY = new PositionXY(new X(8), new Y(20));
        PositionXY neuePosition = bildpunktposition.ermittleNaechstenBildpunkt(positionXY);
        assertNull(neuePosition);
    }

    @Test
    public void testNullGrenzpunkt() {
        PositionXY positionXY = new PositionXY(new X(9), new Y(19));
        PositionXY neuePosition = bildpunktposition.ermittleNaechstenBildpunkt(positionXY);
        assertNull(neuePosition);
    }

    @Test
    public void testAbstandKleiner0() {
        PositionXY positionXY = new PositionXY(new X(8), new Y(20));
        PositionXY neuePosition = bildpunktposition.ermittleNaechstenBildpunkt(positionXY, -1);
        assertNull(neuePosition);
    }

    @Test
    public void testAbstandGleich0() {
        PositionXY positionXY = new PositionXY(new X(5), new Y(10));
        PositionXY neuePosition = bildpunktposition.ermittleNaechstenBildpunkt(positionXY, 0);
        assertEquals(positionXY, neuePosition);
    }

    @Test
    public void testAbstandInnerhalbBild() {
        PositionXY positionXY = new PositionXY(new X(5), new Y(10));
        PositionXY neuePosition = bildpunktposition.ermittleNaechstenBildpunkt(positionXY, 10);
        PositionXY erwartetePosition = new PositionXY(new X(5), new Y(11));
        assertEquals(erwartetePosition, neuePosition);
    }

    @Test
    public void testAbstandAusserhalbBild() {
        PositionXY positionXY = new PositionXY(new X(5), new Y(19));
        PositionXY neuePosition = bildpunktposition.ermittleNaechstenBildpunkt(positionXY, 10);
        assertNull(neuePosition);
    }
}
