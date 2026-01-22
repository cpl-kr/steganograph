package de.platen.steganograph.uniformat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlPositionen;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Eintrag;
import de.platen.steganograph.datentypen.Kanalnummer;
import de.platen.steganograph.datentypen.Positionsinhalt;
import de.platen.steganograph.datentypen.Positionsnummer;
import de.platen.steganograph.utils.ByteUtils;
import de.platen.zufallszahlengenerator.Zufallszahlengenerator;

public abstract class UniFormat {

    protected static final String FEHLER_PARAMETER_UNIFORMAT_NULL = "Einer der Parameter ist null.";
    protected static final String FEHLER_PARAMETER_UNIFORMAT_EINTRAGSINHALT = "Einer der Einträge passt nicht zu Anzahl Positionen und Anzahl Kanäle.";
    protected static final String FEHLER_PARAMETER_UNIFORMAT_EINTRAGSMENGE = "Die Eintragsmenge passt nicht zu Anzahl Positionen, Anzahl Kanäle und Bittiefe.";

    protected final AnzahlPositionen anzahlPositionen;
    protected final AnzahlKanaele anzahlKanaele;
    private final Bittiefe bittiefe;
    private final List<Eintrag> eintraege;

    protected final Map<Positionsnummer, Positionsinhalt> positionsinhalte = new HashMap<>();

    protected UniFormat(AnzahlPositionen anzahlPositionen, AnzahlKanaele anzahlKanaele, Bittiefe bittiefe,
            List<Eintrag> eintraege) {
        this.anzahlPositionen = anzahlPositionen;
        this.anzahlKanaele = anzahlKanaele;
        this.bittiefe = bittiefe;
        this.eintraege = eintraege;
        if ((anzahlPositionen == null) || (anzahlKanaele == null) || (bittiefe == null) || (eintraege == null)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
        checkPositionsinhalt();
    }

    public void versteckeNutzdaten(byte[] nutzdaten) {
        checkParameter(nutzdaten);
        checkPositionsmenge(nutzdaten.length);
        int indexEintraege = 0;
        for (int indexNutzdaten = 0; indexNutzdaten < nutzdaten.length; indexNutzdaten++) {
            if (indexEintraege < eintraege.size()) {
                indexEintraege = versteckeByte(indexEintraege, nutzdaten[indexNutzdaten]);
            }
        }
    }

    public void versteckeNutzdaten(InputStream nutzdaten) throws IOException {
        checkParameter(nutzdaten);
        int bitteile = 8 / bittiefe.get();
        int anzahlNutzdaten = eintraege.size() / bitteile;
        int index = 0;
        boolean ende = false;
        int indexEintraege = 0;
        while (!ende) {
            if (index < anzahlNutzdaten) {
                byte[] nutzdatenByte = new byte[1];
                if (nutzdaten.read(nutzdatenByte) > 0) {
                    if (indexEintraege < eintraege.size()) {
                        indexEintraege = versteckeByte(indexEintraege, nutzdatenByte[0]);
                    } else {
                        ende = true;
                    }
                } else {
                    ende = true;
                }
                index++;
            } else {
                ende = true;
            }
        }
    }

    public byte[] holeNutzdaten(int maximaleAnzahl) {
        int bitteile = 8 / bittiefe.get();
        int laenge = eintraege.size() / bitteile;
        if (maximaleAnzahl < laenge) {
            laenge = maximaleAnzahl;
        }
        byte[] nutzdaten = new byte[laenge];
        int indexEintraege = 0;
        byte[] nutzdatenByte = new byte[1];
        for (int indexNutzdaten = 0; indexNutzdaten < nutzdaten.length; indexNutzdaten++) {
            if (indexNutzdaten < maximaleAnzahl) {
                indexEintraege = holeByte(indexEintraege, nutzdatenByte);
                nutzdaten[indexNutzdaten] = nutzdatenByte[0];
            } else {
                return nutzdaten;
            }
        }
        return nutzdaten;
    }

    public void holeNutzdaten(OutputStream nutzdaten, int maximaleAnzahl) throws IOException {
        checkParameter(nutzdaten);
        int bitteile = 8 / bittiefe.get();
        int anzahlNutzdaten = eintraege.size() / bitteile;
        int index = 0;
        boolean ende = false;
        int indexEintraege = 0;
        while (!ende) {
            if ((index < anzahlNutzdaten) && (index < maximaleAnzahl)) {
                byte[] nutzdatenByte = new byte[1];
                if (indexEintraege < eintraege.size()) {
                    indexEintraege = holeByte(indexEintraege, nutzdatenByte);
                    nutzdaten.write(nutzdatenByte);
                } else {
                    ende = true;
                }
                index++;
            } else {
                ende = true;
            }
        }
    }

    public void verrausche() {
        for (int position = 1; position <= anzahlPositionen.get(); position++) {
            Positionsinhalt positionsinhalt = positionsinhalte.get(new Positionsnummer(position));
            if (positionsinhalt != null) {
                for (int kanalnummer = 1; kanalnummer <= anzahlKanaele.get(); kanalnummer++) {
                    verrausche(positionsinhalt, new Kanalnummer(kanalnummer));
                }
            }
        }
    }

    public int getAnzahlPositionen() {
        return anzahlPositionen.get();
    }

    public int getAnzahlKanaele() {
        return anzahlKanaele.get();
    }

    public int getBittiefe() {
        return bittiefe.get();
    }

    private void checkPositionsinhalt() {
        for (Eintrag eintrag : eintraege) {
            if ((eintrag.getPositionsnummer().get() > anzahlPositionen.get())
                    || (eintrag.getKanalnummer().get() > anzahlKanaele.get())) {
                throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_EINTRAGSINHALT);
            }
        }
    }

    private void checkPositionsmenge(int anzahlBytes) {
        int anzahlEintraegeIst = eintraege.size();
        int anzahlEintraegeSoll = anzahlBytes * (8 / bittiefe.get());
        if (anzahlEintraegeIst < anzahlEintraegeSoll) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_EINTRAGSMENGE);
        }
    }

    private int versteckeByte(int indexEintrag, byte nutzdatenByte) {
        int neuerIndex = indexEintrag;
        int bitteile = 8 / bittiefe.get();
        for (int index = 0; index < bitteile; index++, neuerIndex++) {
            Eintrag eintrag = eintraege.get(neuerIndex);
            Positionsinhalt positionsinhalt = positionsinhalte.get(eintrag.getPositionsnummer());
            Kanalnummer kanalunummer = eintrag.getKanalnummer();
            int wert = positionsinhalt.holeWert(kanalunummer);
            wert = ByteUtils.setzeBitteilAusByteInWert(wert, index, bittiefe.get(), nutzdatenByte);
            positionsinhalt.setzeWert(kanalunummer, wert);
        }
        return neuerIndex;
    }

    private int holeByte(int indexEintrag, byte[] nutzdatenByte) {
        int neuerIndex = indexEintrag;
        int bitteile = 8 / bittiefe.get();
        int wertNeu = 0;
        int wertBitteil = 0;
        for (int index = 0; index < bitteile; index++, neuerIndex++) {
            Eintrag eintrag = eintraege.get(neuerIndex);
            Positionsinhalt positionsinhalt = positionsinhalte.get(eintrag.getPositionsnummer());
            Kanalnummer kanalunummer = eintrag.getKanalnummer();
            int wert = positionsinhalt.holeWert(kanalunummer);
            if (wert != 0) {
                wertBitteil = ByteUtils.holeBitteilAusWert(wert, index, bittiefe.get());
                wertNeu |= wertBitteil;
            }
        }
        byte[] nutzdatenwert = ByteUtils.intToBytes(wertNeu);
        nutzdatenByte[0] = nutzdatenwert[3];
        return neuerIndex;
    }

    private void verrausche(Positionsinhalt positionsinhalt, Kanalnummer kanalnummer) {
        int bereich = 0;
        switch (bittiefe.get()) {
        case 1:
            bereich = 2;
            break;
        case 2:
            bereich = 4;
            break;
        case 4:
            bereich = 16;
            break;
        case 8:
            bereich = 256;
            break;
        }
        positionsinhalt.setzeWert(kanalnummer,
                (ByteUtils.loescheBits(positionsinhalt.holeWert(kanalnummer), bittiefe.get()))
                        | Zufallszahlengenerator.erzeugeZufallszahl(bereich));
    }

    private static void checkParameter(OutputStream nutzdaten) {
        if (nutzdaten == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
    }

    private static void checkParameter(InputStream nutzdaten) {
        if (nutzdaten == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
    }

    private static void checkParameter(byte[] nutzdaten) {
        if (nutzdaten == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_UNIFORMAT_NULL);
        }
    }
}