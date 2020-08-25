package de.platen.steganograph.utils;

public class Bildpunkt4ByteABGR {

    private final int farbe;

    public Bildpunkt4ByteABGR(int farbe) {
        this.farbe = farbe;
    }

    public int getRot() {
        return (farbe & 0xff0000) >> 16;
    }

    public int getGruen() {
        return (farbe & 0xff00) >> 8;
    }

    public int getBlau() {
        return farbe & 0xff;
    }

    public int getAlpha() {
        return (farbe & 0xff000000) >>> 24;
    }

    public static int getRot(int kanalwert) {
        return kanalwert << 16;
    }

    public static int getGruen(int kanalwert) {
        return kanalwert << 8;
    }

    public static int getBlau(int kanalwert) {
        return kanalwert;
    }

    public static int getAlpha(int kanalwert) {
        return kanalwert << 24;
    }
}
