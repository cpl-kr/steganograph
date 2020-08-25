package de.platen.zufallszahlengenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Test;

public class ZufallszahlengeneratorTest {

    @Test
    public void testErzeugeZufallszahlenmenge1() {
        for (int i = 0; i < 100; i++) {
            Set<Integer> zufallszahlen = Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(500),
                    new BereichVon(10), new BereichBis(5001));
            assertEquals(500, zufallszahlen.size());
            for (Integer zufallszahl : zufallszahlen) {
                if (!((zufallszahl >= 10) && (zufallszahl < 5001))) {
                    System.out
                            .println("Bei Durchlauf " + (i + 1) + " ist Zufallszahl nicht im Bereich: " + zufallszahl);
                }
                assertTrue((zufallszahl >= 10) && (zufallszahl < 5001));
            }
        }
    }

    @Test
    public void testErzeugeZufallszahlenmenge2() {
        for (int i = 0; i < 100; i++) {
            Set<Integer> zufallszahlen = Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(500),
                    new BereichVon(10), new BereichBis(5001));
            assertEquals(500, zufallszahlen.size());
            for (Integer zufallszahl : zufallszahlen) {
                if (!((zufallszahl >= 10) && (zufallszahl < 5001))) {
                    System.out
                            .println("Bei Durchlauf " + (i + 1) + " ist Zufallszahl nicht im Bereich: " + zufallszahl);
                }
                assertTrue((zufallszahl >= 10) && (zufallszahl < 5001));
            }
        }
    }

    @Test
    public void testErzeugeZufallszahlenmenge3() {
        for (int i = 0; i < 100; i++) {
            Set<Integer> zufallszahlen = Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(500),
                    new BereichVon(10), new BereichBis(5001));
            assertEquals(500, zufallszahlen.size());
            for (Integer zufallszahl : zufallszahlen) {
                if (!((zufallszahl >= 10) && (zufallszahl < 5001))) {
                    System.out
                            .println("Bei Durchlauf " + (i + 1) + " ist Zufallszahl nicht im Bereich: " + zufallszahl);
                }
                assertTrue((zufallszahl >= 10) && (zufallszahl < 5001));
            }
        }
    }

    @Test
    public void testErzeugeZufallszahlenmenge4() {
        for (int i = 0; i < 100; i++) {
            Set<Integer> zufallszahlen = Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(500),
                    new BereichVon(10), new BereichBis(5001));
            assertEquals(500, zufallszahlen.size());
            for (Integer zufallszahl : zufallszahlen) {
                if (!((zufallszahl >= 10) && (zufallszahl < 5001))) {
                    System.out
                            .println("Bei Durchlauf " + (i + 1) + " ist Zufallszahl nicht im Bereich: " + zufallszahl);
                }
                assertTrue((zufallszahl >= 10) && (zufallszahl < 5001));
            }
        }
    }

    @Test
    public void testErzeugeZufallszahlenmenge5() {
        for (int i = 0; i < 100; i++) {
            Set<Integer> zufallszahlen = Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(500),
                    new BereichVon(10), new BereichBis(5001));
            assertEquals(500, zufallszahlen.size());
            for (Integer zufallszahl : zufallszahlen) {
                if (!((zufallszahl >= 10) && (zufallszahl < 5001))) {
                    System.out
                            .println("Bei Durchlauf " + (i + 1) + " ist Zufallszahl nicht im Bereich: " + zufallszahl);
                }
                assertTrue((zufallszahl >= 10) && (zufallszahl < 5001));
            }
        }
    }

    @Test
    public void testErzeugeZufallszahlenmenge6() {
        for (int i = 0; i < 100; i++) {
            Set<Integer> zufallszahlen = Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(500),
                    new BereichVon(10), new BereichBis(5001));
            assertEquals(500, zufallszahlen.size());
            for (Integer zufallszahl : zufallszahlen) {
                if (!((zufallszahl >= 10) && (zufallszahl < 5001))) {
                    System.out
                            .println("Bei Durchlauf " + (i + 1) + " ist Zufallszahl nicht im Bereich: " + zufallszahl);
                }
                assertTrue((zufallszahl >= 10) && (zufallszahl < 5001));
            }
        }
    }

    @Test
    public void testErzeugeZufallszahlenmenge7() {
        for (int i = 0; i < 100; i++) {
            Set<Integer> zufallszahlen = Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(500),
                    new BereichVon(10), new BereichBis(5001));
            assertEquals(500, zufallszahlen.size());
            for (Integer zufallszahl : zufallszahlen) {
                if (!((zufallszahl >= 10) && (zufallszahl < 5001))) {
                    System.out
                            .println("Bei Durchlauf " + (i + 1) + " ist Zufallszahl nicht im Bereich: " + zufallszahl);
                }
                assertTrue((zufallszahl >= 10) && (zufallszahl < 5001));
            }
        }
    }

    @Test
    public void testErzeugeZufallszahlenmenge8() {
        for (int i = 0; i < 100; i++) {
            Set<Integer> zufallszahlen = Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(500),
                    new BereichVon(10), new BereichBis(5001));
            assertEquals(500, zufallszahlen.size());
            for (Integer zufallszahl : zufallszahlen) {
                if (!((zufallszahl >= 10) && (zufallszahl < 5001))) {
                    System.out
                            .println("Bei Durchlauf " + (i + 1) + " ist Zufallszahl nicht im Bereich: " + zufallszahl);
                }
                assertTrue((zufallszahl >= 10) && (zufallszahl < 5001));
            }
        }
    }

    @Test
    public void testErzeugeZufallszahlenmenge9() {
        for (int i = 0; i < 100; i++) {
            Set<Integer> zufallszahlen = Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(500),
                    new BereichVon(10), new BereichBis(5001));
            assertEquals(500, zufallszahlen.size());
            for (Integer zufallszahl : zufallszahlen) {
                if (!((zufallszahl >= 10) && (zufallszahl < 5001))) {
                    System.out
                            .println("Bei Durchlauf " + (i + 1) + " ist Zufallszahl nicht im Bereich: " + zufallszahl);
                }
                assertTrue((zufallszahl >= 10) && (zufallszahl < 5001));
            }
        }
    }

    @Test
    public void testErzeugeZufallszahlenmenge10() {
        for (int i = 0; i < 100; i++) {
            Set<Integer> zufallszahlen = Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(500),
                    new BereichVon(10), new BereichBis(5001));
            assertEquals(500, zufallszahlen.size());
            for (Integer zufallszahl : zufallszahlen) {
                if (!((zufallszahl >= 10) && (zufallszahl < 5001))) {
                    System.out
                            .println("Bei Durchlauf " + (i + 1) + " ist Zufallszahl nicht im Bereich: " + zufallszahl);
                }
                assertTrue((zufallszahl >= 10) && (zufallszahl < 5001));
            }
        }
    }

    @Test
    public void testErzeugeZufallszahlenmengeParameterAnzahlZufallszahlenNull() {
        try {
            Zufallszahlengenerator.erzeugeZufallszahlenmenge(null, new BereichVon(100), new BereichBis(99));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testErzeugeZufallszahlenmengeParameterBereichVonNull() {
        try {
            Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(1), null, new BereichBis(99));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testErzeugeZufallszahlenmengeParameterBereichBisNull() {
        try {
            Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(1), new BereichVon(100), null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testErzeugeZufallszahlenmengeBereichFalsch() {
        try {
            Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(1), new BereichVon(100),
                    new BereichBis(99));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Der Bereich ist nicht groß genug.", e.getMessage());
        }
    }

    @Test
    public void testErzeugeZufallszahlenmengeAnzahlZufallszahlenFalsch() {
        try {
            Zufallszahlengenerator.erzeugeZufallszahlenmenge(new AnzahlZufallszahlen(100), new BereichVon(10),
                    new BereichBis(100));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Anzahl der Zufallszahlen ist größer als der Bereich.", e.getMessage());
        }
    }
}
