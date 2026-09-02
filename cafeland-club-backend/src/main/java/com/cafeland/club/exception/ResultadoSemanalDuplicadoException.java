package com.cafeland.club.exception;

public class ResultadoSemanalDuplicadoException extends RuntimeException {

    public ResultadoSemanalDuplicadoException(Long miembroId, Long semanaId) {
        super("Ya existe un resultado para el miembro " + miembroId + " en la semana " + semanaId);
    }
}
