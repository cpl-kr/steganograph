package de.platen.steganograph.verteilregelgenerierung;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Blockgroesse;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Kanalnummer;
import de.platen.steganograph.datentypen.Positionsnummer;
import de.platen.steganograph.utils.ByteUtils;

public class VerteilregelgenerierungTest {

    private static final String FEHLER_PARAMETER_NULL = "Parameter ist null.";

    private static final int BLOCKGROESSE = 100;
    private static final int ANZAHL_NUTZDATEN = 5;
    private static final byte ANZAHL_KANAELE = 0x04;
    private static final byte BITTIEFE = 0x02;
    private static final int ANZAHL_BYTES_VERTEILUNG = 5 * 5 * 4;
    private static final int ANZAHL_EINTRAEGE = 20;
    private static final int GROESSE_EINTRAG = 5;

    @Test
    public void testParameterNull() {
        try {
            new Verteilregelgenerierung(null);
            fail();
        } catch (final NullPointerException e) {
            assertEquals(FEHLER_PARAMETER_NULL, e.getMessage());
        }
    }

    @Test
    public void testGeneriereByteArray() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final byte[] konfiguration = verteilregelgenerierung.generiere();
        checkErgebnis(konfiguration);
    }

    @Test
    public void testGeneriereByteArrayAndereKonfiguration() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration(100, 50, 3, 2);
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final byte[] konfiguration = verteilregelgenerierung.generiere();
        checkErgebnis(konfiguration, 100, 50, (byte) 0x03, (byte) 0x02);
    }

    @Test
    public void testGeneriereMitVorgabeParameterNull() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        try {
            verteilregelgenerierung.generiereMitVorgabe(null);
        } catch (final NullPointerException e) {
            assertEquals("Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testGeneriereMitVorgabeParameterLeer() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        try {
            verteilregelgenerierung.generiereMitVorgabe(new ArrayList<Eintrag>());
        } catch (final IllegalArgumentException e) {
            assertEquals("Die Einträge sind fehlerhaft.", e.getMessage());
        }
    }

    @Test
    public void testGeneriereMitVorgabeParameterAnzahlElementeFalsch() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final List<Eintrag> eintraege = new ArrayList<>();
        final Eintrag eintrag = new Eintrag(new Positionsnummer(1), new Kanalnummer(1));
        eintraege.add(eintrag);
        try {
            verteilregelgenerierung.generiereMitVorgabe(eintraege);
        } catch (final IllegalArgumentException e) {
            assertEquals("Die Einträge sind fehlerhaft.", e.getMessage());
        }
    }

    @Test
    public void testGeneriereMitVorgabeParameterPositionsnummerFalsch() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final List<Eintrag> eintraege = new ArrayList<>();
        eintraege.add(erzeugeEintrag(1, 1));
        eintraege.add(erzeugeEintrag(1, 2));
        eintraege.add(erzeugeEintrag(1, 3));
        eintraege.add(erzeugeEintrag(1, 4));
        eintraege.add(erzeugeEintrag(2, 1));
        eintraege.add(erzeugeEintrag(2, 2));
        eintraege.add(erzeugeEintrag(2, 3));
        eintraege.add(erzeugeEintrag(2, 4));
        eintraege.add(erzeugeEintrag(3, 1));
        eintraege.add(erzeugeEintrag(3, 2));
        eintraege.add(erzeugeEintrag(3, 3));
        eintraege.add(erzeugeEintrag(3, 4));
        eintraege.add(erzeugeEintrag(4, 1));
        eintraege.add(erzeugeEintrag(4, 2));
        eintraege.add(erzeugeEintrag(4, 3));
        eintraege.add(erzeugeEintrag(4, 4));
        eintraege.add(erzeugeEintrag(5, 1));
        eintraege.add(erzeugeEintrag(5, 2));
        eintraege.add(erzeugeEintrag(5, 3));
        eintraege.add(erzeugeEintrag(101, 4));
        try {
            verteilregelgenerierung.generiereMitVorgabe(eintraege);
        } catch (final IllegalArgumentException e) {
            assertEquals("Die Einträge sind fehlerhaft.", e.getMessage());
        }
    }

    @Test
    public void testGeneriereMitVorgabeParameterKanalnummerFalsch() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final List<Eintrag> eintraege = new ArrayList<>();
        eintraege.add(erzeugeEintrag(1, 1));
        eintraege.add(erzeugeEintrag(1, 2));
        eintraege.add(erzeugeEintrag(1, 3));
        eintraege.add(erzeugeEintrag(1, 4));
        eintraege.add(erzeugeEintrag(2, 1));
        eintraege.add(erzeugeEintrag(2, 2));
        eintraege.add(erzeugeEintrag(2, 3));
        eintraege.add(erzeugeEintrag(2, 4));
        eintraege.add(erzeugeEintrag(3, 1));
        eintraege.add(erzeugeEintrag(3, 2));
        eintraege.add(erzeugeEintrag(3, 3));
        eintraege.add(erzeugeEintrag(3, 4));
        eintraege.add(erzeugeEintrag(4, 1));
        eintraege.add(erzeugeEintrag(4, 2));
        eintraege.add(erzeugeEintrag(4, 3));
        eintraege.add(erzeugeEintrag(4, 4));
        eintraege.add(erzeugeEintrag(5, 1));
        eintraege.add(erzeugeEintrag(5, 2));
        eintraege.add(erzeugeEintrag(5, 3));
        eintraege.add(erzeugeEintrag(5, 5));
        try {
            verteilregelgenerierung.generiereMitVorgabe(eintraege);
        } catch (final IllegalArgumentException e) {
            assertEquals("Die Einträge sind fehlerhaft.", e.getMessage());
        }
    }

    @Test
    public void testGeneriereMitVorgabe() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final byte[] konfiguration = verteilregelgenerierung.generiereMitVorgabe(erzeugeEintraege());
        checkErgebnisMitVorgabe(konfiguration);
    }

    @Test
    public void testGeneriereOutputStreamParameterNull() throws IOException {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        try {
            verteilregelgenerierung.generiere(null);
        } catch (final IllegalArgumentException e) {
            assertEquals(FEHLER_PARAMETER_NULL, e.getMessage());
        }
    }

    @Test
    public void testGeneriereOutputStream() throws IOException {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        verteilregelgenerierung.generiere(outputStream);
        checkErgebnis(outputStream.toByteArray());
    }

    @Test
    public void testKonvertiereEintraegeParameterNull() {
        try {
            Verteilregelgenerierung.konvertiereEintraege(null);
            fail();
        } catch (final IllegalArgumentException e) {
            assertEquals(FEHLER_PARAMETER_NULL, e.getMessage());
        }
    }

    @Test
    public void testKonvertiereEintraege() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final byte[] verteilregeln = verteilregelgenerierung.generiere();
        final List<Eintrag> eintraege = Verteilregelgenerierung.konvertiereEintraege(verteilregeln);
        assertNotNull(eintraege);
        assertEquals(ANZAHL_EINTRAEGE, eintraege.size());
    }

    @Test
    public void testKonvertiereEintraegeAlternativ() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfigurationAlternativ();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final byte[] verteilregeln = verteilregelgenerierung.generiere();
        final List<Eintrag> eintraege = Verteilregelgenerierung.konvertiereEintraege(verteilregeln);
        assertNotNull(eintraege);
        assertEquals(200, eintraege.size());
    }

    @Test
    public void testErmittleAnzahlPositionenParameterNull() {
        try {
            Verteilregelgenerierung.ermittleAnzahlPositionen(null);
            fail();
        } catch (final IllegalArgumentException e) {
            assertEquals(FEHLER_PARAMETER_NULL, e.getMessage());
        }
    }

    @Test
    public void testErmittleAnzahlPositionen() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final byte[] verteilregeln = verteilregelgenerierung.generiere();
        final AnzahlPositionen anzahlPositionen = Verteilregelgenerierung.ermittleAnzahlPositionen(verteilregeln);
        assertNotNull(anzahlPositionen);
        assertEquals(BLOCKGROESSE, anzahlPositionen.get().intValue());
    }

    @Test
    public void testErmittleAnzahlNutzdatenParameterNull() {
        try {
            Verteilregelgenerierung.ermittleAnzahlNutzdaten(null);
            fail();
        } catch (final IllegalArgumentException e) {
            assertEquals(FEHLER_PARAMETER_NULL, e.getMessage());
        }
    }

    @Test
    public void testErmittleAnzahlNutzdaten() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final byte[] verteilregeln = verteilregelgenerierung.generiere();
        final AnzahlNutzdaten anzahlNutzdaten = Verteilregelgenerierung.ermittleAnzahlNutzdaten(verteilregeln);
        assertNotNull(anzahlNutzdaten);
        assertEquals(ANZAHL_NUTZDATEN, anzahlNutzdaten.get().intValue());
    }

    @Test
    public void testErmittleAnzahlKanaeleParameterNull() {
        try {
            Verteilregelgenerierung.ermittleAnzahlKanaele(null);
            fail();
        } catch (final IllegalArgumentException e) {
            assertEquals(FEHLER_PARAMETER_NULL, e.getMessage());
        }
    }

    @Test
    public void testErmittleAnzahlKanaele() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final byte[] verteilregeln = verteilregelgenerierung.generiere();
        final AnzahlKanaele anzahlKanaele = Verteilregelgenerierung.ermittleAnzahlKanaele(verteilregeln);
        assertNotNull(anzahlKanaele);
        assertEquals(ANZAHL_KANAELE, anzahlKanaele.get().intValue());
    }

    @Test
    public void testErmittleBittiefeParameterNull() {
        try {
            Verteilregelgenerierung.ermittleBittiefe(null);
            fail();
        } catch (final IllegalArgumentException e) {
            assertEquals(FEHLER_PARAMETER_NULL, e.getMessage());
        }
    }

    @Test
    public void testErmittleBittiefe() {
        final KonfigurationVerteilregeln konfigurationGenerierung = erzeugeKonfiguration();
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final byte[] verteilregeln = verteilregelgenerierung.generiere();
        final Bittiefe bittiefe = Verteilregelgenerierung.ermittleBittiefe(verteilregeln);
        assertNotNull(bittiefe);
        assertEquals(BITTIEFE, bittiefe.get().intValue());
    }

    private KonfigurationVerteilregeln erzeugeKonfiguration() {
        final Blockgroesse blockGroesse = new Blockgroesse(BLOCKGROESSE);
        final AnzahlNutzdaten nutzdaten = new AnzahlNutzdaten(ANZAHL_NUTZDATEN);
        final AnzahlKanaele anzahlKanaele = new AnzahlKanaele(ANZAHL_KANAELE);
        final Bittiefe bittiefe = new Bittiefe(BITTIEFE);
        return new KonfigurationVerteilregeln(blockGroesse, nutzdaten, bittiefe, anzahlKanaele);
    }

    private KonfigurationVerteilregeln erzeugeKonfigurationAlternativ() {
        final Blockgroesse blockGroesse = new Blockgroesse(1000);
        final AnzahlNutzdaten nutzdaten = new AnzahlNutzdaten(50);
        final AnzahlKanaele anzahlKanaele = new AnzahlKanaele(2);
        final Bittiefe bittiefe = new Bittiefe(2);
        return new KonfigurationVerteilregeln(blockGroesse, nutzdaten, bittiefe, anzahlKanaele);
    }

    private KonfigurationVerteilregeln erzeugeKonfiguration(int blockgroesse, int anzahlNutzdaten, int kanalanzahl,
            int tiefe) {
        final Blockgroesse blockGroesse = new Blockgroesse(blockgroesse);
        final AnzahlNutzdaten nutzdaten = new AnzahlNutzdaten(anzahlNutzdaten);
        final AnzahlKanaele anzahlKanaele = new AnzahlKanaele(kanalanzahl);
        final Bittiefe bittiefe = new Bittiefe(tiefe);
        return new KonfigurationVerteilregeln(blockGroesse, nutzdaten, bittiefe, anzahlKanaele);
    }

    private void checkErgebnis(byte[] ergebnis) {
        checkVersion(ergebnis);
        checkKonfigurationBlockgroesse(ergebnis);
        checkKonfigurationNutzdaten(ergebnis);
        checkKonfigurationAnzahlKanaele(ergebnis);
        checkKonfigurationBittiefe(ergebnis);
        checkKAnzahlVerteilung(ergebnis);
        checkVerteilung(ergebnis);
    }

    private void checkErgebnisMitVorgabe(byte[] ergebnis) {
        checkVersion(ergebnis);
        checkKonfigurationBlockgroesse(ergebnis);
        checkKonfigurationNutzdaten(ergebnis);
        checkKonfigurationAnzahlKanaele(ergebnis);
        checkKonfigurationBittiefe(ergebnis);
        checkKAnzahlVerteilung(ergebnis);
        checkVerteilungMitVorgabe(ergebnis);
    }

    private void checkErgebnis(byte[] ergebnis, int blockgroesse, int anzahlNutzdaten, byte kanalanzahl, byte tiefe) {
        checkVersion(ergebnis);
        checkKonfigurationBlockgroesse(ergebnis, blockgroesse);
        checkKonfigurationNutzdaten(ergebnis, anzahlNutzdaten);
        checkKonfigurationAnzahlKanaele(ergebnis, kanalanzahl);
        checkKonfigurationBittiefe(ergebnis, tiefe);
        int anzahlBytesVerteilung = anzahlNutzdaten * GROESSE_EINTRAG * (8 / tiefe);
        checkKAnzahlVerteilung(ergebnis, anzahlBytesVerteilung);
        checkVerteilung(ergebnis, anzahlBytesVerteilung, blockgroesse, anzahlNutzdaten, kanalanzahl, tiefe);
    }

    private void checkVersion(byte[] ergebnis) {
        assertEquals(0x1, ergebnis[0]);
        assertEquals(0x0, ergebnis[1]);
        assertEquals(0x0, ergebnis[2]);
    }

    private void checkKonfigurationBlockgroesse(byte[] ergebnis) {
        checkInt(ergebnis, BLOCKGROESSE, 3);
    }

    private void checkKonfigurationBlockgroesse(byte[] ergebnis, int vergleich) {
        checkInt(ergebnis, vergleich, 3);
    }

    private void checkKonfigurationNutzdaten(byte[] ergebnis) {
        checkInt(ergebnis, ANZAHL_NUTZDATEN, 7);
    }

    private void checkKonfigurationNutzdaten(byte[] ergebnis, int vergleich) {
        checkInt(ergebnis, vergleich, 7);
    }

    private void checkKonfigurationAnzahlKanaele(byte[] ergebnis) {
        checkByte(ergebnis, ANZAHL_KANAELE, 11);
    }

    private void checkKonfigurationAnzahlKanaele(byte[] ergebnis, byte vergleich) {
        checkByte(ergebnis, vergleich, 11);
    }

    private void checkKonfigurationBittiefe(byte[] ergebnis) {
        checkByte(ergebnis, BITTIEFE, 12);
    }

    private void checkKonfigurationBittiefe(byte[] ergebnis, byte vergleich) {
        checkByte(ergebnis, vergleich, 12);
    }

    private void checkKAnzahlVerteilung(byte[] ergebnis) {
        checkInt(ergebnis, ANZAHL_BYTES_VERTEILUNG, 13);
    }

    private void checkKAnzahlVerteilung(byte[] ergebnis, int vergleich) {
        checkInt(ergebnis, vergleich, 13);
    }

    private void checkVerteilung(byte[] ergebnis) {
        assertEquals(ANZAHL_BYTES_VERTEILUNG, ergebnis.length - 17);
        int offset = 17;
        final byte[] eintrag = new byte[GROESSE_EINTRAG];
        final int anzahlEintraege = ANZAHL_NUTZDATEN * (8 / BITTIEFE);
        for (int index = 0; index < anzahlEintraege; index++) {
            System.arraycopy(ergebnis, offset, eintrag, 0, eintrag.length);
            final byte[] position = new byte[4];
            System.arraycopy(eintrag, 0, position, 0, position.length);
            final int zahl = ByteUtils.bytesToInt(position);
            assertTrue(zahl > 0 && zahl <= BLOCKGROESSE);
            final int kanal = eintrag[eintrag.length - 1];
            assertTrue(kanal > 0 && kanal <= ANZAHL_KANAELE);
            offset += GROESSE_EINTRAG;
        }
    }

    private void checkVerteilung(byte[] ergebnis, int anzahlBytesVerteilung, int blockgroesse, int anzahlNutzdaten,
            int anzahlKanaele, int bittiefe) {
        assertEquals(anzahlBytesVerteilung, ergebnis.length - 17);
        int offset = 17;
        final byte[] eintrag = new byte[GROESSE_EINTRAG];
        final int anzahlEintraege = anzahlNutzdaten * (8 / bittiefe);
        for (int index = 0; index < anzahlEintraege; index++) {
            System.arraycopy(ergebnis, offset, eintrag, 0, eintrag.length);
            final byte[] position = new byte[4];
            System.arraycopy(eintrag, 0, position, 0, position.length);
            final int zahl = ByteUtils.bytesToInt(position);
            assertTrue(zahl > 0 && zahl <= blockgroesse);
            final int kanal = eintrag[eintrag.length - 1];
            assertTrue(kanal > 0 && kanal <= anzahlKanaele);
            offset += GROESSE_EINTRAG;
        }
    }

    private void checkVerteilungMitVorgabe(byte[] ergebnis) {
        assertEquals(ANZAHL_BYTES_VERTEILUNG, ergebnis.length - 17);
        int offset = 17;
        final byte[] eintrag = new byte[GROESSE_EINTRAG];
        final int anzahlEintraege = ANZAHL_NUTZDATEN * (8 / BITTIEFE);
        final List<Eintrag> eintraegeVergleich = new ArrayList<>();
        for (int index = 0; index < anzahlEintraege; index++) {
            System.arraycopy(ergebnis, offset, eintrag, 0, eintrag.length);
            final byte[] position = new byte[4];
            System.arraycopy(eintrag, 0, position, 0, position.length);
            final int zahl = ByteUtils.bytesToInt(position);
            assertTrue(zahl > 0 && zahl <= BLOCKGROESSE);
            final int kanal = eintrag[eintrag.length - 1];
            assertTrue(kanal > 0 && kanal <= ANZAHL_KANAELE);
            offset += GROESSE_EINTRAG;
            final Eintrag eintragVergleich = new Eintrag(new Positionsnummer(zahl), new Kanalnummer(kanal));
            eintraegeVergleich.add(eintragVergleich);
        }
        final List<Eintrag> eintraegeErwartet = erzeugeEintraege();
        assertEquals(eintraegeErwartet.size(), eintraegeVergleich.size());
        for (final Eintrag eintragZumVergleich : eintraegeVergleich) {
            assertTrue(eintraegeErwartet.contains(eintragZumVergleich));
        }
    }

    private void checkInt(byte[] ergebnis, int menge, int startposition) {
        final byte[] bytesErwartung = ByteUtils.intToBytes(menge);
        final byte[] bytesErgebnis = new byte[bytesErwartung.length];
        System.arraycopy(ergebnis, startposition, bytesErgebnis, 0, bytesErgebnis.length);
        assertArrayEquals(bytesErwartung, bytesErgebnis);
    }

    private void checkByte(byte[] ergebnis, byte wert, int startposition) {
        assertEquals(wert, ergebnis[startposition]);
    }

    private Eintrag erzeugeEintrag(int positionsnummer, int kanalnummer) {
        return new Eintrag(new Positionsnummer(positionsnummer), new Kanalnummer(kanalnummer));
    }

    private List<Eintrag> erzeugeEintraege() {
        final List<Eintrag> eintraege = new ArrayList<>();
        eintraege.add(erzeugeEintrag(1, 1));
        eintraege.add(erzeugeEintrag(1, 2));
        eintraege.add(erzeugeEintrag(1, 3));
        eintraege.add(erzeugeEintrag(1, 4));
        eintraege.add(erzeugeEintrag(2, 1));
        eintraege.add(erzeugeEintrag(2, 2));
        eintraege.add(erzeugeEintrag(2, 3));
        eintraege.add(erzeugeEintrag(2, 4));
        eintraege.add(erzeugeEintrag(3, 1));
        eintraege.add(erzeugeEintrag(3, 2));
        eintraege.add(erzeugeEintrag(3, 3));
        eintraege.add(erzeugeEintrag(3, 4));
        eintraege.add(erzeugeEintrag(4, 1));
        eintraege.add(erzeugeEintrag(4, 2));
        eintraege.add(erzeugeEintrag(4, 3));
        eintraege.add(erzeugeEintrag(4, 4));
        eintraege.add(erzeugeEintrag(5, 1));
        eintraege.add(erzeugeEintrag(5, 2));
        eintraege.add(erzeugeEintrag(5, 3));
        eintraege.add(erzeugeEintrag(5, 4));
        return eintraege;
    }
}
