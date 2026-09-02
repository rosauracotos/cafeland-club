package com.cafeland.club.exception;

public class NumeroSemanaDuplicadoException extends RuntimeException {

    public NumeroSemanaDuplicadoException(Integer numeroSemana) {
        super("Ya existe una semana con el número " + numeroSemana);
    }
}
