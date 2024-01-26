package de.platen.crypt;

public class CryptException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CryptException(final Throwable e) {
        super(e);
    }
}
