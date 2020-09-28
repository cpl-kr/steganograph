package de.platen.steganograph.datentypen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SamplesTest {

    @Test
    public void testSamplesNull() {
        try {
            new Samples(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testSamplesLaengeNull() {
        try {
            new Samples(erzeugeSamplesLaengeNull(4, 2));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Sample-Länge ist null.", e.getMessage());
        }
    }

    @Test
    public void testSamplesLaengeUngleich() {
        try {
            new Samples(erzeugeSamplesLaengeUngleich(4, 2));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Sample-Länge ist nicht gleich.", e.getMessage());
        }
    }

    @Test
    public void testSamples() {
        int[][] sampleWerte = erzeugeSamples(4, 2);
        Samples samples = new Samples(sampleWerte);
        vergleicheSamples(samples.getSamples(), 4, 2);
    }

    private int[][] erzeugeSamplesLaengeNull(int anzahlKanaele, int laenge) {
        int[][] samples = new int[anzahlKanaele][laenge];
        int wert = 1;
        for (int index1 = 0; index1 < anzahlKanaele; index1++) {
            for (int index2 = 0; index2 < laenge; index2++) {
                samples[index1][index2] = wert++;
            }
        }
        samples[1] = null;
        return samples;
    }

    private int[][] erzeugeSamplesLaengeUngleich(int anzahlKanaele, int laenge) {
        int[][] samples = new int[anzahlKanaele][laenge];
        int wert = 1;
        for (int index1 = 0; index1 < anzahlKanaele; index1++) {
            for (int index2 = 0; index2 < laenge; index2++) {
                samples[index1][index2] = wert++;
            }
        }
        samples[1] = new int[laenge + 1];
        return samples;
    }

    private int[][] erzeugeSamples(int anzahlKanaele, int laenge) {
        int[][] samples = new int[anzahlKanaele][laenge];
        int wert = 1;
        for (int index1 = 0; index1 < anzahlKanaele; index1++) {
            for (int index2 = 0; index2 < laenge; index2++) {
                samples[index1][index2] = wert++;
            }
        }
        return samples;
    }

    private void vergleicheSamples(int[][] samples, int anzahlKanaele, int laenge) {
        int wert = 1;
        for (int index1 = 0; index1 < anzahlKanaele; index1++) {
            for (int index2 = 0; index2 < laenge; index2++) {
                assertEquals(wert++, samples[index1][index2]);
            }
        }
    }
}
