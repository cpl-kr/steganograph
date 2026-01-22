package de.platen.steganograph;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AktionZufallsdateiTest {

    @Test
    public void testErzeugeZufallsdatei() throws IOException {
        final AktionZufallsdatei aktionZufallsdatei = new AktionZufallsdatei();
        final String dateiname = "src/test/resources/datei";
        aktionZufallsdatei.erzeugeZufallsdatei(dateiname, 10);
        byte[] bytes = this.leseDatei(dateiname);
        assertEquals(10, bytes.length);
        this.loescheDatei(dateiname);
    }

    private byte[] leseDatei(final String dateiname) throws IOException {
        return FileUtils.readFileToByteArray(new File(dateiname));
    }

    private void loescheDatei(final String dateiname) {
        final File file = new File(dateiname);
        file.delete();
    }
}
