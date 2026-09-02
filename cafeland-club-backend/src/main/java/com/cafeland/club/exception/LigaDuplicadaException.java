package com.cafeland.club.exception;

public class LigaDuplicadaException extends RuntimeException {

    public LigaDuplicadaException(Integer numero) {
        super("Ya existe una liga con el número " + numero);
    }
}
