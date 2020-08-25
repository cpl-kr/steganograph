package de.platen.steganograph.datentypen;

public class Eintrag {

    private static final String FEHLER_PARAMETER_EINTRAG = "Einer der Parameter ist null.";

    private final Positionsnummer positionsnummer;
    private final Kanalnummer kanalnummer;

    public Eintrag(Positionsnummer positionsnummer, Kanalnummer kanalnummer) {
        this.positionsnummer = positionsnummer;
        this.kanalnummer = kanalnummer;
        if ((positionsnummer == null) || (kanalnummer == null)) {
            throw new IllegalArgumentException(FEHLER_PARAMETER_EINTRAG);
        }
    }

    public Positionsnummer getPositionsnummer() {
        return positionsnummer;
    }

    public Kanalnummer getKanalnummer() {
        return kanalnummer;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((kanalnummer == null) ? 0 : kanalnummer.hashCode());
        result = prime * result + ((positionsnummer == null) ? 0 : positionsnummer.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Eintrag other = (Eintrag) obj;
        if (kanalnummer == null) {
            if (other.kanalnummer != null) {
                return false;
            }
        } else if (!kanalnummer.equals(other.kanalnummer)) {
            return false;
        }
        if (positionsnummer == null) {
            if (other.positionsnummer != null) {
                return false;
            }
        } else if (!positionsnummer.equals(other.positionsnummer)) {
            return false;
        }
        return true;
    }
}
