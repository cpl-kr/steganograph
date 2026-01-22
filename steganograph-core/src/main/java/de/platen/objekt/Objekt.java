package de.platen.objekt;

public abstract class Objekt<T> {

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((wert == null) ? 0 : wert.hashCode());
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
        Objekt<?> other = (Objekt<?>) obj;
        if (wert == null) {
            if (other.wert != null) {
                return false;
            }
        } else if (!wert.equals(other.wert)) {
            return false;
        }
        return true;
    }

    private final T wert;

    public Objekt(T wert) {
        this.wert = wert;
    }

    public T get() {
        return wert;
    }
}
