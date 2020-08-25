package de.platen.steganograph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Blockgroesse;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Kanalnummer;
import de.platen.steganograph.datentypen.Positionsnummer;
import de.platen.steganograph.utils.ByteUtils;
import de.platen.steganograph.verteilregelgenerierung.KonfigurationVerteilregeln;
import de.platen.steganograph.verteilregelgenerierung.Verteilregelgenerierung;

public class AktionVersteckenTest {

    private static final int BLOCKGROESSE = 100;
    private static final int ANZAHL_NUTZDATEN = 50;
    private static final int ANZAHL_KANAELE = 4;
    private static final int BITTIEFE = 2;
    private static final int BILDBREITE = 50;
    private static final int BILDHOEHE = 50;
    private static final String DATEINAME_NUTZDATEN = "dateinameNutzdaten";

    @Test
    public void testParameterNull() {
        String dateinameNutzdaten = "dateinameNutzdaten";
        BufferedImage bufferedImageQuelle = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage bufferedImageZiel = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        byte[] verteilregel = { 0x01, 0x02, 0x03 };
        byte[] nutzdaten = { 0x04, 0x05, 0x06 };
        try {
            AktionVerstecken.versteckeNutzdaten(null, bufferedImageQuelle, bufferedImageZiel, verteilregel, nutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Ein oder mehrere Parameter sind null oder fehlerhaft.", e.getMessage());
        }
        try {
            AktionVerstecken.versteckeNutzdaten(dateinameNutzdaten, null, bufferedImageZiel, verteilregel, nutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Ein oder mehrere Parameter sind null oder fehlerhaft.", e.getMessage());
        }
        try {
            AktionVerstecken.versteckeNutzdaten(dateinameNutzdaten, bufferedImageQuelle, null, verteilregel, nutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Ein oder mehrere Parameter sind null oder fehlerhaft.", e.getMessage());
        }
        try {
            AktionVerstecken.versteckeNutzdaten(dateinameNutzdaten, bufferedImageQuelle, bufferedImageZiel, null,
                    nutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Ein oder mehrere Parameter sind null oder fehlerhaft.", e.getMessage());
        }
        try {
            AktionVerstecken.versteckeNutzdaten(dateinameNutzdaten, bufferedImageQuelle, bufferedImageZiel,
                    verteilregel, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Ein oder mehrere Parameter sind null oder fehlerhaft.", e.getMessage());
        }
    }

    @Test
    public void testParameterLeer() {
        String dateinameNutzdaten = "dateinameNutzdaten";
        BufferedImage bufferedImageQuelle = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage bufferedImageZiel = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        byte[] verteilregel = { 0x01, 0x02, 0x03 };
        byte[] nutzdaten = { 0x04, 0x05, 0x06 };
        try {
            AktionVerstecken.versteckeNutzdaten("", bufferedImageQuelle, bufferedImageZiel, verteilregel, nutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Ein oder mehrere Parameter sind null oder fehlerhaft.", e.getMessage());
        }
        try {
            AktionVerstecken.versteckeNutzdaten(dateinameNutzdaten, bufferedImageQuelle, bufferedImageZiel, new byte[0],
                    nutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Ein oder mehrere Parameter sind null oder fehlerhaft.", e.getMessage());
        }
        try {
            AktionVerstecken.versteckeNutzdaten(dateinameNutzdaten, bufferedImageQuelle, bufferedImageZiel,
                    verteilregel, new byte[0]);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Ein oder mehrere Parameter sind null oder fehlerhaft.", e.getMessage());
        }
    }

    @Test
    public void testBufferedImageUngleich() {
        String dateinameNutzdaten = "dateinameNutzdaten";
        byte[] verteilregel = { 0x01, 0x02, 0x03 };
        byte[] nutzdaten = { 0x04, 0x05, 0x06 };
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage bufferedImageUngleich1 = new BufferedImage(20, 10, BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage bufferedImageUngleich2 = new BufferedImage(10, 20, BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage bufferedImageUngleich3 = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
        try {
            AktionVerstecken.versteckeNutzdaten(dateinameNutzdaten, bufferedImage, bufferedImageUngleich1, verteilregel,
                    nutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Ein oder mehrere Parameter sind null oder fehlerhaft.", e.getMessage());
        }
        try {
            AktionVerstecken.versteckeNutzdaten(dateinameNutzdaten, bufferedImage, bufferedImageUngleich2, verteilregel,
                    nutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Ein oder mehrere Parameter sind null oder fehlerhaft.", e.getMessage());
        }
        try {
            AktionVerstecken.versteckeNutzdaten(dateinameNutzdaten, bufferedImage, bufferedImageUngleich3, verteilregel,
                    nutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Ein oder mehrere Parameter sind null oder fehlerhaft.", e.getMessage());
        }
    }

    @Test
    public void testVersteckeNutzdatenFuer1BlockAlsTeilblock() {
        Blockgroesse blockgroesse = new Blockgroesse(BLOCKGROESSE);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(ANZAHL_NUTZDATEN);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(ANZAHL_KANAELE);
        Bittiefe bittiefe = new Bittiefe(BITTIEFE);
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        int anzahlBytes = 10;
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        BufferedImage bufferedImageQuelle = erzeugeBild(BILDBREITE, BILDHOEHE);
        BufferedImage bufferedImageZiel = erzeugeBild(BILDBREITE, BILDHOEHE);
        AktionVerstecken.versteckeNutzdaten(DATEINAME_NUTZDATEN, bufferedImageQuelle, bufferedImageZiel, verteilregel,
                nutzdaten);
        pruefeStartblock(anzahlBytes, DATEINAME_NUTZDATEN, bufferedImageZiel);
        pruefeDatenblock(1, nutzdaten, bufferedImageZiel);
    }

    @Test
    public void testVersteckeNutzdatenFuer1BlockAlsKomplettblock() {
        Blockgroesse blockgroesse = new Blockgroesse(BLOCKGROESSE);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(ANZAHL_NUTZDATEN);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(ANZAHL_KANAELE);
        Bittiefe bittiefe = new Bittiefe(BITTIEFE);
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        int anzahlBytes = 50;
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        BufferedImage bufferedImageQuelle = erzeugeBild(BILDBREITE, BILDHOEHE);
        BufferedImage bufferedImageZiel = erzeugeBild(BILDBREITE, BILDHOEHE);
        AktionVerstecken.versteckeNutzdaten(DATEINAME_NUTZDATEN, bufferedImageQuelle, bufferedImageZiel, verteilregel,
                nutzdaten);
        pruefeStartblock(anzahlBytes, DATEINAME_NUTZDATEN, bufferedImageZiel);
        pruefeDatenblock(1, nutzdaten, bufferedImageZiel);
    }

    @Test
    public void testVersteckeNutzdatenFuerMehrereBloeckeKompletteBloecke() {
        Blockgroesse blockgroesse = new Blockgroesse(BLOCKGROESSE);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(ANZAHL_NUTZDATEN);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(ANZAHL_KANAELE);
        Bittiefe bittiefe = new Bittiefe(BITTIEFE);
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        int anzahlBytes = 150;
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        BufferedImage bufferedImageQuelle = erzeugeBild(BILDBREITE, BILDHOEHE);
        BufferedImage bufferedImageZiel = erzeugeBild(BILDBREITE, BILDHOEHE);
        AktionVerstecken.versteckeNutzdaten(DATEINAME_NUTZDATEN, bufferedImageQuelle, bufferedImageZiel, verteilregel,
                nutzdaten);
        pruefeStartblock(anzahlBytes, DATEINAME_NUTZDATEN, bufferedImageZiel);
        pruefeDatenblock(1, nutzdaten, bufferedImageZiel);
        pruefeDatenblock(2, nutzdaten, bufferedImageZiel);
        pruefeDatenblock(3, nutzdaten, bufferedImageZiel);
    }

    @Test
    public void testVersteckeNutzdatenFuerMehrereBloeckeLetzterBlockTeilblock() {
        Blockgroesse blockgroesse = new Blockgroesse(BLOCKGROESSE);
        AnzahlNutzdaten anzahlNutzdaten = new AnzahlNutzdaten(ANZAHL_NUTZDATEN);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(ANZAHL_KANAELE);
        Bittiefe bittiefe = new Bittiefe(BITTIEFE);
        byte[] verteilregel = generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe);
        int anzahlBytes = 125;
        byte[] nutzdaten = erzeugeNutzdaten(anzahlBytes);
        BufferedImage bufferedImageQuelle = erzeugeBild(BILDBREITE, BILDHOEHE);
        BufferedImage bufferedImageZiel = erzeugeBild(BILDBREITE, BILDHOEHE);
        String dateinameNutzdaten = "dateinameNutzdaten";
        AktionVerstecken.versteckeNutzdaten(dateinameNutzdaten, bufferedImageQuelle, bufferedImageZiel, verteilregel,
                nutzdaten);
        pruefeStartblock(anzahlBytes, dateinameNutzdaten, bufferedImageZiel);
        pruefeDatenblock(1, nutzdaten, bufferedImageZiel);
        pruefeDatenblock(2, nutzdaten, bufferedImageZiel);
        pruefeDatenblock(3, nutzdaten, bufferedImageZiel);
    }

    @Test
    public void testVerteileByte() {
        assertEquals("00000011000000110000001100000011", verteileByte((byte) 0xFF));
        assertEquals("00000000000000000000001100000011", verteileByte((byte) 0x0F));
        assertEquals("00000000000000000000000000000011", verteileByte((byte) 0x03));
        assertEquals("00000000000000000000001100000000", verteileByte((byte) 0x0C));
        assertEquals("00000011000000110000000000000000", verteileByte((byte) 0xF0));
        assertEquals("00000000000000110000000000000000", verteileByte((byte) 0x30));
        assertEquals("00000011000000000000000000000000", verteileByte((byte) 0xC0));
    }

    private byte[] generiere(Blockgroesse blockgroesse, AnzahlNutzdaten anzahlNutzdaten, AnzahlKanaele anzahlKanaele,
            Bittiefe bittiefe) {
        KonfigurationVerteilregeln konfigurationGenerierung = new KonfigurationVerteilregeln(blockgroesse,
                anzahlNutzdaten, bittiefe, anzahlKanaele);
        Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        List<Eintrag> eintraege = erzeugeEintraege(anzahlNutzdaten.get());
        return verteilregelgenerierung.generiereMitVorgabe(eintraege);
    }

    private byte[] erzeugeNutzdaten(int anzahl) {
        boolean ende = false;
        int position = 0;
        byte[] daten = new byte[anzahl];
        while (!ende) {
            for (int index = 0; index < 255; index++) {
                daten[position] = (byte) index;
                position++;
                if (position == anzahl) {
                    return daten;
                }
            }
        }
        return daten;
    }

    private BufferedImage erzeugeBild(int breite, int hoehe) {
        BufferedImage bufferedImage = new BufferedImage(breite, hoehe, BufferedImage.TYPE_4BYTE_ABGR);
        int rgb = 0x00000000;
        for (int x = 0; x < breite; x++) {
            for (int y = 0; y < hoehe; y++) {
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        return bufferedImage;
    }

    private List<Eintrag> erzeugeEintraege(int anzahlBytes) {
        List<Eintrag> eintraege = new ArrayList<>();
        for (int positionsnummer = 1; positionsnummer <= anzahlBytes; positionsnummer++) {
            for (int kanalnummer = 1; kanalnummer <= 4; kanalnummer++) {
                eintraege.add(new Eintrag(new Positionsnummer(positionsnummer), new Kanalnummer(kanalnummer)));
            }
        }
        return eintraege;
    }

    private void pruefeStartblock(int anzahlNutzdaten, String dateiname, BufferedImage bufferedImage) {
        pruefeAnzahl(anzahlNutzdaten, bufferedImage, 0, 0);
        pruefeAnzahl(dateiname.length(), bufferedImage, 4, 0);
        pruefeBytes(dateiname.getBytes(), bufferedImage, 8, 0);
    }

    private void pruefeAnzahl(int wert, BufferedImage bufferedImage, int x, int y) {
        String[] rgb = new String[4];
        for (int index = 0; index < 4; index++) {
            rgb[index] = fuelleNullen(Integer.toBinaryString(bufferedImage.getRGB(x + index, y)), 32);
        }
        byte[] anzahl = ByteUtils.intToBytes(wert);
        for (int index = 0; index < 4; index++) {
            String anzahlwert = verteileByte(anzahl[index]);
            assertEquals(anzahlwert, rgb[index]);
        }
    }

    private void pruefeBytes(byte[] bytes, BufferedImage bufferedImage, int x, int y) {
        String[] rgb = new String[bytes.length];
        for (int index = 0; index < bytes.length; index++) {
            rgb[index] = fuelleNullen(Integer.toBinaryString(bufferedImage.getRGB(x + index, y)), 32);
        }
        for (int index = 0; index < bytes.length; index++) {
            String bytewert = verteileByte(bytes[index]);
            assertEquals(bytewert, rgb[index]);
        }
    }

    private void pruefeDatenblock(int datenblocknummer, byte[] nutzdaten, BufferedImage bufferedImage) {
        int x = 0;
        int y = (BLOCKGROESSE / BILDBREITE) * datenblocknummer;
        int startindex = (datenblocknummer - 1) * ANZAHL_NUTZDATEN;
        int anzahl = ANZAHL_NUTZDATEN;
        if ((nutzdaten.length - startindex) < ANZAHL_NUTZDATEN) {
            anzahl = nutzdaten.length - startindex;
        }
        for (int index = startindex, indexX = x; index < (startindex + anzahl); index++, indexX++) {
            String bytewert = verteileByte(nutzdaten[index]);
            String inhalt = fuelleNullen(Integer.toBinaryString(bufferedImage.getRGB(x + indexX, y)), 32);
            assertEquals(bytewert, inhalt);
        }
    }

    private String verteileByte(byte wert) {
        String vorgabe = "000000";
        String bytewert = Integer.toBinaryString(Byte.toUnsignedInt(wert));
        if (bytewert.length() < 8) {
            int laenge = 8 - bytewert.length();
            String nullen = "";
            for (int i = 0; i < laenge; i++) {
                nullen += "0";
            }
            bytewert = nullen + bytewert;
        }
        String kanal4 = vorgabe + bytewert.substring(0, 2);
        String kanal3 = vorgabe + bytewert.substring(2, 4);
        String kanal2 = vorgabe + bytewert.substring(4, 6);
        String kanal1 = vorgabe + bytewert.substring(6, 8);
        return kanal4 + kanal3 + kanal2 + kanal1;
    }

    private String fuelleNullen(String wert, int anzahl) {
        if (wert.length() < anzahl) {
            int laenge = anzahl - wert.length();
            String nullen = "";
            for (int i = 0; i < laenge; i++) {
                nullen += "0";
            }
            wert = nullen + wert;
        }
        return wert;
    }
}
