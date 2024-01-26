package de.platen.crypt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import de.platen.steganograph.utils.DateiUtils;
import org.apache.commons.lang3.Validate;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.jcajce.JcaPGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.pgpainless.PGPainless;

public class KeyVerwaltung {

    public PGPPublicKey lesePublicKey(final String datei) {
        Validate.notBlank(datei, "datei ist null oder leer.");
        PGPPublicKey key = null;
        try {
            InputStream in = new ByteArrayInputStream(Files.readAllBytes(Path.of(datei)));
            in = PGPUtil.getDecoderStream(in);
            final JcaPGPPublicKeyRingCollection pgpPub = new JcaPGPPublicKeyRingCollection(in);
            in.close();
            final Iterator<PGPPublicKeyRing> rIt = pgpPub.getKeyRings();
            while (key == null && rIt.hasNext()) {
                final PGPPublicKeyRing kRing = rIt.next();
                final Iterator<PGPPublicKey> kIt = kRing.getPublicKeys();
                while (key == null && kIt.hasNext()) {
                    final PGPPublicKey k = kIt.next();
                    if (k.isEncryptionKey()) {
                        key = k;
                    }
                }
            }
        } catch (IOException | PGPException e) {
            throw new CryptException(e);
        }
        return key;
    }

    public PGPPublicKeyRing leseZertifikatAusPublicKey(final String datei) {
        Validate.notBlank(datei, "datei ist null oder leer.");
        try {
            InputStream in = new ByteArrayInputStream(Files.readAllBytes(Path.of(datei)));
            in = PGPUtil.getDecoderStream(in);
            final JcaPGPPublicKeyRingCollection pgpPub = new JcaPGPPublicKeyRingCollection(in);
            in.close();
            final Iterator<PGPPublicKeyRing> rIt = pgpPub.getKeyRings();
            while (rIt.hasNext()) {
                final PGPPublicKeyRing kRing = rIt.next();
                final Iterator<PGPPublicKey> kIt = kRing.getPublicKeys();
                while (kIt.hasNext()) {
                    final PGPPublicKey k = kIt.next();
                    if (k.isEncryptionKey()) {
                        return kRing;
                    }
                }
            }
        } catch (IOException | PGPException e) {
            throw new CryptException(e);
        }
        return null;
    }

    public PGPPrivateKey lesePrivateKeyMitPasswort(final String datei, final String passwort) {
        Validate.notBlank(datei, "datei ist null oder leer.");
        Validate.notNull(passwort, "passwort ist null.");
        try {
            InputStream in = new ByteArrayInputStream(Files.readAllBytes(Path.of(datei)));
            in = PGPUtil.getDecoderStream(in);
            PGPSecretKeyRingCollection pgpSecretKeyRingCollection = new PGPSecretKeyRingCollection(in, new JcaKeyFingerprintCalculator());
            Iterator<PGPSecretKeyRing> keyRingIterator = pgpSecretKeyRingCollection.getKeyRings();
            while (keyRingIterator.hasNext()) {
                PGPSecretKeyRing pgpKeyRing = keyRingIterator.next();
                PGPSecretKey pgpSecretKey = pgpKeyRing.getSecretKey();
                PBESecretKeyDecryptor pbeSecretKeyDecryptor = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider()).build(passwort.toCharArray());
                return pgpSecretKey.extractPrivateKey(pbeSecretKeyDecryptor);
            }
        } catch (IOException | PGPException e) {
            throw new CryptException(e);
        }
        return null;
    }

    public PGPSecretKeyRing lesePrivateKey(final String datei) {
        Validate.notBlank(datei, "datei ist null oder leer.");
        try {
            String key = new String(Files.readAllBytes(Path.of(datei)));
            return PGPainless.readKeyRing().secretKeyRing(key);
        } catch (IOException e) {
            throw new CryptException(e);
        }
    }

    public void erzeugeUndSpeichereKeyPaar(String dateiPublicKey, final String dateiPrivateKey, final String id) {
        erzeugeUndSpeichereKeyPaar(dateiPublicKey, dateiPrivateKey, id, null);
    }

    public void erzeugeUndSpeichereKeyPaar(String dateiPublicKey, final String dateiPrivateKey, final String id, final String passwort) {
        try {
            PGPSecretKeyRing pgpSecretKeyRing;
            if (passwort != null) {
                pgpSecretKeyRing = PGPainless.generateKeyRing().modernKeyRing(id, passwort);
            } else {
                pgpSecretKeyRing = PGPainless.generateKeyRing().modernKeyRing(id);
            }
            final PGPPublicKeyRing pgpPublicKeyRing = PGPainless.extractCertificate(pgpSecretKeyRing);
            final String asciiArmoredPublic = PGPainless.asciiArmor(pgpPublicKeyRing);
            final String asciiArmoredPrivate = PGPainless.asciiArmor(pgpSecretKeyRing);
            DateiUtils.schreibeDatei(dateiPublicKey, asciiArmoredPublic.getBytes());
            DateiUtils.schreibeDatei(dateiPrivateKey, asciiArmoredPrivate.getBytes());
        } catch (IOException | PGPException | InvalidAlgorithmParameterException  | NoSuchAlgorithmException e) {
            throw new CryptException(e);
        }
    }
}
