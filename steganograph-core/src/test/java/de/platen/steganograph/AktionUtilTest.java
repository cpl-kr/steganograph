package de.platen.steganograph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.utils.ByteUtils;

public class AktionUtilTest {

    @Test
    public void testBereiteStartblockdatenParameterAnzahlNutzdatenFalsch() {
        int anzahlNutzdaten = 0;
        AnzahlNutzdaten anzahlNutzdatenBlock = new AnzahlNutzdaten(20);
        String dateiname = "test";
        try {
            AktionUtil.bereiteStartblockdaten(anzahlNutzdaten, anzahlNutzdatenBlock, dateiname);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter falsch.", e.getMessage());
        }
    }

    @Test
    public void testBereiteStartblockdatenParameterAnzahlNutzdatenBlockNull() {
        int anzahlNutzdaten = 10;
        AnzahlNutzdaten anzahlNutzdatenBlock = null;
        String dateiname = "test";
        try {
            AktionUtil.bereiteStartblockdaten(anzahlNutzdaten, anzahlNutzdatenBlock, dateiname);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter falsch.", e.getMessage());
        }
    }

    @Test
    public void testBereiteStartblockdatenParameterDateinameNull() {
        int anzahlNutzdaten = 10;
        AnzahlNutzdaten anzahlNutzdatenBlock = new AnzahlNutzdaten(20);
        String dateiname = null;
        try {
            AktionUtil.bereiteStartblockdaten(anzahlNutzdaten, anzahlNutzdatenBlock, dateiname);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter falsch.", e.getMessage());
        }
    }

    @Test
    public void testBereiteStartblockdatenParameterDateinameLeer() {
        int anzahlNutzdaten = 10;
        AnzahlNutzdaten anzahlNutzdatenBlock = new AnzahlNutzdaten(20);
        String dateiname = "";
        try {
            AktionUtil.bereiteStartblockdaten(anzahlNutzdaten, anzahlNutzdatenBlock, dateiname);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter falsch.", e.getMessage());
        }
    }

    @Test
    public void testBereiteStartblockdatenDatenmenge() {
        int anzahlNutzdaten = 10;
        AnzahlNutzdaten anzahlNutzdatenBlock = new AnzahlNutzdaten(2);
        String dateiname = "test";
        try {
            AktionUtil.bereiteStartblockdaten(anzahlNutzdaten, anzahlNutzdatenBlock, dateiname);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Es können nicht alle Daten im Block untergebracht werden.", e.getMessage());
        }
    }

    @Test
    public void testBereiteStartblockdaten() {
        int anzahlNutzdaten = 10;
        AnzahlNutzdaten anzahlNutzdatenBlock = new AnzahlNutzdaten(20);
        String dateiname = "test/dateiname";
        byte[] daten = AktionUtil.bereiteStartblockdaten(anzahlNutzdaten, anzahlNutzdatenBlock, dateiname);
        assertNotNull(daten);
        assertEquals(8 + "dateiname".getBytes().length, daten.length);
        byte[] zahl = new byte[4];
        System.arraycopy(daten, 0, zahl, 0, zahl.length);
        assertEquals(anzahlNutzdaten, ByteUtils.bytesToInt(zahl));
        System.arraycopy(daten, 4, zahl, 0, zahl.length);
        assertEquals("dateiname".getBytes().length, ByteUtils.bytesToInt(zahl));
        byte[] name = new byte["dateiname".getBytes().length];
        System.arraycopy(daten, 8, name, 0, name.length);
        assertEquals("dateiname", new String(name));
    }
}
