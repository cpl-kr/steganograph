package de.platen.steganograph.uniformat;

import java.util.List;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Kanalnummer;
import de.platen.steganograph.datentypen.Positionsinhalt;
import de.platen.steganograph.datentypen.Positionsnummer;
import de.platen.steganograph.datentypen.Samples;

public class UniFormatAudio extends UniFormat {

    private static final String FEHLER_PARAMETER_NULL = "Der Parameter ist null.";
    private static final String FEHLER_PARAMETER_KANAHLANZAHL = "Die Anzahl Kanäle stimmt nicht mit den Kanälen der Samples überein.";
    private static final String FEHLER_PARAMETER_SAMPLE_LAENGE_BLOCKGROESSE = "Die Sample-Länge ist größer als die Blockgröße.";

    public UniFormatAudio(AnzahlPositionen anzahlPositionen, AnzahlKanaele anzahlKanaele, Bittiefe bittiefe,
            List<Eintrag> eintraege) {
        super(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
    }

    public void uebertrageBereichZuUniFormat(Samples samples) {
        checkParameter(samples);
        int[][] samplewerte = samples.getSamples();
        for (int index = 0; index < samplewerte[0].length; index++) {
            Positionsinhalt positionsinhalt = new Positionsinhalt(anzahlKanaele);
            for (int indexKanal = 0; indexKanal < samplewerte.length; indexKanal++) {
                positionsinhalt.setzeWert(new Kanalnummer(indexKanal + 1), samplewerte[indexKanal][index]);
            }
            positionsinhalte.put(new Positionsnummer(index + 1), positionsinhalt);
        }
    }

    public void uebertrageBereichVonUniFormat(Samples samples) {
        checkParameter(samples);
        int[][] samplewerte = samples.getSamples();
        for (int index = 0; index < samplewerte[0].length; index++) {
            Positionsnummer positionsnummer = new Positionsnummer(index + 1);
            Positionsinhalt positionsinhalt = positionsinhalte.get(positionsnummer);
            for (int indexKanal = 0; indexKanal < samplewerte.length; indexKanal++) {
                samplewerte[indexKanal][index] = positionsinhalt.holeWert(new Kanalnummer(indexKanal + 1));
            }
            positionsinhalte.put(new Positionsnummer(index + 1), positionsinhalt);
        }
    }

    private void checkParameter(Samples samples) {
        if (samples == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        int[][] sampleWerte = samples.getSamples();
        if (sampleWerte.length != anzahlKanaele.get()) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_KANAHLANZAHL);
        }
        for (int index = 0; index < sampleWerte.length; index++) {
            if (sampleWerte[index].length > anzahlPositionen.get()) {
                throw new IllegalArgumentException(FEHLER_PARAMETER_SAMPLE_LAENGE_BLOCKGROESSE);
            }
        }
    }
}
