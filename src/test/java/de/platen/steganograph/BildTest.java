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
        erzeugeTestbild("src/test/resources/BildBlau4ByteABGR.png", 0xFF0000FF, BufferedImage.TYPE_4BYTE_ABGR); // Blau
        erzeugeTestbild("src/test/resources/BildGruen4ByteABGR.png", 0xFF00FF00, BufferedImage.TYPE_4BYTE_ABGR); // Grün
        erzeugeTestbild("src/test/resources/BildRot4ByteABGR.png", 0xFFFF0000, BufferedImage.TYPE_4BYTE_ABGR); // Rot
        erzeugeTestbild("src/test/resources/BildSchwarz4ByteABGR.png", 0xFF000000, BufferedImage.TYPE_4BYTE_ABGR); // Schwarz
        erzeugeTestbild("src/test/resources/BildWeiss4ByteABGR.png", 0xFFFFFFFF, BufferedImage.TYPE_4BYTE_ABGR); // Weiß

        erzeugeTestbild("src/test/resources/BildBlau3ByteBGR.png", 0x0000FF, BufferedImage.TYPE_3BYTE_BGR); // Blau
        erzeugeTestbild("src/test/resources/BildGruen3ByteBGR.png", 0x00FF00, BufferedImage.TYPE_3BYTE_BGR); // Grün
        erzeugeTestbild("src/test/resources/BildRot3ByteBGR.png", 0xFF0000, BufferedImage.TYPE_3BYTE_BGR); // Rot
        erzeugeTestbild("src/test/resources/BildSchwarz3ByteBGR.png", 0x000000, BufferedImage.TYPE_3BYTE_BGR); // Schwarz
        erzeugeTestbild("src/test/resources/BildWeiss3ByteBGR.png", 0xFFFFFF, BufferedImage.TYPE_3BYTE_BGR); // Weiß

        erzeugeTestbild("src/test/resources/BildSchwarzByteGray.png", 0x00, BufferedImage.TYPE_3BYTE_BGR); // Schwarz
        erzeugeTestbild("src/test/resources/BildWeissByteGray.png", 0xFF, BufferedImage.TYPE_3BYTE_BGR); // Weiß
    }

    private static void erzeugeTestbild(String dateiname, int farbe, int typ) throws IOException {
        final BufferedImage bufferedImage = new BufferedImage(400, 400, typ);
        for (int x = 0; x < 400; x++) {
            for (int y = 0; y < 400; y++) {
                bufferedImage.setRGB(x, y, farbe);
            }
        }
        DateiUtils.schreibeBild(dateiname, bufferedImage);
    }
}
