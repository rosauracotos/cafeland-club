package com.cafeland.club.dto.semana;

import java.time.LocalDate;

public record SemanaResponse(
        Long id,
        Integer numeroSemana,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Long ligaId,
        Integer ligaNumero,
        Integer minimoPuntosTorneo
) {
}
