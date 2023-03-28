package de.platen.steganograph;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Breite;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Hoehe;
import de.platen.steganograph.datentypen.PositionXY;
import de.platen.steganograph.datentypen.X;
import de.platen.steganograph.datentypen.Y;
import de.platen.steganograph.uniformat.UniFormatBild;
import de.platen.steganograph.uniformat.UniFormatBildFarbe;
import de.platen.steganograph.uniformat.UniFormatBildGrau;
import de.platen.steganograph.utils.Bildpunktposition;
import de.platen.steganograph.utils.ByteUtils;
import de.platen.steganograph.utils.DateiUtils;
import de.platen.steganograph.verteilregelgenerierung.Verteilregelgenerierung;

public class AktionHolenAusBild {

    private static final String FEHLER_PARAMETER_NULL = "Einer der Parameter ist null.";
    private static final String FEHLER_PARAMETER_LEER = "Einer der Parameter ist leer.";

    public void holeAusBild(String dateinameVerteilregel, String dateinameQuelle, String dateinameNutzdaten)
            throws IOException {
        holeAusBild(dateinameVerteilregel, dateinameQuelle, dateinameNutzdaten, null, null);
    }

    public void holeAusBild(String dateinameVerteilregel, String dateinameQuelle, String dateinameNutzdaten, String dateiPrivateKey, String passwort)
            throws IOException {
        pruefeParameter(dateinameVerteilregel, dateinameQuelle, dateinameNutzdaten);
        BufferedImage bufferedImage = DateiUtils.leseBild(dateinameQuelle);
        byte[] verteilregel;
        if ((dateiPrivateKey != null) && !dateiPrivateKey.isEmpty()) {
            verteilregel = DateiUtils.leseDatei(dateinameVerteilregel, dateiPrivateKey, passwort);
        } else {
            verteilregel = DateiUtils.leseDatei(dateinameVerteilregel);
        }
        List<Eintrag> eintraege = Verteilregelgenerierung.konvertiereEintraege(verteilregel);
        AnzahlPositionen anzahlPositionen = Verteilregelgenerierung.ermittleAnzahlPositionen(verteilregel);
        AnzahlNutzdaten anzahlNutzdaten = Verteilregelgenerierung.ermittleAnzahlNutzdaten(verteilregel);
        AnzahlKanaele anzahlKanaele = Verteilregelgenerierung.ermittleAnzahlKanaele(verteilregel);
        Bittiefe bittiefe = Verteilregelgenerierung.ermittleBittiefe(verteilregel);
        UniFormatBild uniFormatBild = null;
        if (bufferedImage.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            uniFormatBild = new UniFormatBildGrau(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
        } else {
            uniFormatBild = new UniFormatBildFarbe(anzahlPositionen, anzahlKanaele, bittiefe, eintraege);
        }
        PositionXY abPosition = new PositionXY(new X(0), new Y(0));
        byte[] startblock = leseBlock(bufferedImage, uniFormatBild, abPosition, anzahlPositionen.get());
        byte[] zahl = new byte[4];
        System.arraycopy(startblock, 0, zahl, 0, 4);
        int anzahlBytes = ByteUtils.bytesToInt(zahl);
        System.arraycopy(startblock, 4, zahl, 0, 4);
        int laengeDateiname = ByteUtils.bytesToInt(zahl);
        byte[] datei = new byte[laengeDateiname];
        System.arraycopy(startblock, 8, datei, 0, laengeDateiname);
        String dateinameAusBlock = new String(datei);
        String dateinameZiel = dateinameNutzdaten;
        File file = new File(dateinameNutzdaten);
        if (file.isDirectory()) {
            String trenner = "/";
            if (dateinameNutzdaten.endsWith("/")) {
                trenner = "";
            }
            if (dateinameNutzdaten.endsWith("\\")) {
                trenner = "";
            }
            dateinameZiel = dateinameNutzdaten + trenner + dateinameAusBlock;
        }
        byte[] nutzdaten = new byte[anzahlBytes];
        Breite breite = new Breite(bufferedImage.getWidth());
        Hoehe hoehe = new Hoehe(bufferedImage.getHeight());
        Bildpunktposition bildpunktposition = new Bildpunktposition(breite, hoehe);
        abPosition = bildpunktposition.ermittleNaechstenBildpunkt(abPosition, anzahlPositionen.get());
        int offset = 0;
        byte[] blockdaten = null;
        int maxAnzahl = anzahlNutzdaten.get();
        while (offset < anzahlBytes) {
            if ((offset + maxAnzahl) > anzahlBytes) {
                maxAnzahl = anzahlBytes - offset;
            }
            blockdaten = leseBlock(bufferedImage, uniFormatBild, abPosition, maxAnzahl);
            System.arraycopy(blockdaten, 0, nutzdaten, offset, blockdaten.length);
            abPosition = bildpunktposition.ermittleNaechstenBildpunkt(abPosition, anzahlPositionen.get());
            offset += maxAnzahl;
        }
        DateiUtils.schreibeDatei(dateinameZiel, nutzdaten);
    }

    private static void pruefeParameter(String dateinameVerteilregel, String dateinameQuelle,
            String dateinameNutzdaten) {
        if ((dateinameVerteilregel == null) || (dateinameQuelle == null) || (dateinameNutzdaten == null)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        if (dateinameVerteilregel.isEmpty() || dateinameQuelle.isEmpty() || dateinameNutzdaten.isEmpty()) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_LEER);
        }
    }

    private static byte[] leseBlock(BufferedImage bufferedImage, UniFormatBild uniFormatBild, PositionXY abPosition,
            int maximalAnzahl) {
        uniFormatBild.uebertrageBereichZuUniFormat(bufferedImage, abPosition);
        return uniFormatBild.holeNutzdaten(maximalAnzahl);
    }
}
