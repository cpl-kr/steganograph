package de.platen.steganograph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.platen.crypt.Encryptor;
import de.platen.crypt.KeyVerwaltung;
import de.platen.steganograph.datentypen.AnzahlKanaele;
import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.datentypen.Bittiefe;
import de.platen.steganograph.datentypen.Blockgroesse;
import de.platen.steganograph.utils.DateiUtils;
import de.platen.steganograph.verteilregelgenerierung.KonfigurationVerteilregeln;
import de.platen.steganograph.verteilregelgenerierung.Verteilregelgenerierung;
import org.bouncycastle.openpgp.PGPPublicKeyRing;

public class Aktionen {

    private final AktionVersteckenInBild aktionVersteckenInBild;
    private final AktionVersteckenInAudio aktionVersteckenInAudio;
    private final AktionHolenAusBild aktionHolenAusBild;
    private final AktionHolenAusAudio aktionHolenAusAudio;
    private final AktionZufallsdatei aktionZufallsdatei;
    private final AktionInDatei aktionInDatei;
    private final AktionAusDatei aktionAusDatei;

    public Aktionen(final AktionVersteckenInBild aktionVersteckenInBild, final AktionVersteckenInAudio aktionVersteckenInAudio,
            final AktionHolenAusBild aktionHolenAusBild, final AktionHolenAusAudio aktionHolenAusAudio,
                    final AktionZufallsdatei aktionZufallsdatei, final AktionInDatei aktionInDatei, final AktionAusDatei aktionAusDatei) {
        this.aktionVersteckenInBild = aktionVersteckenInBild;
        this.aktionVersteckenInAudio = aktionVersteckenInAudio;
        this.aktionHolenAusBild = aktionHolenAusBild;
        this.aktionHolenAusAudio = aktionHolenAusAudio;
        this.aktionZufallsdatei = aktionZufallsdatei;
        this.aktionInDatei = aktionInDatei;
        this.aktionAusDatei = aktionAusDatei;
    }

    public void erzeugeKeyPaar(final String id, final String dateiPublicKey, final String dateiPrivateKey) {
        erzeugeKeyPaar(id, dateiPublicKey, dateiPrivateKey, null);
    }

    public void erzeugeKeyPaar(final String id, final String dateiPublicKey, final String dateiPrivateKey, String passwort) {
        KeyVerwaltung keyVerwaltung = new KeyVerwaltung();
        if (passwort != null) {
            keyVerwaltung.erzeugeUndSpeichereKeyPaar(dateiPublicKey, dateiPrivateKey, id, passwort);
        } else {
            keyVerwaltung.erzeugeUndSpeichereKeyPaar(dateiPublicKey, dateiPrivateKey, id);
        }
    }

    public void generiere(final String blockgroesse, final String anzahlNutzdaten, final String anzahlKanaele, final String bittiefe,
                          final String dateiname) throws IOException {
        generiere(blockgroesse, anzahlNutzdaten, anzahlKanaele, bittiefe, dateiname, null, null);
    }

    public void generiere(final String blockgroesse, final String anzahlNutzdaten, final String anzahlKanaele, final String bittiefe,
                          final String dateiname, final List<String> dateinamenPublicKey, final String passwort) throws IOException {
        final Blockgroesse blockgroesseKonfiguration = new Blockgroesse(Integer.parseInt(blockgroesse));
        final AnzahlNutzdaten nutzdatenKonfiguration = new AnzahlNutzdaten(Integer.parseInt(anzahlNutzdaten));
        final AnzahlKanaele anzahlKanaeleKonfiguration = new AnzahlKanaele(Integer.parseInt(anzahlKanaele));
        final Bittiefe bittiefeKonfiguration = new Bittiefe(Integer.parseInt(bittiefe));
        final KonfigurationVerteilregeln konfigurationGenerierung = new KonfigurationVerteilregeln(blockgroesseKonfiguration,
                nutzdatenKonfiguration, bittiefeKonfiguration, anzahlKanaeleKonfiguration);
        final Verteilregelgenerierung verteilregelgenerierung = new Verteilregelgenerierung(konfigurationGenerierung);
        final byte[] daten = verteilregelgenerierung.generiere();
        if ((dateinamenPublicKey != null) && !dateinamenPublicKey.isEmpty()) {
            final KeyVerwaltung keyVerwaltung = new KeyVerwaltung();
            final List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
            for (String dateinamePublicKey : dateinamenPublicKey) {
                pgpPublicKeyRingList.add(keyVerwaltung.leseZertifikatAusPublicKey(dateinamePublicKey));
            }
            Encryptor encryptor;
            if (passwort != null) {
                encryptor = new Encryptor(pgpPublicKeyRingList, passwort);
            } else {
                encryptor = new Encryptor(pgpPublicKeyRingList);
            }
            final byte[] datenEncrypted = encryptor.encrypt(daten);
            DateiUtils.schreibeDatei(dateiname, datenEncrypted);
        } else {
            DateiUtils.schreibeDatei(dateiname, daten);
        }
    }

    public void verstecke(String dateinameVerteilregel, String dateinameNutzdaten, String dateinameQuelle,
                          String dateinameZiel, Verrauschoption verrauschoption) throws IOException {
        verstecke(dateinameVerteilregel, dateinameNutzdaten, dateinameQuelle, dateinameZiel, verrauschoption, null, null);
    }

    public void verstecke(String dateinameVerteilregel, String dateinameNutzdaten, String dateinameQuelle,
                          String dateinameZiel, Verrauschoption verrauschoption, String dateiPrivateKey, String passwort) throws IOException {
        if (dateinameQuelle.toLowerCase().endsWith(".png") || dateinameQuelle.toLowerCase().endsWith(".bmp")) {
            this.aktionVersteckenInBild.versteckeInBild(dateinameVerteilregel, dateinameNutzdaten, dateinameQuelle,
                    dateinameZiel, verrauschoption, dateiPrivateKey, passwort);
        }
        if (dateinameQuelle.toLowerCase().endsWith(".wav")) {
            this.aktionVersteckenInAudio.versteckeNutzdatenInAudio(dateinameVerteilregel, dateinameNutzdaten,
                    dateinameQuelle, dateinameZiel, verrauschoption, dateiPrivateKey, passwort);
        }
    }

    public void verstecke(final String dateinameQuelle, final String dateinameZiel, final String laenge, final String offset, final String mitErzeugung) {
        if (Boolean.parseBoolean(mitErzeugung)) {
            this.aktionZufallsdatei.erzeugeZufallsdatei(dateinameZiel, Integer.parseInt(laenge));
        }
        this.aktionInDatei.schreibeInDatei(dateinameQuelle, dateinameZiel, Integer.parseInt(offset));
    }

    public void hole(String dateinameVerteilregel, String dateinameQuelle, String dateinameNutzdaten)
            throws IOException {
        hole(dateinameVerteilregel, dateinameQuelle, dateinameNutzdaten, null, null);
    }

    public void hole(String dateinameVerteilregel, String dateinameQuelle, String dateinameNutzdaten, String dateiPrivateKey, String passwort)
            throws IOException {
        if (dateinameQuelle.toLowerCase().endsWith(".png") || dateinameQuelle.toLowerCase().endsWith(".bmp")) {
            this.aktionHolenAusBild.holeAusBild(dateinameVerteilregel, dateinameQuelle, dateinameNutzdaten, dateiPrivateKey, passwort);
        }
        if (dateinameQuelle.toLowerCase().endsWith(".wav")) {
            this.aktionHolenAusAudio.holeNutzdatenAusAudio(dateinameVerteilregel, dateinameQuelle, dateinameNutzdaten, dateiPrivateKey, passwort);
        }
    }

    public void hole(final String dateinameQuelle, final String dateinameZiel, final String offset, String laenge) {
        this.aktionAusDatei.leseAusDatei(dateinameQuelle, dateinameZiel, Integer.parseInt(offset), Integer.parseInt(laenge));
    }
}
