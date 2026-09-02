package com.cafeland.club.exception;

public class MiembroDuplicadoException extends RuntimeException {

    public MiembroDuplicadoException(String nombre) {
        super("Ya existe un miembro con el nombre " + nombre);
    }
}
