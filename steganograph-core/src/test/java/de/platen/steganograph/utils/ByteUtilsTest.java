package de.platen.steganograph.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ByteUtilsTest {

    @Test
    public void testLoescheBitsBittiefe1() {
        assertEquals(0xFFFFFFFE, ByteUtils.loescheBits(0xFFFFFFFF, 1));
        assertEquals(0xFFFFFFFE, ByteUtils.loescheBits(0xFFFFFFFE, 1));
        assertEquals(0x0000000E, ByteUtils.loescheBits(0x0000000E, 1));
        assertEquals(0x0000000E, ByteUtils.loescheBits(0x0000000F, 1));
    }

    @Test
    public void testLoescheBitsBittiefe2() {
        assertEquals(0xFFFFFFFC, ByteUtils.loescheBits(0xFFFFFFFF, 2));
        assertEquals(0xFFFFFFFC, ByteUtils.loescheBits(0xFFFFFFFE, 2));
        assertEquals(0xFFFFFFFC, ByteUtils.loescheBits(0xFFFFFFFD, 2));
        assertEquals(0xFFFFFFFC, ByteUtils.loescheBits(0xFFFFFFFC, 2));
        assertEquals(0x0000000C, ByteUtils.loescheBits(0x0000000F, 2));
        assertEquals(0x0000000C, ByteUtils.loescheBits(0x0000000E, 2));
        assertEquals(0x0000000C, ByteUtils.loescheBits(0x0000000D, 2));
        assertEquals(0x0000000C, ByteUtils.loescheBits(0x0000000C, 2));
    }

    @Test
    public void testLoescheBitsBittiefe4() {
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFFF, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFFE, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFFD, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFFC, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFFB, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFFA, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFF9, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFF8, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFF7, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFF6, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFF5, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFF4, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFF3, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFF2, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFF1, 4));
        assertEquals(0xFFFFFFF0, ByteUtils.loescheBits(0xFFFFFFF0, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x0000000F, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x0000000E, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x0000000D, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x0000000C, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x0000000B, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x0000000A, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x00000009, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x00000008, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x00000007, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x00000006, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x00000005, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x00000004, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x00000003, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x00000002, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x00000001, 4));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x00000000, 4));
    }

    @Test
    public void testLoescheBitsBittiefe8() {
        assertEquals(0xFFFFFF00, ByteUtils.loescheBits(0xFFFFFFFF, 8));
        assertEquals(0x00000000, ByteUtils.loescheBits(0x0000000F, 8));
    }

    @Test
    public void testLoescheBitsBittiefeAndere() {
        assertEquals(0xFFFFFFFF, ByteUtils.loescheBits(0xFFFFFFFF, 3));
        assertEquals(0x0000000F, ByteUtils.loescheBits(0x0000000F, 3));
        assertEquals(0xFFFFFFFF, ByteUtils.loescheBits(0xFFFFFFFF, 5));
        assertEquals(0x0000000F, ByteUtils.loescheBits(0x0000000F, 5));
        assertEquals(0xFFFFFFFF, ByteUtils.loescheBits(0xFFFFFFFF, 6));
        assertEquals(0x0000000F, ByteUtils.loescheBits(0x0000000F, 6));
        assertEquals(0xFFFFFFFF, ByteUtils.loescheBits(0xFFFFFFFF, 7));
        assertEquals(0x0000000F, ByteUtils.loescheBits(0x0000000F, 7));
    }

    @Test
    public void testSetzeBitteilAusByteInWertBittiefe8() {
        assertEquals(0xFFFFFFFF, ByteUtils.setzeBitteilAusByteInWert(0xFFFFFFFF, 0, 8, (byte) 0xFF));
        assertEquals(0xFFFFFFFE, ByteUtils.setzeBitteilAusByteInWert(0xFFFFFFFF, 0, 8, (byte) 0xFE));
        assertEquals(0xFFFFFF00, ByteUtils.setzeBitteilAusByteInWert(0xFFFFFFFF, 0, 8, (byte) 0x00));
        assertEquals(0xFFFFFF0F, ByteUtils.setzeBitteilAusByteInWert(0xFFFFFFFF, 0, 8, (byte) 0x0F));
        assertEquals(0x000000FF, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 0, 8, (byte) 0xFF));
        assertEquals(0x000000FE, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 0, 8, (byte) 0xFE));
        assertEquals(0x00000000, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 0, 8, (byte) 0x00));
        assertEquals(0x0000000F, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 0, 8, (byte) 0x0F));
    }

    @Test
    public void testSetzeBitteilAusByteInWertBittiefe4() {
        assertEquals(0x0000000F, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 0, 4, (byte) 0x0F));
        assertEquals(0x0000000F, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 1, 4, (byte) 0xF0));
    }

    @Test
    public void testSetzeBitteilAusByteInWertBittiefe2() {
        assertEquals(0x00000003, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 0, 2, (byte) 0x03));
        assertEquals(0x00000003, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 1, 2, (byte) 0x0C));
        assertEquals(0x00000003, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 2, 2, (byte) 0x30));
        assertEquals(0x00000003, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 3, 2, (byte) 0xC0));
    }

    @Test
    public void testSetzeBitteilAusByteInWertBittiefe1() {
        assertEquals(0xFFFFFFFF, ByteUtils.setzeBitteilAusByteInWert(0xFFFFFFFF, 0, 1, (byte) 0xFF));
        assertEquals(0xFFFFFFFE, ByteUtils.setzeBitteilAusByteInWert(0xFFFFFFFF, 0, 1, (byte) 0xFE));
        assertEquals(0xFFFFFFFF, ByteUtils.setzeBitteilAusByteInWert(0xFFFFFFFF, 0, 1, (byte) 0x01));
        assertEquals(0xFFFFFFFE, ByteUtils.setzeBitteilAusByteInWert(0xFFFFFFFF, 0, 1, (byte) 0x00));
        assertEquals(0x00000001, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 1, 1, (byte) 0x02));
        assertEquals(0x00000001, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 2, 1, (byte) 0x04));
        assertEquals(0x00000001, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 3, 1, (byte) 0x08));
        assertEquals(0x00000001, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 4, 1, (byte) 0x10));
        assertEquals(0x00000001, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 5, 1, (byte) 0x20));
        assertEquals(0x00000001, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 6, 1, (byte) 0x40));
        assertEquals(0x00000001, ByteUtils.setzeBitteilAusByteInWert(0x00000000, 7, 1, (byte) 0x80));
    }

    @Test
    public void testHoleBitteilAusWertBittiefe8() {
        assertEquals(0x00000000, ByteUtils.holeBitteilAusWert(0x00000000, 0, 8));
        assertEquals(0x000000FF, ByteUtils.holeBitteilAusWert(0x000000FF, 0, 8));
    }

    @Test
    public void testHoleBitteilAusWertBittiefe4() {
        assertEquals(0x0000000F, ByteUtils.holeBitteilAusWert(0x0000000F, 0, 4));
        assertEquals(0x000000F0, ByteUtils.holeBitteilAusWert(0x0000000F, 1, 4));
    }

    @Test
    public void testHoleBitteilAusWertBittiefe2() {
        assertEquals(0x00000003, ByteUtils.holeBitteilAusWert(0x00000003, 0, 2));
        assertEquals(0x0000000C, ByteUtils.holeBitteilAusWert(0x00000003, 1, 2));
        assertEquals(0x00000030, ByteUtils.holeBitteilAusWert(0x00000003, 2, 2));
        assertEquals(0x000000C0, ByteUtils.holeBitteilAusWert(0x00000003, 3, 2));
    }

    @Test
    public void testHoleBitteilAusWertBittiefe1() {
        assertEquals(0x00000001, ByteUtils.holeBitteilAusWert(0x00000001, 0, 1));
        assertEquals(0x00000002, ByteUtils.holeBitteilAusWert(0x00000001, 1, 1));
        assertEquals(0x00000004, ByteUtils.holeBitteilAusWert(0x00000001, 2, 1));
        assertEquals(0x00000008, ByteUtils.holeBitteilAusWert(0x00000001, 3, 1));
        assertEquals(0x00000010, ByteUtils.holeBitteilAusWert(0x00000001, 4, 1));
        assertEquals(0x00000020, ByteUtils.holeBitteilAusWert(0x00000001, 5, 1));
        assertEquals(0x00000040, ByteUtils.holeBitteilAusWert(0x00000001, 6, 1));
        assertEquals(0x00000080, ByteUtils.holeBitteilAusWert(0x00000001, 7, 1));
    }
}
