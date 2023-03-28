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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class DecryptorTest {

    @Test
    public void testKonstruktor1ParameterPgpSecretKeyRingNull() {
        try {
            new Decryptor(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("pgpSecretKeyRing ist null.", e.getMessage());
        }
    }

    @Test
    public void testKonstruktor2ParameterPgpSecretKeyRingNull() {
        try {
            new Decryptor(null, "test");
            fail();
        } catch (NullPointerException e) {
            assertEquals("pgpSecretKeyRing ist null.", e.getMessage());
        }
    }

    @Test
    public void testKonstruktor2ParameterPasswortNull() {
        try {
            new Decryptor(Mockito.mock(PGPSecretKeyRing.class), null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("passwort ist null oder leer.", e.getMessage());
        }
    }

    @Test
    public void testKonstruktor2ParameterPasswortLeer() {
        try {
            new Decryptor(Mockito.mock(PGPSecretKeyRing.class), "");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("passwort ist null oder leer.", e.getMessage());
        }
    }

    @Test
    public void testDecryptEncryptedDataNull() {
        final Decryptor decryptor = new Decryptor(Mockito.mock(PGPSecretKeyRing.class));
        try {
            decryptor.decrypt(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("encryptedData ist null.", e.getMessage());
        }
    }

    @Test
    public void testDecrypt() throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        final PGPSecretKeyRing pgpSecretKeyRing = PGPainless.generateKeyRing().modernKeyRing("person");
        final PGPPublicKeyRing pgpPublicKeyRing = PGPainless.extractCertificate(pgpSecretKeyRing);
        final List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(pgpPublicKeyRing);
        final Encryptor encryptor = new Encryptor(pgpPublicKeyRingList);
        byte[] data = { 0x01, 0x02, 0x03 };
        byte[] dataEncrypted = encryptor.encrypt(data);
        final Decryptor decryptor = new Decryptor(pgpSecretKeyRing);
        byte[] dataDecrypted = decryptor.decrypt(dataEncrypted);
        assertNotNull(dataDecrypted);
        assertArrayEquals(data, dataDecrypted);
    }

    @Test
    public void testDecryptMitPasswort() throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        final String passwort = "passwort";
        final PGPSecretKeyRing pgpSecretKeyRing = PGPainless.generateKeyRing().modernKeyRing("person", passwort);
        final PGPPublicKeyRing pgpPublicKeyRing = PGPainless.extractCertificate(pgpSecretKeyRing);
        final List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(pgpPublicKeyRing);
        final Encryptor encryptor = new Encryptor(pgpPublicKeyRingList, passwort);
        byte[] data = { 0x01, 0x02, 0x03 };
        byte[] dataEncrypted = encryptor.encrypt(data);
        final Decryptor decryptor = new Decryptor(pgpSecretKeyRing, passwort);
        byte[] dataDecrypted = decryptor.decrypt(dataEncrypted);
        assertNotNull(dataDecrypted);
        assertArrayEquals(data, dataDecrypted);
    }

    @Test
    public void testDecryptInputStreamNull() {
        final Decryptor decryptor = new Decryptor(Mockito.mock(PGPSecretKeyRing.class));
        try {
            decryptor.decrypt(null, new ByteArrayOutputStream());
            fail();
        } catch (NullPointerException e) {
            assertEquals("inputStream ist null.", e.getMessage());
        }
    }

    @Test
    public void testDecryptOutputStreamNull() {
        final Decryptor decryptor = new Decryptor(Mockito.mock(PGPSecretKeyRing.class));
        try {
            decryptor.decrypt(new ByteArrayInputStream(new byte[0]), null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("outputStream ist null.", e.getMessage());
        }
    }

    @Test
    public void testDecryptMitStreams()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        final PGPSecretKeyRing pgpSecretKeyRing = PGPainless.generateKeyRing().modernKeyRing("person");
        final PGPPublicKeyRing pgpPublicKeyRing = PGPainless.extractCertificate(pgpSecretKeyRing);
        final List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(pgpPublicKeyRing);
        final Encryptor encryptor = new Encryptor(pgpPublicKeyRingList);
        byte[] data = { 0x01, 0x02, 0x03 };
        byte[] dataEncrypted = encryptor.encrypt(data);
        final Decryptor decryptor = new Decryptor(pgpSecretKeyRing);
        final InputStream inputStream = new ByteArrayInputStream(dataEncrypted);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decryptor.decrypt(inputStream, byteArrayOutputStream);
        byte[] dataDecrypted = byteArrayOutputStream.toByteArray();
        assertArrayEquals(data, dataDecrypted);
    }

    @Test
    public void testDecryptMitPasswortMehrereRecipienten()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        String passwort = "passwort";
        PGPSecretKeyRing secretKeys1 = PGPainless.generateKeyRing().modernKeyRing("person1", passwort);
        PGPSecretKeyRing secretKeys2 = PGPainless.generateKeyRing().modernKeyRing("person2");
        PGPPublicKeyRing pgpPublicKeyRing1 = PGPainless.extractCertificate(secretKeys1);
        PGPPublicKeyRing pgpPublicKeyRing2 = PGPainless.extractCertificate(secretKeys2);
        List<PGPPublicKeyRing> pgpPublicKeyRingList = new ArrayList<>();
        pgpPublicKeyRingList.add(pgpPublicKeyRing1);
        pgpPublicKeyRingList.add(pgpPublicKeyRing2);
        Encryptor encryptor = new Encryptor(pgpPublicKeyRingList, passwort);
        byte[] data = { 0x01, 0x02, 0x03 };
        byte[] dataEncrypted = encryptor.encrypt(data);
        Decryptor decryptor1 = new Decryptor(secretKeys1, passwort);
        Decryptor decryptor2 = new Decryptor(secretKeys2);
        byte[] dataDecrypted1 = decryptor1.decrypt(dataEncrypted);
        byte[] dataDecrypted2 = decryptor2.decrypt(dataEncrypted);
        assertArrayEquals(data, dataDecrypted1);
        assertArrayEquals(data, dataDecrypted2);
    }
}
