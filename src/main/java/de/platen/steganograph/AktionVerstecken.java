package de.platen.steganograph;

import java.awt.image.BufferedImage;
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
import de.platen.steganograph.uniformat.UniformatBild4ByteABGR;
import de.platen.steganograph.utils.Bildpunktposition;
import de.platen.steganograph.utils.ByteUtils;
import de.platen.steganograph.verteilregelgenerierung.Verteilregelgenerierung;

public class AktionVerstecken {

    private static final String FEHLER_PARAMETER = "Ein oder mehrere Parameter sind null oder fehlerhaft.";
    private static final String FEHLER_DATENMENGE = "Es können nicht alle Nutzdaten im Bild untergebracht werden.";
    private static final String FEHLER_BLOCK = "Es können nicht alle Daten im Block untergebracht werden.";

    public static void versteckeNutzdaten(String dateinameNutzdaten, BufferedImage bufferedImageQuelle,
            BufferedImage bufferedImageZiel, byte[] verteilregel, byte[] nutzdaten) {
        pruefeParameter(dateinameNutzdaten, bufferedImageQuelle, bufferedImageZiel, verteilregel, nutzdaten);
        List<Eintrag> eintraege = Verteilregelgenerierung.konvertiereEintraege(verteilregel);
        AnzahlPositionen anzahlPositionen = Verteilregelgenerierung.ermittleAnzahlPositionen(verteilregel);
        AnzahlNutzdaten anzahlNutzdaten = Verteilregelgenerierung.ermittleAnzahlNutzdaten(verteilregel);
        AnzahlKanaele anzahlKanaele = Verteilregelgenerierung.ermittleAnzahlKanaele(verteilregel);
        Bittiefe bittiefe = Verteilregelgenerierung.ermittleBittiefe(verteilregel);
        int anzahlBloecke = (bufferedImageQuelle.getWidth() * bufferedImageQuelle.getHeight()) / anzahlPositionen.get();
        if (((anzahlBloecke - 1) * anzahlNutzdaten.get()) < nutzdaten.length) {
            throw new RuntimeException(FEHLER_DATENMENGE);
        }
        UniformatBild4ByteABGR uniFormatBild = new UniformatBild4ByteABGR(anzahlPositionen, anzahlKanaele, bittiefe,
                eintraege);
        PositionXY abPosition = new PositionXY(new X(0), new Y(0));
        versteckeStartblock(nutzdaten.length, anzahlNutzdaten, dateinameNutzdaten, uniFormatBild, abPosition,
                anzahlPositionen, bufferedImageZiel);
        Bildpunktposition bildpunktposition = new Bildpunktposition(new Breite(bufferedImageQuelle.getWidth()),
                new Hoehe(bufferedImageQuelle.getHeight()));
        if (bildpunktposition.ermittleNaechstenBildpunkt(abPosition, anzahlPositionen.get()) != null) {
            abPosition = bildpunktposition.ermittleNaechstenBildpunkt(abPosition, anzahlPositionen.get());
        } else {
            throw new RuntimeException(FEHLER_DATENMENGE);
        }
        if (nutzdaten.length <= anzahlNutzdaten.get()) {
            versteckeBlock(bufferedImageZiel, uniFormatBild, abPosition, nutzdaten);
        } else {
            versteckeBloecke(bufferedImageZiel, nutzdaten, anzahlPositionen, anzahlNutzdaten, uniFormatBild, abPosition,
                    bildpunktposition);
        }
    }

    private static void versteckeStartblock(int anzahlNutzdaten, AnzahlNutzdaten anzahlNutzdatenBlock, String dateiname,
            UniformatBild4ByteABGR uniFormatBild, PositionXY abPosition, AnzahlPositionen anzahlPositionen,
            BufferedImage bufferedImage) {
        byte[] anzahl = ByteUtils.intToBytes(anzahlNutzdaten);
        String datei = dateiname;
        int index1 = dateiname.lastIndexOf("/");
        int index2 = dateiname.lastIndexOf("\\");
        if ((index1 != -1) && (index2 != -1)) {
            if (index1 > index2) {
                datei = dateiname.substring(index1 + 1);
            } else {
                datei = dateiname.substring(index2 + 1);
            }
        } else {
            if (index1 != -1) {
                datei = dateiname.substring(index1 + 1);
            }
            if (index2 != -1) {
                datei = dateiname.substring(index2 + 1);
            }
        }
        byte[] laenge = ByteUtils.intToBytes(datei.length());
        byte[] dateinameArray = datei.getBytes();
        byte[] daten = new byte[anzahl.length + laenge.length + dateinameArray.length];
        if (anzahlNutzdatenBlock.get() < daten.length) {
            throw new RuntimeException(FEHLER_BLOCK);
        }
        System.arraycopy(anzahl, 0, daten, 0, anzahl.length);
        System.arraycopy(laenge, 0, daten, 4, laenge.length);
        System.arraycopy(dateinameArray, 0, daten, 8, dateinameArray.length);
        versteckeBlock(bufferedImage, uniFormatBild, abPosition, daten);
    }

    private static void versteckeBlock(BufferedImage bufferedImage, UniformatBild4ByteABGR uniFormatBild,
            PositionXY abPosition, byte[] nutzdatenblock) {
        uniFormatBild.uebertrageBereichZuUniFormat(bufferedImage, abPosition);
        uniFormatBild.verrausche();
        uniFormatBild.versteckeNutzdaten(nutzdatenblock);
        uniFormatBild.uebertrageBereichVonUniFormat(bufferedImage, abPosition);
    }

    private static void versteckeBloecke(BufferedImage bufferedImageZiel, byte[] nutzdaten,
            AnzahlPositionen anzahlPositionen, AnzahlNutzdaten anzahlNutzdaten, UniformatBild4ByteABGR uniFormatBild,
            PositionXY abPosition, Bildpunktposition bildpunktposition) {
        int laenge = anzahlNutzdaten.get();
        byte[] nutzdatenblock = new byte[laenge];
        boolean ende = false;
        int offset = 0;
        while (!ende) {
            System.arraycopy(nutzdaten, offset, nutzdatenblock, 0, laenge);
            versteckeBlock(bufferedImageZiel, uniFormatBild, abPosition, nutzdatenblock);
            if (laenge == anzahlNutzdaten.get()) {
                offset += laenge;
                if (bildpunktposition.ermittleNaechstenBildpunkt(abPosition, laenge) != null) {
                    abPosition = bildpunktposition.ermittleNaechstenBildpunkt(abPosition, anzahlPositionen.get());
                } else {
                    ende = true;
                }
                if ((offset + laenge) > nutzdaten.length) {
                    laenge = nutzdaten.length - offset;
                    nutzdatenblock = new byte[laenge];
                }
            } else {
                ende = true;
            }
        }
    }

    private static void pruefeParameter(String dateinameNutzdaten, BufferedImage bufferedImageQuelle,
            BufferedImage bufferedImageZiel, byte[] verteilregel, byte[] nutzdaten) {
        if ((dateinameNutzdaten == null) || (bufferedImageQuelle == null) || (bufferedImageZiel == null)
                || (verteilregel == null) || (nutzdaten == null)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER);
        }
        if (dateinameNutzdaten.isEmpty() || (bufferedImageQuelle.getType() != bufferedImageZiel.getType())
                || (bufferedImageQuelle.getHeight() != bufferedImageZiel.getHeight())
                || (bufferedImageQuelle.getWidth() != bufferedImageZiel.getWidth()) || (verteilregel.length == 0)
                || (nutzdaten.length == 0)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER);
        }
    }
}
