package de.platen.crypt;

import org.apache.commons.lang3.Validate;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.util.io.Streams;
import org.pgpainless.PGPainless;
import org.pgpainless.decryption_verification.ConsumerOptions;
import org.pgpainless.decryption_verification.DecryptionStream;
import org.pgpainless.util.Passphrase;

import java.io.*;

public class Decryptor {

    private final PGPSecretKeyRing pgpSecretKeyRing;
    private final String passwort;

    public Decryptor(final PGPSecretKeyRing pgpSecretKeyRing) {
        Validate.notNull(pgpSecretKeyRing, "pgpSecretKeyRing ist null.");
        this.pgpSecretKeyRing = pgpSecretKeyRing;
        this.passwort = null;
    }

    public Decryptor(final PGPSecretKeyRing pgpSecretKeyRing, final String passwort) {
        Validate.notNull(pgpSecretKeyRing, "pgpSecretKeyRing ist null.");
        Validate.notBlank(passwort, "passwort ist null oder leer.");
        this.pgpSecretKeyRing = pgpSecretKeyRing;
        this.passwort = passwort;
    }

    public byte[] decrypt(final byte[] encryptedData) {
        Validate.notNull(encryptedData, "encryptedData ist null.");
        final InputStream inputStream = new ByteArrayInputStream(encryptedData);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decrypt(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void decrypt(final InputStream inputStream, final OutputStream outputStream) {
        Validate.notNull(inputStream, "inputStream ist null.");
        Validate.notNull(outputStream, "outputStream ist null.");
        try {
            ConsumerOptions options = ConsumerOptions.get().addDecryptionKey(this.pgpSecretKeyRing);
            if (this.passwort != null){
                options = options.addDecryptionPassphrase(new Passphrase(this.passwort.toCharArray()));
            }
            final DecryptionStream consumerStream = PGPainless.decryptAndOrVerify().onInputStream(inputStream).withOptions(options);
            Streams.pipeAll(consumerStream, outputStream);
            consumerStream.close();
        } catch(PGPException | IOException e) {
            throw new CryptException(e);
        }
    }
}
