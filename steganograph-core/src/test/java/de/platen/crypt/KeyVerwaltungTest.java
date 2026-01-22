package de.platen.crypt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import de.platen.steganograph.utils.DateiUtils;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.junit.Test;
import org.pgpainless.PGPainless;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

public class KeyVerwaltungTest {

    private static final String DATEI_PUBLIC_KEY = "src/test/resources/public.pgp";
    private static final String DATEI_PRIVATE_KEY = "src/test/resources/private.pgp";
    private static final String PASSWORT = "passwort";
    private static final String ID = "person";
    private KeyVerwaltung keyVerwaltung = new KeyVerwaltung();

    @Test
    public void testLesePublicKeyParameterNull() {
        try {
            keyVerwaltung.lesePublicKey(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("datei ist null oder leer.", e.getMessage());
        }
    }

    @Test
    public void testLesePublicKeyParameterLeer() {
        try {
            keyVerwaltung.lesePublicKey("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("datei ist null oder leer.", e.getMessage());
        }
    }

    @Test
    public void testLesePublicKey()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        erstelleDateiPublicKey(DATEI_PUBLIC_KEY);
        final PGPPublicKey pgpPublicKey = keyVerwaltung.lesePublicKey(DATEI_PUBLIC_KEY);
        assertNotNull(pgpPublicKey);
        loescheDatei(DATEI_PUBLIC_KEY);
    }

    @Test
    public void testLeseZertifikatAusPublicKeyParameterNull() {
        try {
            keyVerwaltung.leseZertifikatAusPublicKey(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("datei ist null oder leer.", e.getMessage());
        }
    }

    @Test
    public void testLeseZertifikatAusPublicKeyParameterLeer() {
        try {
            keyVerwaltung.leseZertifikatAusPublicKey("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("datei ist null oder leer.", e.getMessage());
        }
    }

    @Test
    public void testLeseZertifikatAusPublicKey()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        erstelleDateiPublicKey(DATEI_PUBLIC_KEY);
        final PGPPublicKeyRing pgpPublicKeyRing = keyVerwaltung.leseZertifikatAusPublicKey(DATEI_PUBLIC_KEY);
        assertNotNull(pgpPublicKeyRing);
        loescheDatei(DATEI_PUBLIC_KEY);
    }

    @Test
    public void testLesePrivateKeyMitPasswortParameterDateiNull() {
        try {
            keyVerwaltung.lesePrivateKeyMitPasswort(null, "cpl");
            fail();
        } catch (NullPointerException e) {
            assertEquals("datei ist null oder leer.", e.getMessage());
        }
    }

    @Test
    public void testLesePrivateKeyMitPasswortParameterDateiLeer() {
        try {
            keyVerwaltung.lesePrivateKeyMitPasswort("", "cpl");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("datei ist null oder leer.", e.getMessage());
        }
    }

    @Test
    public void testLesePrivateKeyMitPasswortParameterPasswortNull() {
        try {
            keyVerwaltung.lesePrivateKeyMitPasswort("test", null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("passwort ist null.", e.getMessage());
        }
    }

    @Test
    public void testLesePrivateKeyMitPasswort()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        erstelleDateiPrivateKeyMitPasswort(DATEI_PRIVATE_KEY, PASSWORT);
        final PGPPrivateKey pgpPrivateKey = keyVerwaltung.lesePrivateKeyMitPasswort(DATEI_PRIVATE_KEY, PASSWORT);
        assertNotNull(pgpPrivateKey);
        loescheDatei(DATEI_PRIVATE_KEY);
    }

    @Test
    public void testLesePrivateKeyParameterNull() {
        try {
            keyVerwaltung.lesePrivateKey(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("datei ist null oder leer.", e.getMessage());
        }
    }

    @Test
    public void testLesePrivateKeyParameterLeer() {
        try {
            keyVerwaltung.lesePrivateKey("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("datei ist null oder leer.", e.getMessage());
        }
    }

    @Test
    public void testLesePrivateKey()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        erstelleDateiPrivateKeyOhnePasswort(DATEI_PRIVATE_KEY);
        final PGPSecretKeyRing pgpSecretKeyRing = keyVerwaltung.lesePrivateKey(DATEI_PRIVATE_KEY);
        assertNotNull(pgpSecretKeyRing);
        loescheDatei(DATEI_PRIVATE_KEY);
    }

    @Test
    public void testErzeugeUndSpeichereKeyPaarOhnePasswort()  {
        keyVerwaltung.erzeugeUndSpeichereKeyPaar(DATEI_PUBLIC_KEY, DATEI_PRIVATE_KEY, ID);
        File filePublicKey = new File(DATEI_PUBLIC_KEY);
        File filePrivateKey = new File(DATEI_PRIVATE_KEY);
        assertTrue(filePublicKey.exists());
        assertTrue(filePrivateKey.exists());
        filePublicKey.delete();
        filePrivateKey.delete();
    }
    @Test
    public void testErzeugeUndSpeichereKeyPaarMitPasswort()  {
        keyVerwaltung.erzeugeUndSpeichereKeyPaar(DATEI_PUBLIC_KEY, DATEI_PRIVATE_KEY, ID, PASSWORT);
        File filePublicKey = new File(DATEI_PUBLIC_KEY);
        File filePrivateKey = new File(DATEI_PRIVATE_KEY);
        assertTrue(filePublicKey.exists());
        assertTrue(filePrivateKey.exists());
        filePublicKey.delete();
        filePrivateKey.delete();
    }
    private void erstelleDateiPublicKey(final String datei)
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        PGPSecretKeyRing pgpSecretKeyRing = PGPainless.generateKeyRing().modernKeyRing("person");
        PGPPublicKeyRing pgpPublicKeyRing = PGPainless.extractCertificate(pgpSecretKeyRing);
        String asciiArmored = PGPainless.asciiArmor(pgpPublicKeyRing);
        DateiUtils.schreibeDatei(datei, asciiArmored.getBytes());
    }

    private void erstelleDateiPrivateKeyMitPasswort(final String datei, String passwort)
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        PGPSecretKeyRing pgpSecretKeyRing = PGPainless.generateKeyRing().modernKeyRing("person", passwort);
        String asciiArmored = PGPainless.asciiArmor(pgpSecretKeyRing);
        DateiUtils.schreibeDatei(datei, asciiArmored.getBytes());
    }

    private void erstelleDateiPrivateKeyOhnePasswort(final String datei)
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        PGPSecretKeyRing pgpSecretKeyRing = PGPainless.generateKeyRing().modernKeyRing("person");
        String asciiArmored = PGPainless.asciiArmor(pgpSecretKeyRing);
        DateiUtils.schreibeDatei(datei, asciiArmored.getBytes());
    }

    private void loescheDatei(String datei) {
        File file = new File(datei);
        if (file.exists()) {
            file.delete();
        }
    }
}
