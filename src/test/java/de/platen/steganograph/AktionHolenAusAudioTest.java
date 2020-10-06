package de.platen.steganograph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

import de.platen.extern.wavfile.WavFile;
import de.platen.steganograph.datentypen.AnzahlNutzdaten;
import de.platen.steganograph.uniformat.UniFormatAudio;

public class AktionHolenAusAudioTest {

    @Test
    public void testHoleNutzdatenAusAudioParameterDateinameNull() throws IOException {
        String dateinameVerteilregel = "dateinameVerteilregel";
        String dateinameQuelle = "dateinameQuelle";
        String dateinameNutzdaten = "dateinameNutzdaten";
        try {
            new AktionHolenAusAudio().holeNutzdatenAusAudio(null, dateinameQuelle, dateinameNutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionHolenAusAudio().holeNutzdatenAusAudio(dateinameVerteilregel, null, dateinameNutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionHolenAusAudio().holeNutzdatenAusAudio(dateinameVerteilregel, dateinameQuelle, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testHoleNutzdatenAusAudioParameterDateinameLeer() throws IOException {
        String dateinameVerteilregel = "dateinameVerteilregel";
        String dateinameQuelle = "dateinameQuelle";
        String dateinameNutzdaten = "dateinameNutzdaten";
        try {
            new AktionHolenAusAudio().holeNutzdatenAusAudio("", dateinameQuelle, dateinameNutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            new AktionHolenAusAudio().holeNutzdatenAusAudio(dateinameVerteilregel, "", dateinameNutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            new AktionHolenAusAudio().holeNutzdatenAusAudio(dateinameVerteilregel, dateinameQuelle, "");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
    }

    @Test
    public void testHoleNutzdatenAusAudioParameterNull() throws IOException {
        UniFormatAudio uniFormatAudio = Mockito.mock(UniFormatAudio.class);
        WavFile wavFile = Mockito.mock(WavFile.class);
        AnzahlNutzdaten anzahlNutzdaten = Mockito.mock(AnzahlNutzdaten.class);
        String dateinameNutzdaten = "dateinameNutzdaten";
        try {
            new AktionHolenAusAudio().holeNutzdatenAusAudio(null, wavFile, anzahlNutzdaten, dateinameNutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionHolenAusAudio().holeNutzdatenAusAudio(uniFormatAudio, null, anzahlNutzdaten, dateinameNutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionHolenAusAudio().holeNutzdatenAusAudio(uniFormatAudio, wavFile, null, dateinameNutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
        try {
            new AktionHolenAusAudio().holeNutzdatenAusAudio(uniFormatAudio, wavFile, anzahlNutzdaten, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist null.", e.getMessage());
        }
    }

    @Test
    public void testHoleNutzdatenAusAudioParameterLeer() throws IOException {
        UniFormatAudio uniFormatAudio = Mockito.mock(UniFormatAudio.class);
        WavFile wavFile = Mockito.mock(WavFile.class);
        AnzahlNutzdaten anzahlNutzdaten = Mockito.mock(AnzahlNutzdaten.class);
        String dateinameNutzdaten = "dateinameNutzdaten";
        try {
            new AktionHolenAusAudio().holeNutzdatenAusAudio(uniFormatAudio, wavFile, anzahlNutzdaten, "");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
        try {
            Mockito.when(wavFile.getNumFrames()).thenReturn(0L);
            new AktionHolenAusAudio().holeNutzdatenAusAudio(uniFormatAudio, wavFile, anzahlNutzdaten,
                    dateinameNutzdaten);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Einer der Parameter ist leer.", e.getMessage());
        }
    }
}
