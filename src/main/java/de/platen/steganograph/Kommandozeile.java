package de.platen.steganograph;

import java.io.IOException;

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

    private final Aktionen aktionen;

    public Kommandozeile(Aktionen aktionen) {
        this.aktionen = aktionen;
    }

    public int behandleKommandozeile(String[] args) throws IOException, ParseException {
        Options options = new Options();
        addOptionsVerteilregel(options);
        addOptionsVerstecken(options);
        addOptionsHolen(options);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption(OPTION_VERTEILREGELGENERIERUNG)) {
            return behandleVerteilregelgenerierung(cmd, aktionen);
        }
        if (cmd.hasOption(OPTION_VERSTECKEN)) {
            return behandleVerstecken(cmd, aktionen);
        }
        if (cmd.hasOption(OPTION_HOLEN)) {
            return behandleHolen(cmd, aktionen);
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
    }

    private static void addOptionsVerstecken(Options options) {
        options.addOption("v", OPTION_VERSTECKEN, false, "Parameter für das Verstecken.");
        options.addOption("r", OPTION_DATEINAME_VERTEILREGEL, true, "Datei mit Verteilregeln für das Verstecken.");
        options.addOption("n", OPTION_DATEINAME_NUTZDATEN, true, "Datei der Nutzdaten für das Verstecken.");
        options.addOption("q", OPTION_DATEINAME_QUELLE, true, "Quelldatei für das Verstecken.");
        options.addOption("z", OPTION_DATEINAME_ZIEL, true, "Zieldatei für das Verstecken.");
        options.addOption("w", OPTION_VERRAUSCHEN, true, "Angabe für das Verrauschen.");
    }

    private static void addOptionsHolen(Options options) {
        options.addOption("h", OPTION_HOLEN, false, "Parameter für das Holen.");
        options.addOption("v", OPTION_DATEINAME_VERTEILREGEL, true, "Datei mit Verteilregeln für das Holen.");
        options.addOption("n", OPTION_DATEINAME_NUTZDATEN, true, "Datei der Nutzdaten für das Holen.");
        options.addOption("q", OPTION_DATEINAME_QUELLE, true, "Quelldatei für das Holen.");
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
        if (hatfehlendenParameter) {
            return 1;
        }
        aktionen.generiere(blockgroesse, nutzdaten, anzahlKanaele, bittiefe, dateiname);
        return 0;
    }

    private static int behandleVerstecken(CommandLine cmd, Aktionen aktionen) throws IOException {
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
        Verrauschoption verrauschoption = Verrauschoption.OHNE;
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
        aktionen.verstecke(dateiVerteilregel, dateiNutzdaten, dateiQuelle, dateiZiel, verrauschoption);
        return 0;
    }

    private static int behandleHolen(CommandLine cmd, Aktionen aktionen) throws IOException {
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
        aktionen.hole(dateiVerteilregel, dateiQuelle, dateiNutzdaten);
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
