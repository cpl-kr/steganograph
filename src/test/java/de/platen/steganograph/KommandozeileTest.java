package de.platen.steganograph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class KommandozeileTest {

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
    private static final String HINWEIS_VERRAUSCHEN = "Für das Verrauschen muss 'ohne' oder 'nutzdatenbereich' oder 'alles' angegeben werden.";

    private static final String HINWEIS_BLOCKGROESSE = "Für die Verteilregelgenerierung muss die Blockgröße angegeben werden.";
    private static final String HINWEIS_NUTZDATEN = "Für die Verteilregelgenerierung muss die Anzahl der Nutzdaten (Bytes) angegeben werden.";
    private static final String HINWEIS_ANZAHL_KANAELE = "Für die Verteilregelgenerierung muss die Anzahl der Kanäle angegeben werden.";
    private static final String HINWEIS_BITTIEFE = "Für die Verteilregelgenerierung muss die Bittiefe angegeben werden.";
    private static final String HINWEIS_DATEINAME = "Für die Verteilregelgenerierung muss ein Dateiname angegeben werden.";

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

    private static final String DATEINAME_VERTEILREGEL = "src/test/resources/verteilregel";
    private static final String DATEINAME_PUBLIC_KEYS = "src/test/resources/public1.pgp,src/test/resources/public2.pgp";
    private static final String DATEINAME_PRIVATE_KEY = "src/test/resources/private.pgp";

    private static final String OPTION_KEY = "key";
    private static final String OPTION_ID = "id";
    private static final String OPTION_DATEI_PUBLIC_KEY = "dateiPublicKey";
    private static final String OPTION_DATEI_PRIVATE_KEY = "dateiPrivateKey";
    private static final String OPTION_PASSWORT_KEY = "passwort";

    private static final String HINWEIS_ID = "Für das Erzeugen eines Schlüsselpaares muss die ID angegeben werden.";
    private static final String HINWEIS_DATEI_PUBLIC_KEY = "Für das Erzeugen eines Schlüsselpaares muss der Dateiname des öffentlichen Schlüssels angegeben werden.";
    private static final String HINWEIS_DATEI_PRIVATE_KEY = "Für das Erzeugen eines Schlüsselpaares muss der Dateiname des privaten Schlüssels angegeben werden.";

    private Kommandozeile kommandozeile;
    private final Aktionen aktionen = Mockito.mock(Aktionen.class);
    private PrintStream ps;
    private OutputStream os;

    @Before
    public void errorStream() throws Exception {
        os = new ByteArrayOutputStream();
        ps = new PrintStream(os);
        System.setErr(ps);
        kommandozeile = new Kommandozeile(aktionen);
    }

    @After
    public void standardStream() {
        ps.close();
        System.setErr(System.out);
    }

    @Test
    public void testBehandleKommandozeileParameterFuerGenerierungAlleOhneKey() throws Exception {
        String[] blockgroesse = erzeugeParameter(OPTION_BLOCKGROESSE, "100");
        String[] nutzdaten = erzeugeParameter(OPTION_NUTZDATEN, "10");
        String[] anzahlKanaele = erzeugeParameter(OPTION_ANZAHL_KANAELE, "4");
        String[] bittiefe = erzeugeParameter(OPTION_BITTIEFE, "2");
        String[] dateiname = erzeugeParameter(OPTION_DATEINAME, DATEINAME_VERTEILREGEL);
        String[] args = { "--" + OPTION_VERTEILREGELGENERIERUNG, blockgroesse[0], blockgroesse[1], nutzdaten[0],
                nutzdaten[1], anzahlKanaele[0], anzahlKanaele[1], bittiefe[0], bittiefe[1], dateiname[0],
                dateiname[1] };
        assertEquals(0, kommandozeile.behandleKommandozeile(args));
        assertTrue(os.toString().isEmpty());
    }

    @Test
    public void testBehandleKommandozeileParameterFuerGenerierungAlleMitKey() throws Exception {
        String[] blockgroesse = erzeugeParameter(OPTION_BLOCKGROESSE, "100");
        String[] nutzdaten = erzeugeParameter(OPTION_NUTZDATEN, "10");
        String[] anzahlKanaele = erzeugeParameter(OPTION_ANZAHL_KANAELE, "4");
        String[] bittiefe = erzeugeParameter(OPTION_BITTIEFE, "2");
        String[] dateiname = erzeugeParameter(OPTION_DATEINAME, DATEINAME_VERTEILREGEL);
        String[] key = erzeugeParameter(OPTION_PRIVATE_KEY, DATEINAME_PRIVATE_KEY);
        String[] passwort = erzeugeParameter(OPTION_PASSWORT, "passwort");
        String[] args = { "--" + OPTION_VERTEILREGELGENERIERUNG, blockgroesse[0], blockgroesse[1], nutzdaten[0],
                nutzdaten[1], anzahlKanaele[0], anzahlKanaele[1], bittiefe[0], bittiefe[1], dateiname[0],
                dateiname[1], key[0], key[1], passwort[0], passwort[1] };
        assertEquals(0, kommandozeile.behandleKommandozeile(args));
        assertTrue(os.toString().isEmpty());
    }

    @Test
    public void testBehandleKommandozeileParameterFuerGenerierungNurHauptparameter() throws Exception {
        String[] args = { "--" + OPTION_VERTEILREGELGENERIERUNG };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertTrue(ausgabe.contains(HINWEIS_BLOCKGROESSE));
        assertTrue(ausgabe.contains(HINWEIS_NUTZDATEN));
        assertTrue(ausgabe.contains(HINWEIS_ANZAHL_KANAELE));
        assertTrue(ausgabe.contains(HINWEIS_BITTIEFE));
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME));
    }

    @Test
    public void testBehandleKommandozeileParameterFuerGenerierungOhneBlockgroesse() throws Exception {
        String[] nutzdaten = erzeugeParameter(OPTION_NUTZDATEN, "500");
        String[] anzahlKanaele = erzeugeParameter(OPTION_ANZAHL_KANAELE, "4");
        String[] bittiefe = erzeugeParameter(OPTION_BITTIEFE, "2");
        String[] dateiname = erzeugeParameter(OPTION_DATEINAME, DATEINAME_VERTEILREGEL);
        String[] args = { "--" + OPTION_VERTEILREGELGENERIERUNG, nutzdaten[0], nutzdaten[1], anzahlKanaele[0],
                anzahlKanaele[1], bittiefe[0], bittiefe[1], dateiname[0], dateiname[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertTrue(ausgabe.contains(HINWEIS_BLOCKGROESSE));
        assertFalse(ausgabe.contains(HINWEIS_NUTZDATEN));
        assertFalse(ausgabe.contains(HINWEIS_ANZAHL_KANAELE));
        assertFalse(ausgabe.contains(HINWEIS_BITTIEFE));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME));
    }

    @Test
    public void testBehandleKommandozeileParameterFuerGenerierungOhneNutzdaten() throws Exception {
        String[] blockgroesse = erzeugeParameter(OPTION_BLOCKGROESSE, "1000");
        String[] anzahlKanaele = erzeugeParameter(OPTION_ANZAHL_KANAELE, "4");
        String[] bittiefe = erzeugeParameter(OPTION_BITTIEFE, "2");
        String[] dateiname = erzeugeParameter(OPTION_DATEINAME, DATEINAME_VERTEILREGEL);
        String[] args = { "--" + OPTION_VERTEILREGELGENERIERUNG, blockgroesse[0], blockgroesse[1], anzahlKanaele[0],
                anzahlKanaele[1], bittiefe[0], bittiefe[1], dateiname[0], dateiname[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertFalse(ausgabe.contains(HINWEIS_BLOCKGROESSE));
        assertTrue(ausgabe.contains(HINWEIS_NUTZDATEN));
        assertFalse(ausgabe.contains(HINWEIS_ANZAHL_KANAELE));
        assertFalse(ausgabe.contains(HINWEIS_BITTIEFE));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME));
    }

    @Test
    public void testBehandleKommandozeileParameterFuerGenerierungOhneAnzahlKanaele() throws Exception {
        String[] blockgroesse = erzeugeParameter(OPTION_BLOCKGROESSE, "1000");
        String[] nutzdaten = erzeugeParameter(OPTION_NUTZDATEN, "500");
        String[] bittiefe = erzeugeParameter(OPTION_BITTIEFE, "2");
        String[] dateiname = erzeugeParameter(OPTION_DATEINAME, DATEINAME_VERTEILREGEL);
        String[] args = { "--" + OPTION_VERTEILREGELGENERIERUNG, blockgroesse[0], blockgroesse[1], nutzdaten[0],
                nutzdaten[1], bittiefe[0], bittiefe[1], dateiname[0], dateiname[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertFalse(ausgabe.contains(HINWEIS_BLOCKGROESSE));
        assertFalse(ausgabe.contains(HINWEIS_NUTZDATEN));
        assertTrue(ausgabe.contains(HINWEIS_ANZAHL_KANAELE));
        assertFalse(ausgabe.contains(HINWEIS_BITTIEFE));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME));
    }

    @Test
    public void testBehandleKommandozeileParameterFuerGenerierungOhneBittiefe() throws Exception {
        String[] blockgroesse = erzeugeParameter(OPTION_BLOCKGROESSE, "1000");
        String[] nutzdaten = erzeugeParameter(OPTION_NUTZDATEN, "500");
        String[] anzahlKanaele = erzeugeParameter(OPTION_ANZAHL_KANAELE, "4");
        String[] dateiname = erzeugeParameter(OPTION_DATEINAME, DATEINAME_VERTEILREGEL);
        String[] args = { "--" + OPTION_VERTEILREGELGENERIERUNG, blockgroesse[0], blockgroesse[1], nutzdaten[0],
                nutzdaten[1], anzahlKanaele[0], anzahlKanaele[1], dateiname[0], dateiname[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertFalse(ausgabe.contains(HINWEIS_BLOCKGROESSE));
        assertFalse(ausgabe.contains(HINWEIS_NUTZDATEN));
        assertFalse(ausgabe.contains(HINWEIS_ANZAHL_KANAELE));
        assertTrue(ausgabe.contains(HINWEIS_BITTIEFE));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME));
    }

    @Test
    public void testBehandleKommandozeileParameterFuerGenerierungOhneDateiname() throws Exception {
        String[] blockgroesse = erzeugeParameter(OPTION_BLOCKGROESSE, "1000");
        String[] nutzdaten = erzeugeParameter(OPTION_NUTZDATEN, "500");
        String[] anzahlKanaele = erzeugeParameter(OPTION_ANZAHL_KANAELE, "4");
        String[] bittiefe = erzeugeParameter(OPTION_BITTIEFE, "2");
        String[] args = { "--" + OPTION_VERTEILREGELGENERIERUNG, blockgroesse[0], blockgroesse[1], nutzdaten[0],
                nutzdaten[1], anzahlKanaele[0], anzahlKanaele[1], bittiefe[0], bittiefe[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertFalse(ausgabe.contains(HINWEIS_BLOCKGROESSE));
        assertFalse(ausgabe.contains(HINWEIS_NUTZDATEN));
        assertFalse(ausgabe.contains(HINWEIS_ANZAHL_KANAELE));
        assertFalse(ausgabe.contains(HINWEIS_BITTIEFE));
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME));
    }

    @Test
    public void testBehandleKommandozeileParameterFuerVersteckenAlleOhneKey() throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] dateiZiel = erzeugeParameter(OPTION_DATEINAME_ZIEL, "ziel");
        String[] args = { "--" + OPTION_VERSTECKEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiNutzdaten[0],
                dateiNutzdaten[1], dateiQuelle[0], dateiQuelle[1], dateiZiel[0], dateiZiel[1] };
        assertEquals(0, kommandozeile.behandleKommandozeile(args));
        assertTrue(os.toString().isEmpty());
    }

    @Test
    public void testBehandleKommandozeileParameterFuerVersteckenAlleMitKey() throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] dateiZiel = erzeugeParameter(OPTION_DATEINAME_ZIEL, "ziel");
        String[] key = erzeugeParameter(OPTION_PUBLIC_KEYS, DATEINAME_PUBLIC_KEYS);
        String[] passwort = erzeugeParameter(OPTION_PASSWORT, "passwort");
        String[] args = { "--" + OPTION_VERSTECKEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiNutzdaten[0],
                dateiNutzdaten[1], dateiQuelle[0], dateiQuelle[1], dateiZiel[0], dateiZiel[1], key[0], key[1], passwort[0], passwort[1] };
        assertEquals(0, kommandozeile.behandleKommandozeile(args));
        assertTrue(os.toString().isEmpty());
    }

    @Test
    public void testBehandleKommandozeileParameterFuerVersteckenAlleUndOptionVerrauscheOhne() throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] dateiZiel = erzeugeParameter(OPTION_DATEINAME_ZIEL, "ziel");
        String[] verrauschen = erzeugeParameter(OPTION_VERRAUSCHEN, VERRAUSCHEN_WERT_OHNE);
        String[] args = { "--" + OPTION_VERSTECKEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiNutzdaten[0],
                dateiNutzdaten[1], dateiQuelle[0], dateiQuelle[1], dateiZiel[0], dateiZiel[1], verrauschen[0],
                verrauschen[1] };
        assertEquals(0, kommandozeile.behandleKommandozeile(args));
        assertTrue(os.toString().isEmpty());
    }

    @Test
    public void testBehandleKommandozeileParameterFuerVersteckenAlleUndOptionVerrauscheNutzdatenbereich()
            throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] dateiZiel = erzeugeParameter(OPTION_DATEINAME_ZIEL, "ziel");
        String[] verrauschen = erzeugeParameter(OPTION_VERRAUSCHEN, VERRAUSCHEN_WERT_NUTZDATENBEREICH);
        String[] args = { "--" + OPTION_VERSTECKEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiNutzdaten[0],
                dateiNutzdaten[1], dateiQuelle[0], dateiQuelle[1], dateiZiel[0], dateiZiel[1], verrauschen[0],
                verrauschen[1] };
        assertEquals(0, kommandozeile.behandleKommandozeile(args));
        assertTrue(os.toString().isEmpty());
    }

    @Test
    public void testBehandleKommandozeileParameterFuerVersteckenAlleUndOptionVerrauscheAlles() throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] dateiZiel = erzeugeParameter(OPTION_DATEINAME_ZIEL, "ziel");
        String[] verrauschen = erzeugeParameter(OPTION_VERRAUSCHEN, VERRAUSCHEN_WERT_ALLES);
        String[] args = { "--" + OPTION_VERSTECKEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiNutzdaten[0],
                dateiNutzdaten[1], dateiQuelle[0], dateiQuelle[1], dateiZiel[0], dateiZiel[1], verrauschen[0],
                verrauschen[1] };
        assertEquals(0, kommandozeile.behandleKommandozeile(args));
        assertTrue(os.toString().isEmpty());
    }

    @Test
    public void testBehandleKommandozeileParameterFuerVersteckenAlleUndOptionVerrauscheFalscherWert() throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] dateiZiel = erzeugeParameter(OPTION_DATEINAME_ZIEL, "ziel");
        String[] verrauschen = erzeugeParameter(OPTION_VERRAUSCHEN, "falsch");
        String[] args = { "--" + OPTION_VERSTECKEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiNutzdaten[0],
                dateiNutzdaten[1], dateiQuelle[0], dateiQuelle[1], dateiZiel[0], dateiZiel[1], verrauschen[0],
                verrauschen[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertTrue(ausgabe.contains(HINWEIS_VERRAUSCHEN));
    }

    @Test
    public void testBehandleKommandozeileParameterFueVersteckenNurHauptparameter() throws Exception {
        String[] args = { "--" + OPTION_VERSTECKEN };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_VERTEILREGEL));
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_NUTZDATEN));
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_QUELLE));
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_ZIEL));
    }

    @Test
    public void testBehandleKommandozeileParameterFueVersteckenOhneDateiVersteckregel() throws Exception {
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] dateiZiel = erzeugeParameter(OPTION_DATEINAME_ZIEL, "ziel");
        String[] args = { "--" + OPTION_VERSTECKEN, dateiNutzdaten[0], dateiNutzdaten[1], dateiQuelle[0],
                dateiQuelle[1], dateiZiel[0], dateiZiel[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_VERTEILREGEL));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_NUTZDATEN));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_QUELLE));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_ZIEL));
    }

    @Test
    public void testBehandleKommandozeileParameterFueVersteckenOhneDateiNutzdaten() throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] dateiZiel = erzeugeParameter(OPTION_DATEINAME_ZIEL, "ziel");
        String[] args = { "--" + OPTION_VERSTECKEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiQuelle[0],
                dateiQuelle[1], dateiZiel[0], dateiZiel[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_VERTEILREGEL));
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_NUTZDATEN));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_QUELLE));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_ZIEL));
    }

    @Test
    public void testBehandleKommandozeileParameterFueVersteckenOhneDateiQuelle() throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] dateiZiel = erzeugeParameter(OPTION_DATEINAME_ZIEL, "ziel");
        String[] args = { "--" + OPTION_VERSTECKEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiNutzdaten[0],
                dateiNutzdaten[1], dateiZiel[0], dateiZiel[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_VERTEILREGEL));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_NUTZDATEN));
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_QUELLE));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_ZIEL));
    }

    @Test
    public void testBehandleKommandozeileParameterFueVersteckenOhneDateiZiel() throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] args = { "--" + OPTION_VERSTECKEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiNutzdaten[0],
                dateiNutzdaten[1], dateiQuelle[0], dateiQuelle[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_VERTEILREGEL));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_NUTZDATEN));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_QUELLE));
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_ZIEL));
    }

    @Test
    public void testBehandleKommandozeileParameterFuerHolenAlleOhneKey() throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] args = { "--" + OPTION_HOLEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiNutzdaten[0],
                dateiNutzdaten[1], dateiQuelle[0], dateiQuelle[1] };
        assertEquals(0, kommandozeile.behandleKommandozeile(args));
        assertTrue(os.toString().isEmpty());
    }

    @Test
    public void testBehandleKommandozeileParameterFuerHolenAlleMitKey() throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] key = erzeugeParameter(OPTION_PUBLIC_KEYS, DATEINAME_PUBLIC_KEYS);
        String[] passwort = erzeugeParameter(OPTION_PASSWORT, "passwort");
        String[] args = { "--" + OPTION_HOLEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiNutzdaten[0],
                dateiNutzdaten[1], dateiQuelle[0], dateiQuelle[1], key[0], key[1], passwort[0], passwort[1] };
        assertEquals(0, kommandozeile.behandleKommandozeile(args));
        assertTrue(os.toString().isEmpty());
    }

    @Test
    public void testBehandleKommandozeileParameterFueHolenNurHauptparameter() throws Exception {
        String[] args = { "--" + OPTION_HOLEN };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_VERTEILREGEL));
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_NUTZDATEN));
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_QUELLE));
    }

    @Test
    public void testBehandleKommandozeileParameterFueHolenOhneDateiVersteckregel() throws Exception {
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] args = { "--" + OPTION_HOLEN, dateiNutzdaten[0], dateiNutzdaten[1], dateiQuelle[0], dateiQuelle[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_VERTEILREGEL));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_NUTZDATEN));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_QUELLE));
    }

    @Test
    public void testBehandleKommandozeileParameterFueHolenOhneDateiNutzdaten() throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiQuelle = erzeugeParameter(OPTION_DATEINAME_QUELLE, "quelle");
        String[] args = { "--" + OPTION_HOLEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiQuelle[0],
                dateiQuelle[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_VERTEILREGEL));
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_NUTZDATEN));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_QUELLE));
    }

    @Test
    public void testBehandleKommandozeileParameterFueHolenOhneDateiQuelle() throws Exception {
        String[] dateiVersteckregel = erzeugeParameter(OPTION_DATEINAME_VERTEILREGEL, "versteckregel");
        String[] dateiNutzdaten = erzeugeParameter(OPTION_DATEINAME_NUTZDATEN, "nutzdaten");
        String[] args = { "--" + OPTION_HOLEN, dateiVersteckregel[0], dateiVersteckregel[1], dateiNutzdaten[0],
                dateiNutzdaten[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_VERTEILREGEL));
        assertFalse(ausgabe.contains(HINWEIS_DATEINAME_NUTZDATEN));
        assertTrue(ausgabe.contains(HINWEIS_DATEINAME_QUELLE));
    }

    @Test
    public void testBehandleKommandozeileParameterFuerKeyAlle() throws Exception {
        String[] id = erzeugeParameter(OPTION_ID, "id");
        String[] dateiPublicKey = erzeugeParameter(OPTION_DATEI_PUBLIC_KEY, "public.pgp");
        String[] dateiPrivateKey = erzeugeParameter(OPTION_DATEI_PRIVATE_KEY, "private.pgp");
        String[] passwort = erzeugeParameter(OPTION_PASSWORT_KEY, "passwort");
        String[] args = { "--" + OPTION_KEY, id[0], id[1], dateiPublicKey[0], dateiPublicKey[1], dateiPrivateKey[0], dateiPrivateKey[1], passwort[0], passwort[1] };
        assertEquals(0, kommandozeile.behandleKommandozeile(args));
        assertTrue(os.toString().isEmpty());
    }

    @Test
    public void testBehandleKommandozeileParameterFuerKeyOhnePasswort() throws Exception {
        String[] id = erzeugeParameter(OPTION_ID, "id");
        String[] dateiPublicKey = erzeugeParameter(OPTION_DATEI_PUBLIC_KEY, "public.pgp");
        String[] dateiPrivateKey = erzeugeParameter(OPTION_DATEI_PRIVATE_KEY, "private.pgp");
        String[] args = { "--" + OPTION_KEY, id[0], id[1], dateiPublicKey[0], dateiPublicKey[1], dateiPrivateKey[0], dateiPrivateKey[1] };
        assertEquals(0, kommandozeile.behandleKommandozeile(args));
        assertTrue(os.toString().isEmpty());
    }

    @Test
    public void testBehandleKommandozeileParameterFuerKeyOhneId() throws Exception {
        String[] dateiPublicKey = erzeugeParameter(OPTION_DATEI_PUBLIC_KEY, "public.pgp");
        String[] dateiPrivateKey = erzeugeParameter(OPTION_DATEI_PRIVATE_KEY, "private.pgp");
        String[] args = { "--" + OPTION_KEY, dateiPublicKey[0], dateiPublicKey[1], dateiPrivateKey[0], dateiPrivateKey[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertFalse(ausgabe.contains(HINWEIS_DATEI_PUBLIC_KEY));
        assertFalse(ausgabe.contains(HINWEIS_DATEI_PRIVATE_KEY));
    }

    @Test
    public void testBehandleKommandozeileParameterFuerKeyOhnePublicKey() throws Exception {
        String[] id = erzeugeParameter(OPTION_ID, "id");
        String[] dateiPrivateKey = erzeugeParameter(OPTION_DATEI_PRIVATE_KEY, "private.pgp");
        String[] args = { "--" + OPTION_KEY, id[0], id[1], dateiPrivateKey[0], dateiPrivateKey[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertFalse(ausgabe.contains(HINWEIS_ID));
        assertFalse(ausgabe.contains(HINWEIS_DATEI_PRIVATE_KEY));
    }

    @Test
    public void testBehandleKommandozeileParameterFuerKeyOhnePrivateKey() throws Exception {
        String[] id = erzeugeParameter(OPTION_ID, "id");
        String[] dateiPublicKey = erzeugeParameter(OPTION_DATEI_PUBLIC_KEY, "public.pgp");
        String[] args = { "--" + OPTION_KEY, id[0], id[1], dateiPublicKey[0], dateiPublicKey[1] };
        assertEquals(1, kommandozeile.behandleKommandozeile(args));
        String ausgabe = os.toString();
        assertFalse(ausgabe.contains(HINWEIS_ID));
        assertFalse(ausgabe.contains(HINWEIS_DATEI_PUBLIC_KEY));
    }

    private static String[] erzeugeParameter(String name, String wert) {
        return new String[] { "--" + name, wert };
    }
}
