package de.platen.steganograph.uniformat;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Kanalnummer;
import de.platen.steganograph.datentypen.Positionsinhalt;
import de.platen.steganograph.datentypen.Positionsnummer;

public class UniFormatTest {

    private static final int DATENLAENGE = 256;
    private static final int DATENLAENGE_WENIG = 256 / 2;
    private static final byte[] DATEN = new byte[DATENLAENGE];
    private static final byte[] DATEN_WENIG = new byte[DATENLAENGE_WENIG];

    static {
        for (int index = 0; index < DATENLAENGE; index++) {
            DATEN[index] = (byte) ((byte) index & (byte) 0xFF);
        }
        for (int index = 0; index < DATENLAENGE_WENIG; index++) {
            DATEN_WENIG[index] = (byte) ((byte) index & (byte) 0xFF);
        }
    }

    @Test
    public void testParameterNullAnzahlPositionen() {
        AnzahlPositionen anzahlPositionen = null;
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(4);
        Bittiefe bittiefe = new Bittiefe(2);
        List<Eintrag> eintraege = new ArrayList<>();
        try {
            new UniFormatZumTest(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testParameterNullAnzahlKanaele() {
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(100);
        AnzahlKanaele anzahlKanaele = null;
        Bittiefe bittiefe = new Bittiefe(2);
        List<Eintrag> eintraege = new ArrayList<>();
        try {
            new UniFormatZumTest(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testParameterNullBittiefe() {
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(100);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(4);
        Bittiefe bittiefe = null;
        List<Eintrag> eintraege = new ArrayList<>();
        try {
            new UniFormatZumTest(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testParameterNullEintraege() {
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(100);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(4);
        Bittiefe bittiefe = new Bittiefe(2);
        List<Eintrag> eintraege = null;
        try {
            new UniFormatZumTest(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testEintraegeUnpassendFalschePositionsnummer() {
        try {
            erzeugeUniFormatEintragFalschePositionsnummer();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Einträge passt nicht zu Anzahl Positionen und Anzahl Kanäle.", e.getMessage());
        }
    }

    @Test
    public void testEintraegeUnpassendFalscheKanalnummer() {
        try {
            erzeugeUniFormatEintragFalscheKanalnummer();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Einträge passt nicht zu Anzahl Positionen und Anzahl Kanäle.", e.getMessage());
        }
    }

    @Test
    public void testPositionsmengeUnpassend() {
        UniFormat uniFormat = erzeugeUniFormat(1);
        byte[] daten = new byte[1000];
        for (int index = 0; index < 1000; index++) {
            daten[index] = 0x00;
        }
        try {
            uniFormat.versteckeNutzdaten(daten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Die Eintragsmenge passt nicht zu Anzahl Positionen, Anzahl Kanäle und Bittiefe.",
                    e.getMessage());
        }
    }

    @Test
    public void testVersteckeUndHoleNutzdatenBytesBittiefe1() {
        UniFormat uniFormat = erzeugeUniFormat(1);
        uniFormat.versteckeNutzdaten(DATEN);
        assertTrue(Arrays.equals(DATEN, uniFormat.holeNutzdaten(DATEN.length)));
    }

    @Test
    public void testVersteckeUndHoleNutzdatenBytesBittiefe2() {
        UniFormat uniFormat = erzeugeUniFormat(2);
        uniFormat.versteckeNutzdaten(DATEN);
        assertArrayEquals(DATEN, uniFormat.holeNutzdaten(DATEN.length));
    }

    @Test
    public void testVersteckeUndHoleNutzdatenBytesBittiefe4() {
        UniFormat uniFormat = erzeugeUniFormat(4);
        uniFormat.versteckeNutzdaten(DATEN);
        assertArrayEquals(DATEN, uniFormat.holeNutzdaten(DATEN.length));
    }

    @Test
    public void testVersteckeUndHoleNutzdatenBytesBittiefe8() {
        UniFormat uniFormat = erzeugeUniFormat(8);
        uniFormat.versteckeNutzdaten(DATEN);
        assertArrayEquals(DATEN, uniFormat.holeNutzdaten(DATEN.length));
    }

    @Test
    public void testVersteckeUndHoleNutzdatenStreamBittiefe1() throws IOException {
        UniFormat uniFormat = erzeugeUniFormat(1);
        InputStream inputStream = new ByteArrayInputStream(DATEN);
        uniFormat.versteckeNutzdaten(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        uniFormat.holeNutzdaten(outputStream, DATEN.length);
        assertArrayEquals(DATEN, outputStream.toByteArray());
    }

    @Test
    public void testVersteckeUndHoleNutzdatenStreamBittiefe2() throws IOException {
        UniFormat uniFormat = erzeugeUniFormat(2);
        InputStream inputStream = new ByteArrayInputStream(DATEN);
        uniFormat.versteckeNutzdaten(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        uniFormat.holeNutzdaten(outputStream, DATEN.length);
        assertArrayEquals(DATEN, outputStream.toByteArray());
    }

    @Test
    public void testVersteckeUndHoleNutzdatenStreamBittiefe4() throws IOException {
        UniFormat uniFormat = erzeugeUniFormat(4);
        InputStream inputStream = new ByteArrayInputStream(DATEN);
        uniFormat.versteckeNutzdaten(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        uniFormat.holeNutzdaten(outputStream, DATEN.length);
        assertArrayEquals(DATEN, outputStream.toByteArray());
    }

    @Test
    public void testVersteckeUndHoleNutzdatenStreamBittiefe8() throws IOException {
        UniFormat uniFormat = erzeugeUniFormat(8);
        InputStream inputStream = new ByteArrayInputStream(DATEN);
        uniFormat.versteckeNutzdaten(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        uniFormat.holeNutzdaten(outputStream, DATEN.length);
        assertArrayEquals(DATEN, outputStream.toByteArray());
    }

    @Test
    public void testVersteckeUndHoleNutzdatenBytesWenigBittiefe1() {
        UniFormat uniFormat = erzeugeUniFormat(1);
        uniFormat.versteckeNutzdaten(DATEN_WENIG);
        assertArrayEquals(DATEN_WENIG, uniFormat.holeNutzdaten(DATEN_WENIG.length));
    }

    @Test
    public void testVersteckeUndHoleNutzdatenBytesWenigBittiefe2() {
        UniFormat uniFormat = erzeugeUniFormat(2);
        uniFormat.versteckeNutzdaten(DATEN_WENIG);
        assertArrayEquals(DATEN_WENIG, uniFormat.holeNutzdaten(DATEN_WENIG.length));
    }

    @Test
    public void testVersteckeUndHoleNutzdatenBytesWenigBittiefe4() {
        UniFormat uniFormat = erzeugeUniFormat(4);
        uniFormat.versteckeNutzdaten(DATEN_WENIG);
        assertArrayEquals(DATEN_WENIG, uniFormat.holeNutzdaten(DATEN_WENIG.length));
    }

    @Test
    public void testVersteckeUndHoleNutzdatenBytesWenigBittiefe8() {
        UniFormat uniFormat = erzeugeUniFormat(8);
        uniFormat.versteckeNutzdaten(DATEN_WENIG);
        assertArrayEquals(DATEN_WENIG, uniFormat.holeNutzdaten(DATEN_WENIG.length));
    }

    @Test
    public void testVersteckeUndHoleNutzdatenStreamWenigBittiefe1() throws IOException {
        UniFormat uniFormat = erzeugeUniFormat(1);
        InputStream inputStream = new ByteArrayInputStream(DATEN_WENIG);
        uniFormat.versteckeNutzdaten(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        uniFormat.holeNutzdaten(outputStream, DATEN_WENIG.length);
        assertArrayEquals(DATEN_WENIG, outputStream.toByteArray());
    }

    @Test
    public void testVersteckeUndHoleNutzdatenStreamWenigBittiefe2() throws IOException {
        UniFormat uniFormat = erzeugeUniFormat(2);
        InputStream inputStream = new ByteArrayInputStream(DATEN_WENIG);
        uniFormat.versteckeNutzdaten(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        uniFormat.holeNutzdaten(outputStream, DATEN_WENIG.length);
        assertArrayEquals(DATEN_WENIG, outputStream.toByteArray());
    }

    @Test
    public void testVersteckeUndHoleNutzdatenStreamWenigBittiefe4() throws IOException {
        UniFormat uniFormat = erzeugeUniFormat(4);
        InputStream inputStream = new ByteArrayInputStream(DATEN_WENIG);
        uniFormat.versteckeNutzdaten(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        uniFormat.holeNutzdaten(outputStream, DATEN_WENIG.length);
        assertArrayEquals(DATEN_WENIG, outputStream.toByteArray());
    }

    @Test
    public void testVersteckeUndHoleNutzdatenStreamWenigBittiefe8() throws IOException {
        UniFormat uniFormat = erzeugeUniFormat(8);
        InputStream inputStream = new ByteArrayInputStream(DATEN_WENIG);
        uniFormat.versteckeNutzdaten(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        uniFormat.holeNutzdaten(outputStream, DATEN_WENIG.length);
        assertArrayEquals(DATEN_WENIG, outputStream.toByteArray());
    }

    @Test
    public void testVerrauscheBittiefe1() {
        UniFormatZumTest uniFormat = erzeugeUniFormat(1);
        uniFormat.verrausche();
        uniFormat.checkPositionsinhalte(1);
    }

    @Test
    public void testVerrauscheBittiefe2() {
        UniFormatZumTest uniFormat = erzeugeUniFormat(2);
        uniFormat.verrausche();
        uniFormat.checkPositionsinhalte(2);
    }

    @Test
    public void testVerrauscheBittiefe4() {
        UniFormatZumTest uniFormat = erzeugeUniFormat(4);
        uniFormat.verrausche();
        uniFormat.checkPositionsinhalte(4);
    }

    @Test
    public void testVerrauscheBittiefe8() {
        UniFormatZumTest uniFormat = erzeugeUniFormat(8);
        uniFormat.verrausche();
        uniFormat.checkPositionsinhalte(8);
    }

    private UniFormatZumTest erzeugeUniFormat(int wertBittiefe) {
        int positionsanzahl = 512;
        int kanalanzahl = 4;
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(positionsanzahl);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(kanalanzahl);
        Bittiefe bittiefe = new Bittiefe(wertBittiefe);
        int anzahlEintraege = DATENLAENGE * (8 / wertBittiefe);
        List<Eintrag> eintraege = new ArrayList<>();
        int indexEintrag = 0;
        int indexPosition = 1;
        int indexKanal = 1;
        while (indexEintrag < anzahlEintraege) {
            Positionsnummer positionsnummer = new Positionsnummer(indexPosition);
            Kanalnummer kanalnummer = new Kanalnummer(indexKanal);
            Eintrag eintrag = new Eintrag(positionsnummer, kanalnummer);
            eintraege.add(eintrag);
            if (indexKanal < kanalanzahl) {
                indexKanal++;
            } else {
                indexKanal = 1;
                indexPosition++;
            }
            indexEintrag++;
        }
        return new UniFormatZumTest(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
    }

    private UniFormat erzeugeUniFormatEintragFalscheKanalnummer() {
        int positionsanzahl = 512;
        int kanalanzahl = 4;
        int wertBittiefe = 2;
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(positionsanzahl);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(kanalanzahl - 1);
        Bittiefe bittiefe = new Bittiefe(wertBittiefe);
        int anzahlEintraege = DATENLAENGE * (8 / wertBittiefe);
        List<Eintrag> eintraege = new ArrayList<>();
        int indexEintrag = 0;
        int indexPosition = 1;
        int indexKanal = 1;
        while (indexEintrag < anzahlEintraege) {
            Positionsnummer positionsnummer = new Positionsnummer(indexPosition);
            Kanalnummer kanalnummer = new Kanalnummer(indexKanal);
            Eintrag eintrag = new Eintrag(positionsnummer, kanalnummer);
            eintraege.add(eintrag);
            if (indexKanal < kanalanzahl) {
                indexKanal++;
            } else {
                indexKanal = 1;
                indexPosition++;
            }
            indexEintrag++;
        }
        return new UniFormatZumTest(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
    }

    private UniFormat erzeugeUniFormatEintragFalschePositionsnummer() {
        int positionsanzahl = 512;
        int kanalanzahl = 4;
        int wertBittiefe = 2;
        AnzahlPositionen anzahlPositionen = new AnzahlPositionen(positionsanzahl - 500);
        AnzahlKanaele anzahlKanaele = new AnzahlKanaele(kanalanzahl);
        Bittiefe bittiefe = new Bittiefe(wertBittiefe);
        int anzahlEintraege = DATENLAENGE * (8 / wertBittiefe);
        List<Eintrag> eintraege = new ArrayList<>();
        int indexEintrag = 0;
        int indexPosition = 1;
        int indexKanal = 1;
        while (indexEintrag < anzahlEintraege) {
            Positionsnummer positionsnummer = new Positionsnummer(indexPosition);
            Kanalnummer kanalnummer = new Kanalnummer(indexKanal);
            Eintrag eintrag = new Eintrag(positionsnummer, kanalnummer);
            eintraege.add(eintrag);
            if (indexKanal < kanalanzahl) {
                indexKanal++;
            } else {
                indexKanal = 1;
                indexPosition++;
            }
            indexEintrag++;
        }
        return new UniFormatZumTest(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
    }

    private class UniFormatZumTest extends UniFormat {

        public UniFormatZumTest(AnzahlPositionen anzahlPositionen, AnzahlKanaele anzahlKanaele, Bittiefe bittiefe,
                List<Eintrag> eintraege) {
            super(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
            for (int indexPosition = 1; indexPosition <= anzahlPositionen.get(); indexPosition++) {
                Positionsinhalt positionsinhalt = new Positionsinhalt(anzahlKanaele);
                for (int indexKanal = 1; indexKanal <= anzahlKanaele.get(); indexKanal++) {
                    positionsinhalt.setzeWert(new Kanalnummer(indexKanal), 0);
                }
                positionsinhalte.put(new Positionsnummer(indexPosition), positionsinhalt);
            }
        }

        public void checkPositionsinhalte(int bittiefe) {
            for (int indexPosition = 1; indexPosition <= anzahlPositionen.get(); indexPosition++) {
                Positionsinhalt positionsinhalt = positionsinhalte.get(new Positionsnummer(indexPosition));
                for (int indexKanal = 1; indexKanal <= anzahlKanaele.get(); indexKanal++) {
                    checkBereich(positionsinhalt.holeWert(new Kanalnummer(indexKanal)), bittiefe);
                }
            }
        }

        private void checkBereich(int wert, int bittiefe) {
            assertFalse(wert < 0);
            switch (bittiefe) {
            case 1:
                assertTrue(wert <= 1);
                break;
            case 2:
                assertTrue(wert <= 3);
                break;
            case 4:
                assertTrue(wert <= 15);
                break;
            case 8:
                assertTrue(wert <= 255);
                break;
            default:
                break;
            }
        }
    }
}
