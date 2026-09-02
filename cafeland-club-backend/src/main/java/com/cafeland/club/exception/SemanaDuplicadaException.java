package com.cafeland.club.exception;

import java.time.LocalDate;

public class SemanaDuplicadaException extends RuntimeException {

    public SemanaDuplicadaException(LocalDate fechaInicio, LocalDate fechaFin) {
        super("Ya existe una semana para el periodo " + fechaInicio + " a " + fechaFin);
    }
}
