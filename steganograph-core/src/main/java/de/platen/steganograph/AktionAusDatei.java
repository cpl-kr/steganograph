package de.platen.steganograph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import static java.util.Objects.requireNonNull;

public class AktionAusDatei {

    public void leseAusDatei(final String dateinameQuelle, final String dateinameZiel, final long offset, int laenge) {
        if ((offset < 0) || (laenge < 0)) {
            throw new IllegalArgumentException();
        }
        RandomAccessFile dateiQuelle = this.checkDateiRandomAccessFile(dateinameQuelle);
        File dateiZiel = this.checkDateiFile(dateinameZiel);
        try {
            if (dateiQuelle.length() < (offset + laenge)) {
                throw new IllegalArgumentException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        OutputStream outputStream = null;
        try {
            dateiQuelle.seek(offset);
            outputStream = new FileOutputStream(dateiZiel);
            byte[] bytes = new byte[laenge];
            dateiQuelle.read(bytes);
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dateiQuelle.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private RandomAccessFile checkDateiRandomAccessFile(final String dateiname) {
        requireNonNull(dateiname);
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(dateiname, "r");
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        return randomAccessFile;
    }

    private File checkDateiFile(final String dateiname) {
        requireNonNull(dateiname);
        File file = new File(dateiname);
        if (file.isDirectory()) {
            throw new IllegalArgumentException();
        }
        return file;
    }
}
