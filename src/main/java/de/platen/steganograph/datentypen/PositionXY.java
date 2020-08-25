package de.platen.steganograph.datentypen;

public class PositionXY {

    private static final String PARAMETERFEHLER = "Einer der Parameter ist null.";

    private final X x;
    private final Y y;

    public PositionXY(final X x, final Y y) {
        this.x = x;
        this.y = y;
        if ((x == null) || (y == null)) {
            throw new IllegalArgumentException(PARAMETERFEHLER);
        }
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
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
        PositionXY other = (PositionXY) obj;
        if (x == null) {
            if (other.x != null) {
                return false;
            }
        } else if (!x.equals(other.x)) {
            return false;
        }
        if (y == null) {
            if (other.y != null) {
                return false;
            }
        } else if (!y.equals(other.y)) {
            return false;
        }
        return true;
    }
}
