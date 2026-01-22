package de.platen.steganograph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import static java.util.Objects.requireNonNull;

public class AktionInDatei {

    public void schreibeInDatei(final String dateinameQuelle, final String dateinameZiel, final long offset) {
        if (offset < 0) {
            throw new IllegalArgumentException();
        }
        File dateiQuelle = this.checkDateiFile(dateinameQuelle);
        RandomAccessFile dateiZiel = this.checkDateiRandomAccessFile(dateinameZiel);
        long laengeQuelle = dateiQuelle.length();
        long laengeZiel;
        try {
            laengeZiel = dateiZiel.length();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!((offset < laengeZiel) && (offset + laengeQuelle) <= laengeZiel)) {
            throw new IllegalArgumentException();
        }
        try {
            dateiZiel.seek(offset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(dateiQuelle);
            int c;
            while ((c = inputStream.read()) >= 0) {
                dateiZiel.write((byte) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                dateiZiel.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private File checkDateiFile(final String dateiname) {
        requireNonNull(dateiname);
        File file = new File(dateiname);
        if (!file.exists()) {
            throw new IllegalArgumentException();
        }
        if (file.isDirectory()) {
            throw new IllegalArgumentException();
        }
        return file;
    }

    private RandomAccessFile checkDateiRandomAccessFile(final String dateiname) {
        requireNonNull(dateiname);
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(dateiname, "rw");
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        return randomAccessFile;
    }
}
