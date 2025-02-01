package de.platen.steganograph;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.cli.ParseException;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(),
                new AktionHolenAusBild(), new AktionHolenAusAudio(),
                new AktionZufallsdatei(), new AktionInDatei(), new AktionAusDatei());
        Kommandozeile kommandozeile = new Kommandozeile(aktionen);
        final long beginn = new Date().getTime();
        kommandozeile.behandleKommandozeile(args);
        final long ende = new Date().getTime();
        final long zeit = (ende - beginn) / 1000L;
        System.out.println("Zeit: " + zeit + " Sekunden.");
    }
}
