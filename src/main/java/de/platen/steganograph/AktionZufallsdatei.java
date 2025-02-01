package de.platen.steganograph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static java.util.Objects.requireNonNull;

public class AktionZufallsdatei {

    public void erzeugeZufallsdatei(final String dateiname, final long laenge) {
        requireNonNull(dateiname);
        if (laenge < 0) {
            throw new IllegalArgumentException();
        }
        final File datei = new File(dateiname);
        OutputStream outputStream = null;
        SecureRandom secureRandom = null;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            outputStream = new FileOutputStream(datei);
            for (int i = 0; i < laenge; i++) {
                byte wert = (byte) secureRandom.nextInt(255);
                outputStream.write(wert);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
