package de.platen.steganograph;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Blockgroesse;
import de.platen.steganograph.datentypen.Breite;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Hoehe;
import de.platen.steganograph.datentypen.PositionXY;
import de.platen.steganograph.datentypen.X;
import de.platen.steganograph.datentypen.Y;
import de.platen.steganograph.uniformat.UniFormatBild4ByteABGR;
import de.platen.steganograph.utils.Bildpunktposition;
import de.platen.steganograph.utils.ByteUtils;
import de.platen.steganograph.utils.DateiUtils;
import de.platen.steganograph.verteilregelgenerierung.KonfigurationVerteilregeln;
import de.platen.steganograph.verteilregelgenerierung.Verteilregelgenerierung;

public class Aktionen {

    public void generiere(String blockgroesse, String anzahlNutzdaten, String anzahlKanaele, String bittiefe,
            String dateiname) throws IOException {
        Blockgroesse blockgroesseKonfiguration = new Blockgroesse(Integer.valueOf(blockgroesse));
        AnzahlNutzdaten nutzdatenKonfiguration = new AnzahlNutzdaten(Integer.valueOf(anzahlNutzdaten));
        AnzahlKanaele anzahlKanaeleKonfiguration = new AnzahlKanaele(Integer.valueOf(anzahlKanaele));
        Bittiefe bittiefeKonfiguration = new Bittiefe(Integer.valueOf(bittiefe));
        KonfigurationVerteilregeln konfigurationGenerierung = new KonfigurationVerteilregeln(blockgroesseKonfiguration,
                nutzdatenKonfiguration, bittiefeKonfiguration, anzahlKanaeleKonfiguration);
        Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        byte[] daten = verteilregelgenerierung.generiere();
        DateiUtils.schreibeDatei(dateiname, daten);
    }

    public void verstecke(String dateinameVerteilregel, String dateinameNutzdaten, String dateinameQuelle,
            String dateinameZiel, Verrauschoption verrauschoption) throws IOException {
        BufferedImage bufferedImageQuelle = DateiUtils.leseBild(dateinameQuelle);
        BufferedImage bufferedImageZiel = kopiereBild(bufferedImageQuelle);
        byte[] verteilregel = DateiUtils.leseDatei(dateinameVerteilregel);
        byte[] nutzdaten = DateiUtils.leseDatei(dateinameNutzdaten);
        AktionVerstecken.versteckeNutzdaten(dateinameNutzdaten, bufferedImageQuelle, bufferedImageZiel, verteilregel,
                nutzdaten, verrauschoption);
        DateiUtils.schreibeBild(dateinameZiel, bufferedImageZiel);
    }

    public void hole(String dateinameVerteilregel, String dateinameQuelle, String dateinameNutzdaten)
            throws IOException {
        BufferedImage bufferedImage = DateiUtils.leseBild(dateinameQuelle);
        byte[] verteilregel = DateiUtils.leseDatei(dateinameVerteilregel);
        List<Eintrag> eintraege = Verteilregelgenerierung.konvertiereEintraege(verteilregel);
        AnzahlPositionen anzahlPositionen = Verteilregelgenerierung.ermittleAnzahlPositionen(verteilregel);
        AnzahlNutzdaten anzahlNutzdaten = Verteilregelgenerierung.ermittleAnzahlNutzdaten(verteilregel);
        AnzahlKanaele anzahlKanaele = Verteilregelgenerierung.ermittleAnzahlKanaele(verteilregel);
        Bittiefe bittiefe = Verteilregelgenerierung.ermittleBittiefe(verteilregel);
        UniFormatBild4ByteABGR uniFormatBild = new UniFormatBild4ByteABGR(anzahlPositionen, anzahlKanaele, bittiefe,
                eintraege);
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

    private static BufferedImage kopiereBild(BufferedImage bufferedImage) {
        BufferedImage bufferedImageKopie = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                bufferedImage.getType());
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                bufferedImageKopie.setRGB(x, y, bufferedImage.getRGB(x, x));
            }
        }
        return bufferedImageKopie;
    }

    private static byte[] leseBlock(BufferedImage bufferedImage, UniFormatBild4ByteABGR uniFormatBild,
            PositionXY abPosition, int maximalAnzahl) {
        uniFormatBild.uebertrageBereichZuUniFormat(bufferedImage, abPosition);
        return uniFormatBild.holeNutzdaten(maximalAnzahl);
    }
}
