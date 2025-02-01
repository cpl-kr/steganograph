package de.platen.steganograph;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AktionInDateiTest {

    @Test
    public void testSchreibeInDatei() throws IOException {
        final AktionInDatei aktionInDatei = new AktionInDatei();
        final String dateinameQuelle = "src/test/resources/quelldatei";
        final String dateinameZiel = "src/test/resources/zieldatei";
        final long offset = 10;
        this.erzeugeDatei(dateinameQuelle, 5, (byte) 0x01);
        this.erzeugeDatei(dateinameZiel, 20, (byte) 0x02);
        aktionInDatei.schreibeInDatei(dateinameQuelle, dateinameZiel, offset);
        final byte[] bytes = { 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x01, 0x01, 0x01, 0x01, 0x01, 0x02, 0x02, 0x02, 0x02, 0x02 };
        final byte[] bytesGelesen = this.leseDatei(dateinameZiel);
        assertArrayEquals(bytes, bytesGelesen);
        this.loescheDatei(dateinameQuelle);
        this.loescheDatei(dateinameZiel);
    }

    private void erzeugeDatei(final String dateiname, final int laenge, byte inhalt) throws IOException {
        final byte[] bytes = new byte[laenge];
        Arrays.fill(bytes, inhalt);
        FileUtils.writeByteArrayToFile(new File(dateiname), bytes);
    }

    private byte[] leseDatei(final String dateiname) throws IOException {
        return FileUtils.readFileToByteArray(new File(dateiname));
    }

    private void loescheDatei(final String dateiname) {
        final File file = new File(dateiname);
        file.delete();
    }
}
