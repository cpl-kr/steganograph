package de.platen.crypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Date;

import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPBEEncryptedData;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.PBEDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.PublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentVerifierBuilderProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBEDataDecryptorFactoryBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBEKeyEncryptionMethodGenerator;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.util.io.Streams;

public class PGPUtils {
    public static byte[] createSignedObject(final int signingAlg, final PGPPrivateKey signingKey, final byte[] data)
            throws PGPException, IOException {
        final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        final BCPGOutputStream bcOut = new BCPGOutputStream(bOut);
        final PGPSignatureGenerator sGen = new PGPSignatureGenerator(
                new JcaPGPContentSignerBuilder(signingAlg, PGPUtil.SHA384).setProvider("BCFIPS"));
        sGen.init(PGPSignature.BINARY_DOCUMENT, signingKey);
        sGen.generateOnePassVersion(false).encode(bcOut);
        final PGPLiteralDataGenerator lGen = new PGPLiteralDataGenerator();
        final OutputStream lOut = lGen.open(bcOut, PGPLiteralData.BINARY, "_CONSOLE", data.length, new Date());
        for (int i = 0; i != data.length; i++) {
            lOut.write(data[i]);
            sGen.update(data[i]);
        }
        lGen.close();
        sGen.generate().encode(bcOut);
        return bOut.toByteArray();
    }

    public static boolean verifySignedObject(final PGPPublicKey verifyingKey, final byte[] pgpSignedData)
            throws PGPException, IOException {
        final JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpSignedData);
        final PGPOnePassSignatureList onePassList = (PGPOnePassSignatureList) pgpFact.nextObject();
        final PGPOnePassSignature ops = onePassList.get(0);
        final PGPLiteralData literalData = (PGPLiteralData) pgpFact.nextObject();
        final InputStream dIn = literalData.getInputStream();
        ops.init(new JcaPGPContentVerifierBuilderProvider().setProvider("BCFIPS"), verifyingKey);
        int ch;
        while ((ch = dIn.read()) >= 0) {
            ops.update((byte) ch);
        }
        final PGPSignatureList sigList = (PGPSignatureList) pgpFact.nextObject();
        final PGPSignature sig = sigList.get(0);
        return ops.verify(sig);
    }

    public static byte[] createRsaEncryptedObject(final PGPPublicKey encryptionKey, final byte[] data)
            throws PGPException, IOException {
        final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        final PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
        final OutputStream pOut = lData.open(bOut, PGPLiteralData.BINARY, PGPLiteralData.CONSOLE, data.length,
                new Date());
        pOut.write(data);
        pOut.close();
        final byte[] plainText = bOut.toByteArray();
        final ByteArrayOutputStream encOut = new ByteArrayOutputStream();
        final PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
                new JcePGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_256).setWithIntegrityPacket(true)
                        .setSecureRandom(new SecureRandom()).setProvider("BCFIPS"));
        encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encryptionKey).setProvider("BCFIPS"));
        final OutputStream cOut = encGen.open(encOut, plainText.length);
        cOut.write(plainText);
        cOut.close();
        return encOut.toByteArray();
    }

    public static byte[] extractRsaEncryptedObject(final PGPPrivateKey privateKey, final byte[] pgpEncryptedData)
            throws PGPException, IOException {
        final PGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpEncryptedData);
        final PGPEncryptedDataList encList = (PGPEncryptedDataList) pgpFact.nextObject();// note: we can only do this
                                                                                         // because we know we match the
                                                                                         // first encrypted data object
        final PGPPublicKeyEncryptedData encData = (PGPPublicKeyEncryptedData) encList.get(0);
        final PublicKeyDataDecryptorFactory dataDecryptorFactory = new JcePublicKeyDataDecryptorFactoryBuilder()
                .setProvider("BCFIPS").build(privateKey);
        final InputStream clear = encData.getDataStream(dataDecryptorFactory);
        final byte[] literalData = Streams.readAll(clear);
        if (encData.verify()) {
            final PGPObjectFactory litFact = new JcaPGPObjectFactory(literalData);
            final PGPLiteralData litData = (PGPLiteralData) litFact.nextObject();
            final byte[] data = Streams.readAll(litData.getInputStream());
            return data;
        }
        throw new IllegalStateException("modification check failed");
    }

    public static byte[] createPbeEncryptedObject(final char[] passwd, final byte[] data)
            throws PGPException, IOException {
        final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        final PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
        final OutputStream pOut = lData.open(bOut, PGPLiteralData.BINARY, PGPLiteralData.CONSOLE, data.length,
                new Date());
        pOut.write(data);
        pOut.close();
        final byte[] plainText = bOut.toByteArray();
        final ByteArrayOutputStream encOut = new ByteArrayOutputStream();
        final PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
                new JcePGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_256).setWithIntegrityPacket(true)
                        .setSecureRandom(new SecureRandom()).setProvider("BCFIPS"));
        encGen.addMethod(new JcePBEKeyEncryptionMethodGenerator(passwd).setProvider("BCFIPS"));
        final OutputStream cOut = encGen.open(encOut, plainText.length);
        cOut.write(plainText);
        cOut.close();
        return encOut.toByteArray();
    }

    public static byte[] extractPbeEncryptedObject(final char[] passwd, final byte[] pgpEncryptedData)
            throws PGPException, IOException {
        final PGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpEncryptedData);
        final PGPEncryptedDataList encList = (PGPEncryptedDataList) pgpFact.nextObject();
        final PGPPBEEncryptedData encData = (PGPPBEEncryptedData) encList.get(0);
        final PBEDataDecryptorFactory dataDecryptorFactory = new JcePBEDataDecryptorFactoryBuilder(
                new JcaPGPDigestCalculatorProviderBuilder().setProvider("BCFIPS").build()).setProvider("BCFIPS")
                        .build(passwd);
        final InputStream clear = encData.getDataStream(dataDecryptorFactory);
        final byte[] literalData = Streams.readAll(clear);
        if (encData.verify()) {
            final PGPObjectFactory litFact = new JcaPGPObjectFactory(literalData);
            final PGPLiteralData litData = (PGPLiteralData) litFact.nextObject();
            final byte[] data = Streams.readAll(litData.getInputStream());
            return data;
        }
        throw new IllegalStateException("modification check failed");
    }

}
