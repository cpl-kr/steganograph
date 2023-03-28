package de.platen.crypt;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.junit.Test;
import org.mockito.Mockito;
import org.pgpainless.PGPainless;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class EncryptorTest {

    @Test
    public void testKonstruktor1ParameterPgpPublicKeyRingNull() {
        try {
            new Encryptor(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("pgpPublicKeyRing ist null.", e.getMessage());
        }
    }

    @Test
    public void testKonstruktor1ParameterPgpPublicKeyRingLeer() {
        try {
            new Encryptor(new ArrayList<>());
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("pgpPublicKeyRing ist leer.", e.getMessage());
        }
    }

    @Test
    public void testKonstruktor2ParameterPgpPublicKeyRingNull() {
        try {
            new Encryptor(null, "passwort");
            fail();
        } catch (NullPointerException e) {
            assertEquals("pgpPublicKeyRing ist null.", e.getMessage());
        }
    }

    @Test
    public void testKonstruktor2ParameterPgpPublicKeyRingLeer() {
        try {
            new Encryptor(new ArrayList<>(), "passwort");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("pgpPublicKeyRing ist leer.", e.getMessage());
        }
    }

    @Test
    public void testKonstruktor2ParameterPasswortNull() {
        List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(Mockito.mock(PGPPublicKeyRing.class));
        try {
            new Encryptor(pgpPublicKeyRingList, null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("passwort ist null oder leer.", e.getMessage());
        }
    }

    @Test
    public void testKonstruktor2ParameterPasswortLeer() {
        List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(Mockito.mock(PGPPublicKeyRing.class));
        try {
            new Encryptor(pgpPublicKeyRingList, "");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("passwort ist null oder leer.", e.getMessage());
        }
    }

    @Test
    public void testEncryptDataNull() {
        List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(Mockito.mock(PGPPublicKeyRing.class));
        Encryptor encryptor = new Encryptor(pgpPublicKeyRingList);
        try {
            encryptor.encrypt(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("data ist null.", e.getMessage());
        }
    }

    @Test
    public void testEncrypt() throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        PGPSecretKeyRing pgpSecretKeyRing = PGPainless.generateKeyRing().modernKeyRing("person");
        PGPPublicKeyRing pgpPublicKeyRing = PGPainless.extractCertificate(pgpSecretKeyRing);
        List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(pgpPublicKeyRing);
        Encryptor encryptor = new Encryptor(pgpPublicKeyRingList);
        byte[] data = { 0x01, 0x02, 0x03 };
        byte[] dataEncrypted = encryptor.encrypt(data);
        assertNotNull(dataEncrypted);
        assertTrue(dataEncrypted.length > 3);
    }


    @Test
    public void testEncryptMitPasswort() throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        String passwort = "passwort";
        PGPSecretKeyRing pgpSecretKeyRing = PGPainless.generateKeyRing().modernKeyRing("person", passwort);
        PGPPublicKeyRing pgpPublicKeyRing = PGPainless.extractCertificate(pgpSecretKeyRing);
        List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(pgpPublicKeyRing);
        Encryptor encryptor = new Encryptor(pgpPublicKeyRingList, passwort);
        byte[] data = { 0x01, 0x02, 0x03 };
        byte[] dataEncrypted = encryptor.encrypt(data);
        assertNotNull(dataEncrypted);
        assertTrue(dataEncrypted.length > 3);
    }

    @Test
    public void testEncryptMehrereRecipienten()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        String passwort = "passwort";
        PGPSecretKeyRing pgpSecretKeyRing1 = PGPainless.generateKeyRing().modernKeyRing("person1", passwort);
        PGPSecretKeyRing pgpSecretKeyRing2 = PGPainless.generateKeyRing().modernKeyRing("person2");
        PGPPublicKeyRing pgpPublicKeyRing1 = PGPainless.extractCertificate(pgpSecretKeyRing1);
        PGPPublicKeyRing pgpPublicKeyRing2 = PGPainless.extractCertificate(pgpSecretKeyRing2);
        List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(pgpPublicKeyRing1);
        pgpPublicKeyRingList.add(pgpPublicKeyRing2);
        Encryptor encryptor = new Encryptor(pgpPublicKeyRingList, passwort);
        byte[] data = { 0x01, 0x02, 0x03 };
        byte[] dataEncrypted = encryptor.encrypt(data);
        assertNotNull(dataEncrypted);
        assertTrue(dataEncrypted.length > 3);
    }
    @Test
    public void testEncryptInputStreamNull() {
        List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(Mockito.mock(PGPPublicKeyRing.class));
        Encryptor encryptor = new Encryptor(pgpPublicKeyRingList);
        try {
            encryptor.encrypt(null, new ByteArrayOutputStream());
            fail();
        } catch (NullPointerException e) {
            assertEquals("inputStream ist null.", e.getMessage());
        }
    }

    @Test
    public void testEncryptOutputStreamNull() {
        List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(Mockito.mock(PGPPublicKeyRing.class));
        Encryptor encryptor = new Encryptor(pgpPublicKeyRingList);
        try {
            encryptor.encrypt(new ByteArrayInputStream(new byte[1]), null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("outputStream ist null.", e.getMessage());
        }
    }

    @Test
    public void testEncryptMitStreams()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        PGPSecretKeyRing pgpSecretKeyRing = PGPainless.generateKeyRing().modernKeyRing("person");
        PGPPublicKeyRing pgpPublicKeyRing = PGPainless.extractCertificate(pgpSecretKeyRing);
        List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(pgpPublicKeyRing);
        Encryptor encryptor = new Encryptor(pgpPublicKeyRingList);
        byte[] data = { 0x01, 0x02, 0x03 };
        final InputStream inputStream = new ByteArrayInputStream(data);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        encryptor.encrypt(inputStream, byteArrayOutputStream);
        byte[] dataEncrypted = byteArrayOutputStream.toByteArray();
        assertNotNull(dataEncrypted);
        assertTrue(dataEncrypted.length > 3);
    }
}
