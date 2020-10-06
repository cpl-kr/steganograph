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

    private final AktionVersteckenInBild aktionVersteckenInBild;
    private final AktionVersteckenInAudio aktionVersteckenInAudio;
    private final AktionHolenAusBild aktionHolenAusBild;
    private final AktionHolenAusAudio aktionHolenAusAudio;

    public Aktionen(AktionVersteckenInBild aktionVersteckenInBild, AktionVersteckenInAudio aktionVersteckenInAudio,
            AktionHolenAusBild aktionHolenAusBild, AktionHolenAusAudio aktionHolenAusAudio) {
        this.aktionVersteckenInBild = aktionVersteckenInBild;
        this.aktionVersteckenInAudio = aktionVersteckenInAudio;
        this.aktionHolenAusBild = aktionHolenAusBild;
        this.aktionHolenAusAudio = aktionHolenAusAudio;
    }

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
            aktionVersteckenInBild.versteckeInBild(dateinameVerteilregel, dateinameNutzdaten, dateinameQuelle,
                    dateinameZiel, verrauschoption);
        }
        if (dateinameQuelle.toLowerCase().endsWith(".wav")) {
            aktionVersteckenInAudio.versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten,
                    dateinameQuelle, dateinameZiel, verrauschoption);
        }
    }

    public void hole(String dateinameVerteilregel, String dateinameQuelle, String dateinameNutzdaten)
            throws IOException {
        if (dateinameQuelle.toLowerCase().endsWith(".png") || dateinameQuelle.toLowerCase().endsWith(".bmp")) {
            aktionHolenAusBild.holeAusBild(dateinameVerteilregel, dateinameQuelle, dateinameNutzdaten);
        }
        if (dateinameQuelle.toLowerCase().endsWith(".wav")) {
            aktionHolenAusAudio.holeNutzdatenAusAudio(dateinameNutzdaten, dateinameNutzdaten, dateinameNutzdaten);
        }
    }
}
