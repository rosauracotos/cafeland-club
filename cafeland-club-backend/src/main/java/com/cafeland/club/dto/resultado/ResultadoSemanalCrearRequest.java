package com.cafeland.club.dto.resultado;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ResultadoSemanalCrearRequest(
        @NotNull Long miembroId,
        @NotNull Long semanaId,
        @NotNull @PositiveOrZero Integer puntosDesafio,
        @NotNull @PositiveOrZero Integer puntosTorneo
) {
}
