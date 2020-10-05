package de.platen.steganograph;

import java.io.IOException;

import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Blockgroesse;
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
        if (dateinameQuelle.toLowerCase().endsWith(".png") || dateinameQuelle.toLowerCase().endsWith(".bmp")) {
            AktionVersteckenInBild.versteckeInBild(dateinameVerteilregel, dateinameNutzdaten, dateinameQuelle,
                    dateinameZiel, verrauschoption);
        }
        if (dateinameQuelle.toLowerCase().endsWith(".wav")) {
            AktionVersteckenInAudio.versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten,
                    dateinameQuelle, dateinameZiel, verrauschoption);
        }
    }

    public void hole(String dateinameVerteilregel, String dateinameQuelle, String dateinameNutzdaten)
            throws IOException {
        if (dateinameQuelle.toLowerCase().endsWith(".png") || dateinameQuelle.toLowerCase().endsWith(".bmp")) {
            AktionHolenAusBild.holeAusBild(dateinameVerteilregel, dateinameQuelle, dateinameNutzdaten);
        }
        if (dateinameQuelle.toLowerCase().endsWith(".wav")) {
            AktionHolenAusAudio.holeNutzdatenAusAudio(dateinameNutzdaten, dateinameNutzdaten, dateinameNutzdaten);
        }
    }
}
