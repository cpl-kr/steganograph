package de.platen.steganograph.utils;

import de.platen.crypt.Decryptor;
import de.platen.crypt.KeyVerwaltung;
import org.bouncycastle.openpgp.PGPSecretKeyRing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DateiUtils {

    public static BufferedImage leseBild(String dateiname) throws IOException {
        File file = new File(dateiname);
        return ImageIO.read(file);
    }

    public static void schreibeBild(String dateiname, BufferedImage bufferedImage) throws IOException {
        File file = new File(dateiname);
        bufferedImage.flush();
        ImageIO.write(bufferedImage, "png", file);
    }

    public static void schreibeDatei(final String dateiname, final byte[] daten) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dateiname);
            fos.write(daten);
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
    }

    public static byte[] leseDatei(final String dateiname) throws IOException {
        byte[] daten = new byte[0];
        FileInputStream fis = null;
        ByteArrayOutputStream fos = null;
        try {
            fis = new FileInputStream(dateiname);
            fos = new ByteArrayOutputStream();
            int b = 0;
            boolean ende = false;
            while (!ende) {
                b = fis.read();
                if (b != -1) {
                    fos.write(b);
                } else {
                    ende = true;
                }
            }
            fos.flush();
            daten = fos.toByteArray();
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        return daten;
    }

    public static byte[] leseDatei(String dateiname, String dateiPrivateKey, String passwort ) throws IOException {
        final KeyVerwaltung keyVerwaltung = new KeyVerwaltung();
        final PGPSecretKeyRing pgpSecretKeyRing = keyVerwaltung.lesePrivateKey(dateiPrivateKey);
        final byte[] verteilregelEncrypted = leseDatei(dateiname);
        final Decryptor decryptor;
        if (passwort != null) {
            decryptor = new Decryptor(pgpSecretKeyRing, passwort);
        } else {
            decryptor = new Decryptor(pgpSecretKeyRing);
        }
        return decryptor.decrypt(verteilregelEncrypted);
    }
}
