package de.platen.steganograph.uniformat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Samples;

public class UniFormatAudioTest {

    @Test
    public void testCheckParameterNull() {
        UniFormatAudio uniFormatAudio = erzeugeUniFormatAudio();
        try {
            uniFormatAudio.uebertrageBereichVonUniFormat(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Der Parameter ist null.", e.getMessage());
        }
        try {
            uniFormatAudio.uebertrageBereichZuUniFormat(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testCheckParameterKanalanzahl() {
        UniFormatAudio uniFormatAudio = erzeugeUniFormatAudio();
        try {
            uniFormatAudio.uebertrageBereichVonUniFormat(erzeugeSamples(3, 2));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Anzahl Kanäle stimmt nicht mit den Kanälen der Samples überein.", e.getMessage());
        }
        try {
            uniFormatAudio.uebertrageBereichZuUniFormat(erzeugeSamples(3, 2));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Anzahl Kanäle stimmt nicht mit den Kanälen der Samples überein.", e.getMessage());
        }
    }

    @Test
    public void testCheckParameterBlockgroesse() {
        UniFormatAudio uniFormatAudio = erzeugeUniFormatAudio();
        try {
            uniFormatAudio.uebertrageBereichVonUniFormat(erzeugeSamples(4, 200));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Sample-Länge ist größer als die Blockgröße.", e.getMessage());
        }
        try {
            uniFormatAudio.uebertrageBereichZuUniFormat(erzeugeSamples(4, 200));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Sample-Länge ist größer als die Blockgröße.", e.getMessage());
        }
    }

    @Test
    public void testUebertrageBereichZuUndVonUniFormat() {
        UniFormatAudio uniFormatAudio = erzeugeUniFormatAudio();
        Samples samples1 = erzeugeSamples(4, 100);
        uniFormatAudio.uebertrageBereichZuUniFormat(samples1);
        Samples samples2 = new Samples(new int[4][100]);
        uniFormatAudio.uebertrageBereichVonUniFormat(samples2);
        vergleichSamples(samples1, samples2);
    }

    private UniFormatAudio erzeugeUniFormatAudio() {
        return new UniFormatAudio(new AnzahlPositionen(100), new AnzahlKanaele(4), new Bittiefe(2),
                new ArrayList<Eintrag>());
    }

    private Samples erzeugeSamples(int kanalanzahl, int laenge) {
        int[][] samplewerte = new int[kanalanzahl][laenge];
        int wert = 0;
        for (int index1 = 0; index1 < kanalanzahl; index1++) {
            for (int index2 = 0; index2 < laenge; index2++) {
                samplewerte[index1][index2] = wert++;
            }
        }
        return new Samples(samplewerte);
    }

    private void vergleichSamples(Samples samples1, Samples samples2) {
        int[][] samplewerte1 = samples1.getSamples();
        int[][] samplewerte2 = samples2.getSamples();
        assertEquals(samplewerte1.length, samplewerte2.length);
        for (int index1 = 0; index1 < samplewerte1.length; index1++) {
            assertEquals(samplewerte1[index1].length, samplewerte2[index1].length);
            for (int index2 = 0; index2 < samplewerte1[index1].length; index2++) {
                assertEquals(samplewerte1[index1][index2], samplewerte2[index1][index2]);
            }
        }
    }
}
