package de.platen.crypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.util.io.Streams;
import org.pgpainless.PGPainless;
import org.pgpainless.encryption_signing.EncryptionOptions;
import org.pgpainless.encryption_signing.EncryptionStream;
import org.pgpainless.encryption_signing.ProducerOptions;
import org.pgpainless.util.Passphrase;

public class Encryptor {

    final List<PGPPublicKeyRing> pgpPublicKeyRing;
    final String passwort;

    public Encryptor(final List<PGPPublicKeyRing> pgpPublicKeyRing) {
        Validate.notNull(pgpPublicKeyRing, "pgpPublicKeyRing ist null.");
        Validate.notEmpty(pgpPublicKeyRing, "pgpPublicKeyRing ist leer.");
        this.pgpPublicKeyRing = new ArrayList<>(pgpPublicKeyRing);
        this.passwort = null;
    }

    public Encryptor(final List<PGPPublicKeyRing> pgpPublicKeyRing, final String passwort) {
        Validate.notNull(pgpPublicKeyRing, "pgpPublicKeyRing ist null.");
        Validate.notEmpty(pgpPublicKeyRing, "pgpPublicKeyRing ist leer.");
        Validate.notBlank(passwort, "passwort ist null oder leer.");
        this.pgpPublicKeyRing = new ArrayList<>(pgpPublicKeyRing);
        this.passwort = passwort;
    }

    public byte[] encrypt(final byte[] data) {
        Validate.notNull(data, "data ist null.");
        final InputStream inputStream = new ByteArrayInputStream(data);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        encrypt(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void encrypt(final InputStream inputStream, final OutputStream outputStream) {
        Validate.notNull(inputStream, "inputStream ist null.");
        Validate.notNull(outputStream, "outputStream ist null.");
        try {
            EncryptionOptions encryptionOptions = EncryptionOptions.get();
            for (PGPPublicKeyRing pgpPublicKeyRingRecipient : pgpPublicKeyRing) {
                encryptionOptions = encryptionOptions.addRecipient(pgpPublicKeyRingRecipient);
            }
            if (this.passwort != null) {
                encryptionOptions.addPassphrase(new Passphrase(this.passwort.toCharArray()));
            }
            final ProducerOptions options = ProducerOptions.encrypt(encryptionOptions);
            final EncryptionStream encryptionStream = PGPainless.encryptAndOrSign().onOutputStream(outputStream).withOptions(options);
            Streams.pipeAll(inputStream, encryptionStream);
            encryptionStream.close();
        } catch (PGPException | IOException e) {
            throw new CryptException(e);
        }
    }
}
