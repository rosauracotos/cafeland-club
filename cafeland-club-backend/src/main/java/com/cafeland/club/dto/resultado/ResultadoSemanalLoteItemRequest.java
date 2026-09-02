package com.cafeland.club.dto.resultado;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ResultadoSemanalLoteItemRequest(
        @NotNull Long miembroId,
        @NotNull @Min(0) Integer puntosDesafio,
        @NotNull @Min(0) Integer puntosTorneo
) {
}
