package de.platen.zufallszahlengenerator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Zufallszahlengenerator {

    private static final String FEHLER_BEREICH = "Bereich muss größer 0 sein.";
    private static final String FEHLER_PARAMETER_NULL = "Einer der Parameter ist null.";
    private static final String FEHLER_BEREICH_FALSCH = "Der Bereich ist nicht groß genug.";
    private static final String FEHLER_ANZAHLZUFALLSZAHL = "Die Anzahl der Zufallszahlen ist größer als der Bereich.";

    public Zufallszahlengenerator() {
    }

    public Set<Integer> erzeugeMengeZufallszahlen(AnzahlZufallszahlen anzahlZufallszahlen, BereichVon bereichVon,
            BereichBis bereichBis) {
        return Zufallszahlengenerator.erzeugeZufallszahlenmenge(anzahlZufallszahlen, bereichVon, bereichBis);
    }

    public static Set<Integer> erzeugeZufallszahlenmenge(AnzahlZufallszahlen anzahlZufallszahlen, BereichVon bereichVon,
            BereichBis bereichBis) {
        checkParameter(anzahlZufallszahlen, bereichVon, bereichBis);
        Set<Integer> zufallszahlen = new HashSet<>();
        SecureRandom secureRandom = null;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        int wertVon = bereichVon.get();
        int wertBis = bereichBis.get();
        List<Integer> indexVon = new ArrayList<>();
        List<Integer> indexBis = new ArrayList<>();
        int zufallswert = secureRandom.nextInt(wertBis - wertVon) + wertVon;
        zufallszahlen.add(zufallswert);
        if ((zufallswert != wertVon) && (zufallswert != (wertBis - 1))) {
            indexVon.add(wertVon);
            indexBis.add(zufallswert);
            indexVon.add(zufallswert + 1);
            indexBis.add(wertBis);
        } else {
            if (zufallswert == wertVon) {
                indexVon.add(wertVon + 1);
                indexBis.add(wertBis);
            }
            if (zufallswert == (wertBis - 1)) {
                indexVon.add(wertVon);
                indexBis.add(wertBis - 1);
            }
        }
        int index = 0;
        int merkVon1 = 0;
        int merkBis1 = 0;
        int merkVon2 = 0;
        int merkBis2 = 0;
        int von = 0;
        int bis = 0;
        while (zufallszahlen.size() < anzahlZufallszahlen.get()) {
            zufallswert = secureRandom.nextInt(indexVon.size());
            index = zufallswert;
            von = indexVon.get(index);
            bis = indexBis.get(index);
            if ((bis - von) > 1) {
                zufallswert = secureRandom.nextInt(bis - von) + von;
                zufallszahlen.add(zufallswert);
                if ((zufallswert != von) && (zufallswert != (bis - 1))) {
                    merkVon1 = von;
                    merkBis1 = zufallswert;
                    merkVon2 = zufallswert + 1;
                    merkBis2 = bis;
                    indexVon.add(merkVon1);
                    indexBis.add(merkBis1);
                    indexVon.add(merkVon2);
                    indexBis.add(merkBis2);
                } else {
                    if (zufallswert == von) {
                        indexVon.add(von + 1);
                        indexBis.add(bis);
                    }
                    if (zufallswert == (bis - 1)) {
                        indexVon.add(von);
                        indexBis.add(bis - 1);
                    }
                }
            } else {
                zufallszahlen.add(von);
                indexVon.remove(index);
                indexBis.remove(index);
            }
        }
        return zufallszahlen;
    }

    public static int erzeugeZufallszahl(int bereich) {
        if (bereich < 1) {
            throw new IllegalArgumentException(FEHLER_BEREICH);
        }
        SecureRandom secureRandom = null;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return secureRandom.nextInt(bereich);
    }

    private static void checkParameter(AnzahlZufallszahlen anzahlZufallszahlen, BereichVon bereichVon,
            BereichBis bereichBis) {
        if ((anzahlZufallszahlen == null) || (bereichVon == null) || (bereichBis == null)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        if (bereichBis.get() <= bereichVon.get()) {
            throw new IllegalArgumentException(FEHLER_BEREICH_FALSCH);
        }
        if ((bereichBis.get() - bereichVon.get()) <= anzahlZufallszahlen.get()) {
            throw new IllegalArgumentException(FEHLER_ANZAHLZUFALLSZAHL);
        }
    }
}
