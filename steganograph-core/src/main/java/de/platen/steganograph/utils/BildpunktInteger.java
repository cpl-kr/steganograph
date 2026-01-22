package de.platen.steganograph.utils;

public class BildpunktInteger {

    private final int farbe;

    public BildpunktInteger(int farbe) {
        this.farbe = farbe;
    }

    public int getByte1() {
        return farbe & 0xff;
    }

    public int getByte2() {
        return (farbe & 0xff00) >> 8;
    }

    public int getByte3() {
        return (farbe & 0xff0000) >> 16;
    }

    public int getByte4() {
        return (farbe & 0xff000000) >>> 24;
    }

    public static int getByte1(int kanalwert) {
        return kanalwert;
    }

    public static int getByte2(int kanalwert) {
        return kanalwert << 8;
    }

    public static int getByte3(int kanalwert) {
        return kanalwert << 16;
    }

    public static int getByte4(int kanalwert) {
        return kanalwert << 24;
    }
}
