package de.platen.steganograph.verteilregelgenerierung;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Kanalnummer;
import de.platen.steganograph.datentypen.Positionsnummer;
import de.platen.steganograph.utils.ByteUtils;
import de.platen.zufallszahlengenerator.AnzahlZufallszahlen;
import de.platen.zufallszahlengenerator.BereichBis;
import de.platen.zufallszahlengenerator.BereichVon;
import de.platen.zufallszahlengenerator.Zufallszahlengenerator;

public class Verteilregelgenerierung {

    private static final byte[] VERSION = { 0x01, 0x00, 0x00 };

    private static final String FEHLER_PARAMETER_NULL = "Parameter ist null.";
    private static final String FEHLER_DATENGROESSE = "Mehr Bytes als maxinale Länge eines Byte-Arrays.";
    private static final String FEHLER_EINTRAEGE = "Die Einträge sind fehlerhaft.";

    private static final int EINTRAG_GROESSE = 5;

    private final KonfigurationVerteilregeln konfiguration;

    public Verteilregelgenerierung(final KonfigurationVerteilregeln konfiguration) {
        if (konfiguration == null) {
            throw new NullPointerException(FEHLER_PARAMETER_NULL);
        }
        this.konfiguration = konfiguration;
    }

    public byte[] generiere() {
        byte[] verteilung = ermittleVerteilung();
        return erzeuge(verteilung);
    }

    public byte[] generiereMitVorgabe(List<Eintrag> eintraege) {
        checkParameter(eintraege);
        byte[] verteilung = ermittleVerteilung(eintraege);
        return erzeuge(verteilung);
    }

    public void generiere(OutputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        stream.write(VERSION);
        stream.write(ByteUtils.intToBytes(konfiguration.getBlockgroesse().get()));
        stream.write(ByteUtils.intToBytes(konfiguration.getNutzdaten().get()));
        byte wertInByte = konfiguration.getAnzahlKanaele().get().byteValue();
        stream.write(wertInByte);
        wertInByte = konfiguration.getBittiefe().get().byteValue();
        stream.write(wertInByte);
        stream.write(ByteUtils.intToBytes((ermittleAnzahlBytesVerteilung())));
        schreibeVerteilung(stream);
    }

    public static List<Eintrag> konvertiereEintraege(byte[] verteilregel) {
        if (verteilregel == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        List<Eintrag> eintraege = new LinkedList<>();
        int offset = 17;
        boolean ende = false;
        while (!ende) {
            byte[] positionsteil = new byte[4];
            System.arraycopy(verteilregel, offset, positionsteil, 0, positionsteil.length);
            Positionsnummer positionsnummer = new Positionsnummer(ByteUtils.bytesToInt(positionsteil));
            Byte kanalteil = Byte.valueOf(verteilregel[offset + 4]);
            Kanalnummer kanalnummer = new Kanalnummer(kanalteil.intValue());
            Eintrag eintrag = new Eintrag(positionsnummer, kanalnummer);
            eintraege.add(eintrag);
            offset += EINTRAG_GROESSE;
            if (offset >= verteilregel.length) {
                ende = true;
            }
        }
        return eintraege;
    }

    public static AnzahlPositionen ermittleAnzahlPositionen(byte[] verteilregel) {
        if (verteilregel == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        int offset = 3;
        byte[] teil = new byte[4];
        System.arraycopy(verteilregel, offset, teil, 0, teil.length);
        return new AnzahlPositionen(ByteUtils.bytesToInt(teil));
    }

    public static AnzahlNutzdaten ermittleAnzahlNutzdaten(byte[] verteilregel) {
        if (verteilregel == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        int offset = 7;
        byte[] teil = new byte[4];
        System.arraycopy(verteilregel, offset, teil, 0, teil.length);
        return new AnzahlNutzdaten(ByteUtils.bytesToInt(teil));
    }

    public static AnzahlKanaele ermittleAnzahlKanaele(byte[] verteilregel) {
        if (verteilregel == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        int offset = 11;
        byte[] teil = new byte[4];
        teil[3] = verteilregel[offset];
        return new AnzahlKanaele(ByteUtils.bytesToInt(teil));
    }

    public static Bittiefe ermittleBittiefe(byte[] verteilregel) {
        if (verteilregel == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_NULL);
        }
        int offset = 12;
        byte[] teil = new byte[4];
        teil[3] = verteilregel[offset];
        return new Bittiefe(ByteUtils.bytesToInt(teil));
    }

    private byte[] erzeuge(byte[] verteilung) {
        long anzahl = ermittleAnzahlBytes();
        if (anzahl > Integer.MAX_VALUE) {
            throw new VerteilregelgenerierungException(FEHLER_DATENGROESSE);
        }
        byte[] daten = new byte[(int) anzahl];
        int offset = 0;
        offset = ByteUtils.setzeByteArray(daten, offset, VERSION);
        offset = ByteUtils.setzeIntWert(daten, offset, konfiguration.getBlockgroesse().get());
        offset = ByteUtils.setzeIntWert(daten, offset, konfiguration.getNutzdaten().get());
        offset = ByteUtils.setzeAlsByteWert(daten, offset, konfiguration.getAnzahlKanaele().get().byteValue());
        offset = ByteUtils.setzeAlsByteWert(daten, offset, konfiguration.getBittiefe().get());
        offset = ByteUtils.setzeIntWert(daten, offset, ermittleAnzahlBytesVerteilung());
        ByteUtils.setzeByteArray(daten, offset, verteilung);
        return daten;
    }

    private long ermittleAnzahlBytes() {
        int anzahlBytesVersion = 3;
        int anzahlBytesBlockgroesse = 4;
        int anzahlBytesNutzdaten = 4;
        int anzahlBytesAnzahlKanaele = 1;
        int anzahlBytesBittiefe = 1;
        int anzahlBytesVerteilung = ermittleAnzahlBytesVerteilung() + 4;
        return anzahlBytesVersion + anzahlBytesBlockgroesse + anzahlBytesNutzdaten + anzahlBytesAnzahlKanaele
                + anzahlBytesBittiefe + anzahlBytesVerteilung;
    }

    private int ermittleAnzahlBytesVerteilung() {
        return ermittleAnzahlEintraegeNutzdaten() * EINTRAG_GROESSE;
    }

    private byte[] ermittleVerteilung() {
        byte[] verteilungsdaten = new byte[ermittleAnzahlBytesVerteilung()];
        Set<Integer> eintraegeNutzdaten = erzeugeZufallszahlen();
        int offset = 0;
        for (Integer eintrag : eintraegeNutzdaten) {
            byte[] eintragsdaten = erzeugeEintrag(eintrag);
            offset = ByteUtils.setzeByteArray(verteilungsdaten, offset, eintragsdaten);
        }
        return verteilungsdaten;
    }

    private byte[] ermittleVerteilung(List<Eintrag> eintraege) {
        byte[] verteilungsdaten = new byte[ermittleAnzahlBytesVerteilung()];
        int offset = 0;
        for (Eintrag eintrag : eintraege) {
            byte[] eintragsdaten = erzeugeEintrag(eintrag);
            offset = ByteUtils.setzeByteArray(verteilungsdaten, offset, eintragsdaten);
        }
        return verteilungsdaten;
    }

    private void schreibeVerteilung(OutputStream stream) throws IOException {
        Set<Integer> eintraegeNutzdaten = erzeugeZufallszahlen();
        for (Integer eintrag : eintraegeNutzdaten) {
            stream.write(erzeugeEintrag(eintrag));
        }
    }

    private int ermittleAnzahlEintraegeNutzdaten() {
        int bittiefe = konfiguration.getBittiefe().get();
        int bitteile = 8 / bittiefe;
        return bitteile * konfiguration.getNutzdaten().get();
    }

    private int ermittleAnzahlEintraegeBlock() {
        int bittiefe = konfiguration.getBittiefe().get();
        int bitteile = 8 / bittiefe;
        return bitteile * konfiguration.getBlockgroesse().get();
    }

    private byte[] erzeugeEintrag(Integer zufallszahl) {
        byte[] eintrag = new byte[EINTRAG_GROESSE];
        int zwischenzahl = zufallszahl;
        int kanal = 1;
        int blockgroesse = konfiguration.getBlockgroesse().get();
        while (zwischenzahl > blockgroesse) {
            zwischenzahl -= blockgroesse;
            kanal++;
        }
        int offset = ByteUtils.setzeIntWert(eintrag, 0, zwischenzahl);
        ByteUtils.setzeAlsByteWert(eintrag, offset, kanal);
        return eintrag;
    }

    private byte[] erzeugeEintrag(Eintrag eintrag) {
        byte[] eintragswert = new byte[EINTRAG_GROESSE];
        int offset = ByteUtils.setzeIntWert(eintragswert, 0, eintrag.getPositionsnummer().get());
        ByteUtils.setzeAlsByteWert(eintragswert, offset, eintrag.getKanalnummer().get());
        return eintragswert;
    }

    private Set<Integer> erzeugeZufallszahlen() {
        return Zufallszahlengenerator.erzeugeZufallszahlenmenge(
                new AnzahlZufallszahlen(ermittleAnzahlEintraegeNutzdaten()), new BereichVon(1),
                new BereichBis(ermittleAnzahlEintraegeBlock() + 1));
    }

    private void checkParameter(List<Eintrag> eintraege) {
        if (eintraege == null) {
            throw new NullPointerException(FEHLER_PARAMETER_NULL);
        }
        if (eintraege.isEmpty()) {
            throw new IllegalArgumentException(FEHLER_EINTRAEGE);
        }
        if (eintraege.size() != ermittleAnzahlEintraegeNutzdaten()) {
            throw new IllegalArgumentException(FEHLER_EINTRAEGE);
        }
        for (Eintrag eintrag : eintraege) {
            if (eintrag.getPositionsnummer().get() > konfiguration.getBlockgroesse().get()) {
                throw new IllegalArgumentException(FEHLER_EINTRAEGE);
            }
            if (eintrag.getKanalnummer().get() > konfiguration.getAnzahlKanaele().get()) {
                throw new IllegalArgumentException(FEHLER_EINTRAEGE);
            }
        }
    }
}
