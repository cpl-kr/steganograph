package de.platen.steganograph;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import de.platen.extern.wavfile.WavFile;
import de.platen.extern.wavfile.WavFileException;

public class AudioTest {

    @Test
    @Ignore
    public void testAudio() throws IOException, WavFileException {
        WavFile wavFileOriginal = WavFile.openWavFile(new File("src/test/resources/audiooriginal.wav"));
        int numChannels = wavFileOriginal.getNumChannels();
        long sampleRate = wavFileOriginal.getSampleRate();
        int validBits = wavFileOriginal.getValidBits();
        int numFramesNeu = 5000;
        WavFile wavFileNeu = WavFile.newWavFile(new File("src/test/resources/audioneu.wav"), numChannels, numFramesNeu,
                validBits, sampleRate);
        int[][] sampleBuffer = new int[numChannels][numFramesNeu];
        wavFileOriginal.readFrames(sampleBuffer, numFramesNeu);
        wavFileNeu.writeFrames(sampleBuffer, numFramesNeu);
        wavFileOriginal.close();
        wavFileNeu.close();
    }

    @Test
    @Ignore
    public void testAudioNeu() throws IOException, WavFileException {
        int sekunden = 10;
        int numChannels = 2;
        int validBits = 16;
        int sampleRate = 44100;
        int numFrames = sampleRate * sekunden;
        String dateiname = "src/test/resources/audioneu.wav";
        WavFile wavFile = WavFile.newWavFile(new File(dateiname), numChannels, numFrames, validBits, sampleRate);
        int[][] sampleBuffer = new int[numChannels][sampleRate];
        for (int sekunde = 1; sekunde <= 10; sekunde++) {
            for (int sample = 0; sample < sampleRate; sample++) {
                for (int kanal = 0; kanal < numChannels; kanal++) {
                    sampleBuffer[kanal][sample] = sample;
                }

            }
            wavFile.writeFrames(sampleBuffer, sampleRate);
        }
        wavFile.close();
    }
}
