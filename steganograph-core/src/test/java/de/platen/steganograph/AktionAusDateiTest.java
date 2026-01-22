package de.platen.steganograph;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AktionAusDateiTest {

    @Test
    public void testLeseAusDatei() throws IOException {
        final AktionAusDatei aktionAusDatei = new AktionAusDatei();
        final String dateinameQuelle = "src/test/resources/quelldatei";
        final String dateinameZiel = "src/test/resources/zieldatei";
        final long offset = 10;
        final byte[] bytes = { 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x01, 0x01, 0x01, 0x01, 0x01, 0x02, 0x02, 0x02, 0x02, 0x02 };
        FileUtils.writeByteArrayToFile(new File(dateinameQuelle), bytes);
        aktionAusDatei.leseAusDatei(dateinameQuelle, dateinameZiel, offset, 5);
        byte[] bytesErwartet = { 0x01, 0x01, 0x01, 0x01, 0x01 };
        byte[] bytesGelesen = FileUtils.readFileToByteArray(new File(dateinameZiel));
        assertArrayEquals(bytesErwartet, bytesGelesen);
        this.loescheDatei(dateinameQuelle);
        this.loescheDatei(dateinameZiel);
    }

    private void loescheDatei(final String dateiname) {
        final File file = new File(dateiname);
        file.delete();
    }
}
