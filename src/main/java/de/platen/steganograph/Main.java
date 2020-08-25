package de.platen.steganograph;

import java.io.IOException;

import org.apache.commons.cli.ParseException;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        Aktionen aktionen = new Aktionen();
        Kommandozeile kommandozeile = new Kommandozeile(aktionen);
        kommandozeile.behandleKommandozeile(args);
    }
}
