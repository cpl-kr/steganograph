package de.platen.steganograph.datentypen;

public class Samples {

    private final int[][] samples;

    private static final String FEHLER_PARAMETER_NULL = "Parameter ist null.";
    private static final String FEHLER_PARAMETER_SAMPLE_LAENGE_NULL = "Die Sample-Länge ist null.";
    private static final String FEHLER_PARAMETER_SAMPLE_LAENGE_UNGLEICH = "Die Sample-Länge ist nicht gleich.";

    public Samples(int[][] samples) {
        this.samples = samples;
        checkParameter(samples);
    }

    public int[][] getSamples() {
        return samples;
    }

    private void checkParameter(int[][] samples) {
        if (samples == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        int referenzLaenge = samples[0].length;
        for (int index = 0; index < samples.length; index++) {
            if (samples[index] == null) {
                throw new IllegalArgumentException(FEHLER_PARAMETER_SAMPLE_LAENGE_NULL);
            }
            if (samples[index].length != referenzLaenge) {
                throw new IllegalArgumentException(FEHLER_PARAMETER_SAMPLE_LAENGE_UNGLEICH);
            }
        }
    }
}
