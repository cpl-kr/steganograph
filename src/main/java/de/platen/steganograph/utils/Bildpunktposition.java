package de.platen.steganograph.utils;

import de.platen.steganograph.datentypen.Breite;
import de.platen.steganograph.datentypen.Hoehe;
import de.platen.steganograph.datentypen.PositionXY;
import de.platen.steganograph.datentypen.X;
import de.platen.steganograph.datentypen.Y;

public class Bildpunktposition {

    private static final String PARAMETERFEHLER = "Einer der Parameter ist null.";

    private final Breite breite;
    private final Hoehe hoehe;

    public Bildpunktposition(Breite breite, Hoehe hoehe) {
        this.breite = breite;
        this.hoehe = hoehe;
        if ((breite == null) || (hoehe == null)) {
            throw new IllegalArgumentException(PARAMETERFEHLER);
        }
    }

    public PositionXY ermittleNaechstenBildpunkt(PositionXY positionXY) {
        if (positionXY.getX().get() >= breite.get()) {
            return null;
        }
        if (positionXY.getY().get() >= hoehe.get()) {
            return null;
        }
        if (positionXY.getX().get() < breite.get() - 1) {
            X x = new X(positionXY.getX().get() + 1);
            return new PositionXY(x, positionXY.getY());
        } else {
            if (positionXY.getY().get() < hoehe.get() - 1) {
                X x = new X(0);
                Y y = new Y(positionXY.getY().get() + 1);
                return new PositionXY(x, y);
            }
        }
        return null;
    }

    public PositionXY ermittleNaechstenBildpunkt(PositionXY positionXY, int abstand) {
        if (abstand < 0) {
            return null;
        }
        if (abstand == 0) {
            return positionXY;
        }
        PositionXY neuePosition = positionXY;
        for (int i = 0; i < abstand; i++) {
            neuePosition = ermittleNaechstenBildpunkt(neuePosition);
            if (neuePosition == null) {
                return null;
            }
        }
        return neuePosition;
    }
}
