package de.platen.steganograph;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import de.platen.steganograph.utils.DateiUtils;

public class BildTest {

    @Test
    @Ignore
    public void testBild() throws IOException {
        erzeugeTestbild("src/test/resources/testbild1.png", 0xFF0000FF); // Blau
        erzeugeTestbild("src/test/resources/testbild2.png", 0xFF00FF00); // Grün
        erzeugeTestbild("src/test/resources/testbild3.png", 0xFFFF0000); // Rot
        erzeugeTestbild("src/test/resources/testbild4.png", 0xFF000000); // Schwarz
        erzeugeTestbild("src/test/resources/testbild5.png", 0xFFFFFFFF); // Weiß
    }

    private static void erzeugeTestbild(String dateiname, int farbe) throws IOException {
        final BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_4BYTE_ABGR);
        for (int x = 0; x < 400; x++) {
            for (int y = 0; y < 400; y++) {
                bufferedImage.setRGB(x, y, farbe);
            }
        }
        DateiUtils.schreibeBild(dateiname, bufferedImage);
    }
}
