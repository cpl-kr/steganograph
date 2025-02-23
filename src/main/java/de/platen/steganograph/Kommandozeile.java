package de.platen.steganograph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.platen.steganograph.gui.Gui;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Kommandozeile {

    private static final String OPTION_VERTEILREGELGENERIERUNG = "verteilregelgenerierung";
    private static final String OPTION_BLOCKGROESSE = "blockgroesse";
    private static final String OPTION_NUTZDATEN = "nutzdaten";
    private static final String OPTION_ANZAHL_KANAELE = "anzahlkanaele";
    private static final String OPTION_BITTIEFE = "bittiefe";
    private static final String OPTION_DATEINAME = "dateiname";
    private static final String OPTION_VERRAUSCHEN = "verrauschen";
    private static final String VERRAUSCHEN_WERT_OHNE = "ohne";
    private static final String VERRAUSCHEN_WERT_NUTZDATENBEREICH = "nutzdatenbereich";
    private static final String VERRAUSCHEN_WERT_ALLES = "alles";
    private static final String OPTION_PUBLIC_KEYS = "verschluesselungsdateien";
    private static final String OPTION_PRIVATE_KEY = "entschluesselungsdatei";
    private static final String OPTION_PASSWORT = "passwort";

    private static final String HINWEIS_BLOCKGROESSE = "Für die Verteilregelgenerierung muss die Blockgröße angegeben werden.";
    private static final String HINWEIS_NUTZDATEN = "Für die Verteilregelgenerierung muss die Anzahl der Nutzdaten (Bytes) angegeben werden.";
    private static final String HINWEIS_ANZAHL_KANAELE = "Für die Verteilregelgenerierung muss die Anzahl der Kanäle angegeben werden.";
    private static final String HINWEIS_BITTIEFE = "Für die Verteilregelgenerierung muss die Bittiefe angegeben werden.";
    private static final String HINWEIS_DATEINAME = "Für die Verteilregelgenerierung muss ein Dateiname angegeben werden.";
    private static final String HINWEIS_VERRAUSCHEN = "Für das Verrauschen muss 'ohne' oder 'nutzdatenbereich' oder 'alles' angegeben werden.";

    private static final String OPTION_VERSTECKEN = "verstecken";
    private static final String OPTION_HOLEN = "holen";

    private static final String OPTION_DATEINAME_VERTEILREGEL = "dateiVerteilregel";
    private static final String OPTION_DATEINAME_NUTZDATEN = "dateiNutzdaten";
    private static final String OPTION_DATEINAME_QUELLE = "dateiQuelle";
    private static final String OPTION_DATEINAME_ZIEL = "dateiZiel";

    private static final String HINWEIS_DATEINAME_VERTEILREGEL = "Für das Verstecken/Holen muss die Datei mit den Verteilregeln angegeben werden.";
    private static final String HINWEIS_DATEINAME_NUTZDATEN = "Für das Verstecken/Holen muss die Datei mit/für den Nutzdaten angegeben werden.";
    private static final String HINWEIS_DATEINAME_QUELLE = "Für das Verstecken/Holen muss die Quelldatei angegeben werden.";
    private static final String HINWEIS_DATEINAME_ZIEL = "Für das Verstecken muss die Zieldatei angegeben werden.";

    private static final String OPTION_KEY = "key";
    private static final String OPTION_ID = "id";
    private static final String OPTION_DATEI_PUBLIC_KEY = "dateiPublicKey";
    private static final String OPTION_DATEI_PRIVATE_KEY = "dateiPrivateKey";
    private static final String OPTION_PASSWORT_KEY = "passwort";

    private static final String HINWEIS_ID = "Für das Erzeugen eines Schlüsselpaares muss die ID angegeben werden.";
    private static final String HINWEIS_DATEI_PUBLIC_KEY = "Für das Erzeugen eines Schlüsselpaares muss der Dateiname des ööfentlichen Schlüssels angegeben werden.";
    private static final String HINWEIS_DATEI_PRIVATE_KEY = "Für das Erzeugen eines Schlüsselpaares muss der Dateiname des ööfentlichen Schlüssels angegeben werden.";

    private static final String OPTION_GUI = "gui";

    private static final String OPTION_IN_DATEI = "inDatei";
    private static final String OPTION_IN_DATEI_QUELLE = "quelldatei";
    private static final String OPTION_IN_DATEI_ZIEL = "zieldatei";
    private static final String OPTION_IN_DATEI_LAENGE = "laenge";
    private static final String OPTION_IN_DATEI_OFFSET = "offset";
    private static final String OPTION_IN_DATEI_ERZEUGUNG = "erzeugung";

    private static final String HINWEIS_IN_DATEI_QUELLE = "Für das Verstecken in einer Datei muss die Quelldatei angegeben werden.";
    private static final String HINWEIS_IN_DATEI_ZIEL = "Für das Verstecken in einer Datei muss die Zieldatei angegeben werden.";
    private static final String HINWEIS_IN_DATEI_LAENGE = "Für das Verstecken in einer Datei muss die Länge angegeben werden.";
    private static final String HINWEIS_IN_DATEI_OFFSET = "Für das Verstecken in einer Datei muss der Offset angegeben werden.";
    private static final String HINWEIS_IN_DATEI_ERZEUGUNG = "Für das Verstecken in einer Datei muss die Erzeugungsoption angegeben werden.";

    private static final String OPTION_AUS_DATEI = "ausDatei";
    private static final String OPTION_AUS_DATEI_QUELLE = "quelldatei";
    private static final String OPTION_AUS_DATEI_ZIEL = "zieldatei";
    private static final String OPTION_AUS_DATEI_OFFSET = "offset";
    private static final String OPTION_AUS_DATEI_LAENGE = "laenge";

    private static final String HINWEIS_AUS_DATEI_QUELLE = "Für das Auslesen aus einer Datei muss die Quelldatei angegeben werden.";
    private static final String HINWEIS_AUS_DATEI_ZIEL = "Für das Auslesen aus einer Datei muss die Zieldatei angegeben werden.";
    private static final String HINWEIS_AUS_DATEI_OFFSET = "Für das Auslesen aus einer Datei muss der Offset angegeben werden.";
    private static final String HINWEIS_AUS_DATEI_LAENGE = "Für das Auslesen aus einer Datei muss die Länge angegeben werden.";

    private static final String OPTION_ZUFALLSDATEI = "zufallsdatei";
    private static final String OPTION_ZUFALLSDATEI_NAME = "name";
    private static final String OPTION_ZUFALLSDATEI_LAENGE = "laenge";

    private static final String HINWEIS_ZUFFALSDATEI_NAME = "Für die Erzeugung einer Datei mit Zufallswerten muss der Dateiname angegeben werden";
    private static final String HINWEIS_ZUFFALSDATEI_LAENGE = "Für die Erzeugung einer Datei mit Zufallswerten muss die Dateilänge angegeben werden";

    private final Aktionen aktionen;

    public Kommandozeile(Aktionen aktionen) {
        this.aktionen = aktionen;
    }

    public int behandleKommandozeile(String[] args) throws IOException, ParseException {
        if (args.length  == 0) {
            new Gui().starteGui(args);
            return 0;
        }
        Options options = new Options();
        addOptionsVerteilregel(options);
        addOptionsVerstecken1(options);
        addOptionsAuslesen1(options);
        addOptionsKeyPaar(options);
        addOptionsGui(options);
        addOptionsVerstecken2(options);
        addOptionsAuslesen2(options);
        addOptionsZufallsdatei(options);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption(OPTION_VERTEILREGELGENERIERUNG)) {
            return behandleVerteilregelgenerierung(cmd, aktionen);
        }
        if (cmd.hasOption(OPTION_VERSTECKEN)) {
            return behandleVerstecken1(cmd, aktionen);
        }
        if (cmd.hasOption(OPTION_HOLEN)) {
            return behandleAuslesen1(cmd, aktionen);
        }
        if (cmd.hasOption(OPTION_KEY)) {
            return behandleKey(cmd, aktionen);
        }
        if (cmd.hasOption(OPTION_GUI)) {
            return behandleGui(args);
        }
        if (cmd.hasOption(OPTION_IN_DATEI)) {
            return behandleVerstecken2(cmd, aktionen);
        }
        if (cmd.hasOption(OPTION_AUS_DATEI)) {
            return behandleAuslesen2(cmd, aktionen);
        }
        if (cmd.hasOption(OPTION_ZUFALLSDATEI)) {
            return behandleZufallsdatei(cmd, aktionen);
        }
        return 0;
    }

    private static void addOptionsVerteilregel(Options options) {
        options.addOption("v", OPTION_VERTEILREGELGENERIERUNG, false, "Parameter für die Verteilregelgenerierung.");
        options.addOption("b", OPTION_BLOCKGROESSE, true, "Blockgröße für die Verteilregelgenerierung.");
        options.addOption("n", OPTION_NUTZDATEN, true, "Anzahl der Nutzdaten (Bytes) für die Verteilregelgenerierung.");
        options.addOption("k", OPTION_ANZAHL_KANAELE, true, "Anzahl der Kanäle für die Verteilregelgenerierung.");
        options.addOption("t", OPTION_BITTIEFE, true, "Anzahl der Bits pro Kanal für die Verteilregelgenerierung.");
        options.addOption("d", OPTION_DATEINAME, true, "Datei für die Verteilregelgenerierung.");
        options.addOption("e", OPTION_PUBLIC_KEYS, true, "Dateien für die Verschlüsselung.");
        options.addOption("p", OPTION_PASSWORT, true, "Passwort.");
    }

    private static void addOptionsVerstecken1(Options options) {
        options.addOption("v", OPTION_VERSTECKEN, false, "Parameter für das Verstecken.");
        options.addOption("r", OPTION_DATEINAME_VERTEILREGEL, true, "Datei mit Verteilregeln für das Verstecken.");
        options.addOption("n", OPTION_DATEINAME_NUTZDATEN, true, "Datei der Nutzdaten für das Verstecken.");
        options.addOption("q", OPTION_DATEINAME_QUELLE, true, "Quelldatei für das Verstecken.");
        options.addOption("z", OPTION_DATEINAME_ZIEL, true, "Zieldatei für das Verstecken.");
        options.addOption("w", OPTION_VERRAUSCHEN, true, "Angabe für das Verrauschen.");
        options.addOption("e", OPTION_PRIVATE_KEY, true, "Datei für die Entschlüsselung.");
        options.addOption("p", OPTION_PASSWORT, true, "Passwort.");
    }

    private static void addOptionsAuslesen1(Options options) {
        options.addOption("h", OPTION_HOLEN, false, "Parameter für das Holen.");
        options.addOption("v", OPTION_DATEINAME_VERTEILREGEL, true, "Datei mit Verteilregeln für das Holen.");
        options.addOption("n", OPTION_DATEINAME_NUTZDATEN, true, "Datei der Nutzdaten für das Holen.");
        options.addOption("q", OPTION_DATEINAME_QUELLE, true, "Quelldatei für das Holen.");
        options.addOption("e", OPTION_PRIVATE_KEY, true, "Datei für die Entschlüsselung.");
        options.addOption("p", OPTION_PASSWORT, true, "Passwort.");
    }

    private static void addOptionsGui(Options options) {
        options.addOption("g", OPTION_GUI, false, "Parameter für die GUI.");
    }

    private static void addOptionsKeyPaar(Options options) {
        options.addOption("h", OPTION_KEY, false, "Parameter für das Erzeugen eines Schlüsselpaares.");
        options.addOption("i", OPTION_ID, true, "Parameter für die ID des Schlüsselpaares.");
        options.addOption("d", OPTION_DATEI_PUBLIC_KEY, true, "Parameter für den Dateinamen des öffentlichen Schlüssels.");
        options.addOption("f", OPTION_DATEI_PRIVATE_KEY, true, "Parameter für den Dateinamen des privaten Schlüssels.");
        options.addOption("p", OPTION_PASSWORT_KEY, true, "Parameter für das Passwort des privaten Schlüssels.");
    }

    private static void addOptionsVerstecken2(Options options) {
        options.addOption("i", OPTION_IN_DATEI, false, "Parameter für das Verstecken");
        options.addOption("q", OPTION_IN_DATEI_QUELLE, true, "Die zu versteckende Datei");
        options.addOption("z", OPTION_IN_DATEI_ZIEL, true, "Datei für die zu versteckende Datei");
        options.addOption("l", OPTION_IN_DATEI_LAENGE, true, "Länge zu versteckenden Datei");
        options.addOption("o", OPTION_IN_DATEI_OFFSET, true, "Offset innerhalb der Datei");
        options.addOption("e", OPTION_IN_DATEI_ERZEUGUNG, true, "Flag für die Erzeugung");
    }

    private static void addOptionsAuslesen2(Options options) {
        options.addOption("i", OPTION_AUS_DATEI, false, "Parameter für das Auslesen");
        options.addOption("i", OPTION_AUS_DATEI_QUELLE, true, "Datei  mit der versteckten Datei");
        options.addOption("i", OPTION_AUS_DATEI_ZIEL, true, "Name der ausgelesenen Datei");
        options.addOption("i", OPTION_AUS_DATEI_OFFSET, true, "Offset innerhalb der Datei");
        options.addOption("i", OPTION_AUS_DATEI_LAENGE, true, "Länge der auszulesenden Datei");
    }

    private static void addOptionsZufallsdatei(Options options) {
        options.addOption("z", OPTION_ZUFALLSDATEI, false, "Parameter fü+r die Ertzeugung einer Datei mit Zufallswerten");
        options.addOption("n", OPTION_ZUFALLSDATEI_NAME, true, "Parameter für den Namen der Datei mit Zufallswerten");
        options.addOption("l", OPTION_ZUFALLSDATEI_LAENGE, true, "Parameter für die Länge der Datei mit Zufallswerten");
    }

    private static int behandleVerteilregelgenerierung(CommandLine cmd, Aktionen aktionen) throws IOException {
        boolean hatfehlendenParameter = false;
        String blockgroesse = getOption(cmd, OPTION_BLOCKGROESSE);
        if (!isOptionOk(blockgroesse)) {
            System.err.println(HINWEIS_BLOCKGROESSE);
            hatfehlendenParameter = true;
        }
        String nutzdaten = getOption(cmd, OPTION_NUTZDATEN);
        if (!isOptionOk(nutzdaten)) {
            System.err.println(HINWEIS_NUTZDATEN);
            hatfehlendenParameter = true;
        }
        String anzahlKanaele = getOption(cmd, OPTION_ANZAHL_KANAELE);
        if (!isOptionOk(anzahlKanaele)) {
            System.err.println(HINWEIS_ANZAHL_KANAELE);
            hatfehlendenParameter = true;
        }
        String bittiefe = getOption(cmd, OPTION_BITTIEFE);
        if (!isOptionOk(bittiefe)) {
            System.err.println(HINWEIS_BITTIEFE);
            hatfehlendenParameter = true;
        }
        String dateiname = getOption(cmd, OPTION_DATEINAME);
        if (!isOptionOk(dateiname)) {
            System.err.println(HINWEIS_DATEINAME);
            hatfehlendenParameter = true;
        }
        String dateienPublicKeys = getOption(cmd, OPTION_PUBLIC_KEYS);
        List<String> dateinamenPublicKeys = new ArrayList<>();
        if (dateienPublicKeys != null) {
            String[] dateien = dateienPublicKeys.split(",");
            dateinamenPublicKeys.addAll(Arrays.asList(dateien));
        }
        String passwort = getOption(cmd, OPTION_PASSWORT);
        if (hatfehlendenParameter) {
            return 1;
        }
        aktionen.generiere(blockgroesse, nutzdaten, anzahlKanaele, bittiefe, dateiname, dateinamenPublicKeys, passwort);
        return 0;
    }

    private static int behandleVerstecken1(CommandLine cmd, Aktionen aktionen) throws IOException {
        boolean hatfehlendenParameter = false;
        String dateiVerteilregel = getOption(cmd, OPTION_DATEINAME_VERTEILREGEL);
        if (!isOptionOk(dateiVerteilregel)) {
            System.err.println(HINWEIS_DATEINAME_VERTEILREGEL);
            hatfehlendenParameter = true;
        }
        String dateiNutzdaten = getOption(cmd, OPTION_DATEINAME_NUTZDATEN);
        if (!isOptionOk(dateiNutzdaten)) {
            System.err.println(HINWEIS_DATEINAME_NUTZDATEN);
            hatfehlendenParameter = true;
        }
        String dateiQuelle = getOption(cmd, OPTION_DATEINAME_QUELLE);
        if (!isOptionOk(dateiQuelle)) {
            System.err.println(HINWEIS_DATEINAME_QUELLE);
            hatfehlendenParameter = true;
        }
        String dateiZiel = getOption(cmd, OPTION_DATEINAME_ZIEL);
        if (!isOptionOk(dateiZiel)) {
            System.err.println(HINWEIS_DATEINAME_ZIEL);
            hatfehlendenParameter = true;
        }
        if (hatfehlendenParameter) {
            return 1;
        }
        Verrauschoption verrauschoption = null;
        String verrauschen = getOption(cmd, OPTION_VERRAUSCHEN);
        if (isOptionOk(verrauschen)) {
            switch (verrauschen) {
            case VERRAUSCHEN_WERT_OHNE:
                verrauschoption = Verrauschoption.OHNE;
                break;
            case VERRAUSCHEN_WERT_NUTZDATENBEREICH:
                verrauschoption = Verrauschoption.NUTZDATENBEREICH;
                break;
            case VERRAUSCHEN_WERT_ALLES:
                verrauschoption = Verrauschoption.ALLES;
                break;
            default:
                System.err.println(HINWEIS_VERRAUSCHEN);
                return 1;
            }
        }
        String dateinamePrivateKey = getOption(cmd, OPTION_PRIVATE_KEY);
        String passwort = getOption(cmd, OPTION_PASSWORT);
        aktionen.verstecke(dateiVerteilregel, dateiNutzdaten, dateiQuelle, dateiZiel, verrauschoption, dateinamePrivateKey, passwort);
        return 0;
    }

    private static int behandleAuslesen1(CommandLine cmd, Aktionen aktionen) throws IOException {
        boolean hatfehlendenParameter = false;
        String dateiVerteilregel = getOption(cmd, OPTION_DATEINAME_VERTEILREGEL);
        if (!isOptionOk(dateiVerteilregel)) {
            System.err.println(HINWEIS_DATEINAME_VERTEILREGEL);
            hatfehlendenParameter = true;
        }
        String dateiNutzdaten = getOption(cmd, OPTION_DATEINAME_NUTZDATEN);
        if (!isOptionOk(dateiNutzdaten)) {
            System.err.println(HINWEIS_DATEINAME_NUTZDATEN);
            hatfehlendenParameter = true;
        }
        String dateiQuelle = getOption(cmd, OPTION_DATEINAME_QUELLE);
        if (!isOptionOk(dateiQuelle)) {
            System.err.println(HINWEIS_DATEINAME_QUELLE);
            hatfehlendenParameter = true;
        }
        if (hatfehlendenParameter) {
            return 1;
        }
        String dateinamePrivateKey = getOption(cmd, OPTION_PRIVATE_KEY);
        String passwort = getOption(cmd, OPTION_PASSWORT);
        aktionen.hole(dateiVerteilregel, dateiQuelle, dateiNutzdaten, dateinamePrivateKey, passwort);
        return 0;
    }

    private static int behandleKey(CommandLine cmd, Aktionen aktionen) {
        boolean hatfehlendenParameter = false;
        String id = getOption(cmd, OPTION_ID);
        if (!isOptionOk(id)) {
            System.err.println(HINWEIS_ID);
            hatfehlendenParameter = true;
        }
        String dateiPublicKey = getOption(cmd, OPTION_DATEI_PUBLIC_KEY);
        if (!isOptionOk(dateiPublicKey)) {
            System.err.println(HINWEIS_DATEI_PUBLIC_KEY);
            hatfehlendenParameter = true;
        }
        String dateiPrivateKey = getOption(cmd, OPTION_DATEI_PRIVATE_KEY);
        if (!isOptionOk(dateiPrivateKey)) {
            System.err.println(HINWEIS_DATEI_PRIVATE_KEY);
            hatfehlendenParameter = true;
        }
        if (hatfehlendenParameter) {
            return 1;
        }
        String passwort = getOption(cmd, OPTION_PASSWORT_KEY);
        if (passwort == null) {
            aktionen.erzeugeKeyPaar(id, dateiPublicKey, dateiPrivateKey);
        } else {
            aktionen.erzeugeKeyPaar(id, dateiPublicKey, dateiPrivateKey, passwort);
        }
        return 0;
    }

    private static int behandleVerstecken2(CommandLine cmd, Aktionen aktionen) {
        boolean hatfehlendenParameter = false;
        String quelle = getOption(cmd, OPTION_IN_DATEI_QUELLE);
        if (!isOptionOk(quelle)) {
            System.err.println(HINWEIS_IN_DATEI_QUELLE);
            hatfehlendenParameter = true;
        }
        String ziel = getOption(cmd, OPTION_IN_DATEI_ZIEL);
        if (!isOptionOk(ziel)) {
            System.err.println(HINWEIS_IN_DATEI_ZIEL);
            hatfehlendenParameter = true;
        }
        String laenge = getOption(cmd, OPTION_IN_DATEI_LAENGE);
        if (!isOptionOk(laenge)) {
            System.err.println(HINWEIS_IN_DATEI_LAENGE);
            hatfehlendenParameter = true;
        }
        String offset = getOption(cmd, OPTION_IN_DATEI_OFFSET);
        if (!isOptionOk(offset)) {
            System.err.println(HINWEIS_IN_DATEI_OFFSET);
            hatfehlendenParameter = true;
        }
        String erzeugung = getOption(cmd, OPTION_IN_DATEI_ERZEUGUNG);
        if (!isOptionOk(erzeugung)) {
            System.err.println(HINWEIS_IN_DATEI_ERZEUGUNG);
            hatfehlendenParameter = true;
        }
        if (hatfehlendenParameter) {
            return 1;
        }
        aktionen.verstecke(quelle, ziel, laenge, offset, erzeugung);
        return 0;
    }

    private static int behandleAuslesen2(CommandLine cmd, Aktionen aktionen) {
        boolean hatfehlendenParameter = false;
        String quelle = getOption(cmd, OPTION_AUS_DATEI_QUELLE);
        if (!isOptionOk(quelle)) {
            System.err.println(HINWEIS_AUS_DATEI_QUELLE);
            hatfehlendenParameter = true;
        }
        String ziel = getOption(cmd, OPTION_AUS_DATEI_ZIEL);
        if (!isOptionOk(ziel)) {
            System.err.println(HINWEIS_AUS_DATEI_ZIEL);
            hatfehlendenParameter = true;
        }
        String offset = getOption(cmd, OPTION_AUS_DATEI_OFFSET);
        if (!isOptionOk(offset)) {
            System.err.println(HINWEIS_AUS_DATEI_OFFSET);
            hatfehlendenParameter = true;
        }
        String laenge = getOption(cmd, OPTION_AUS_DATEI_LAENGE);
        if (!isOptionOk(laenge)) {
            System.err.println(HINWEIS_AUS_DATEI_LAENGE);
            hatfehlendenParameter = true;
        }
        if (hatfehlendenParameter) {
            return 1;
        }
        return 0;
    }

    private static int behandleZufallsdatei(CommandLine cmd, Aktionen aktionen) {
        boolean hatfehlendenParameter = false;
        String name = getOption(cmd, OPTION_ZUFALLSDATEI_NAME);
        if (!isOptionOk(name)) {
            System.err.println(HINWEIS_ZUFFALSDATEI_NAME);
            hatfehlendenParameter = true;
        }
        String laenge = getOption(cmd, OPTION_ZUFALLSDATEI_LAENGE);
        if (!isOptionOk(laenge)) {
            System.err.println(HINWEIS_ZUFFALSDATEI_LAENGE);
            hatfehlendenParameter = true;
        }
        if (hatfehlendenParameter) {
            return 1;
        }
        aktionen.erzeuge(name, laenge);
        return 0;
    }

    private static int behandleGui(String[] args) {
        new Gui().starteGui(args);
        return 0;
    }

    private static String getOption(CommandLine cmd, String option) {
        if (cmd.hasOption(option)) {
            return cmd.getOptionValue(option);
        }
        return null;
    }

    private static boolean isOptionOk(String option) {
        return !(option == null);
    }
}
