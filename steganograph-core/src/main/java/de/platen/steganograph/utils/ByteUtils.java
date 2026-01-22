package de.platen.steganograph.utils;

import java.nio.ByteBuffer;

public class ByteUtils {

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getLong();
    }

    public static int bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getInt();
    }

    public static int setzeIntWert(final byte[] daten, final int offset, final int wert) {
        byte[] wertInBytes = ByteUtils.intToBytes(wert);
        System.arraycopy(wertInBytes, 0, daten, offset, wertInBytes.length);
        return offset + wertInBytes.length;
    }

    public static int setzeAlsByteWert(final byte[] daten, final int offset, final int wert) {
        byte wertInByte = (byte) wert;
        daten[offset] = wertInByte;
        return offset + 1;
    }

    public static int setzeByteArray(final byte[] daten, final int offset, final byte[] werte) {
        System.arraycopy(werte, 0, daten, offset, werte.length);
        return offset + werte.length;
    }

    public static int setzeBitteilAusByteInWert(int wert, int bitteil, int bittiefe, byte nutzdatenByte) {
        int nutzdatenwert = nutzdatenByte & (byte) 0xFF;
        int wertNeu = loescheBits(wert, bittiefe);
        int byteNeu = 0x00;
        if (bittiefe != 8) {
            int bitmaske = verschiebeBitsLinks(erstelleBitmaske(bittiefe), bitteil, bittiefe);
            byteNeu = verschiebeBitsRechts(filtereWert(nutzdatenwert, bitmaske), bitteil, bittiefe);
        } else {
            byteNeu = nutzdatenByte & 0xFF;
        }
        return wertNeu | byteNeu;
    }

    public static int holeBitteilAusWert(int wert, int bitteil, int bittiefe) {
        int bitmaske = erstelleBitmaske(bittiefe);
        int bitmaskenwert = 0x000000 | bitmaske;
        int rest = wert & bitmaskenwert;
        return verschiebeBitsLinks(rest, bitteil, bittiefe);
    }

    public static int loescheBits(int wert, int bittiefe) {
        int bits = 0;
        switch (bittiefe) {
        case 1:
            bits = 0xFFFFFFFE;
            break;
        case 2:
            bits = 0xFFFFFFFC;
            break;
        case 4:
            bits = 0xFFFFFFF0;
            break;
        case 8:
            bits = 0xFFFFFF00;
            break;
        default:
            return wert;
        }
        return wert & bits;
    }

    private static int erstelleBitmaske(int bittiefe) {
        switch (bittiefe) {
        case 1:
            return 0x01;
        case 2:
            return 0x03;
        case 4:
            return 0x0F;
        case 8:
            return (byte) 0xFF;
        }
        return 0x00;
    }

    private static int verschiebeBitsLinks(int datenByte, int bitteil, int bittiefe) {
        int schiebewert = bittiefe * bitteil;
        return Integer.rotateLeft(datenByte, schiebewert);
    }

    private static int verschiebeBitsRechts(int datenByte, int bitteil, int bittiefe) {
        int schiebewert = bittiefe * bitteil;
        return Integer.rotateRight(datenByte, schiebewert);
    }

    private static int filtereWert(int datenByte, int bitmaske) {
        return datenByte & bitmaske;
    }
}