package de.platen.steganograph;

import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.utils.ByteUtils;

public class AktionUtil {

    private static final String FEHLER_PARAMETER = "Parameter falsch.";
    private static final String FEHLER_BLOCK = "Es können nicht alle Daten im Block untergebracht werden.";

    public static byte[] bereiteStartblockdaten(int anzahlNutzdaten, AnzahlNutzdaten anzahlNutzdatenBlock,
            String dateiname) {
        pruefeParameter(anzahlNutzdaten, anzahlNutzdatenBlock, dateiname);
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
        return daten;
    }

    private static void pruefeParameter(int anzahlNutzdaten, AnzahlNutzdaten anzahlNutzdatenBlock, String dateiname) {
        if (anzahlNutzdaten < 1) {
            throw new IllegalArgumentException(FEHLER_PARAMETER);
        }
        if ((anzahlNutzdatenBlock == null) || (dateiname == null)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER);
        }
        if (dateiname.isEmpty()) {
            throw new IllegalArgumentException(FEHLER_PARAMETER);
        }
    }
}
