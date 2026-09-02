package com.cafeland.club.dto.resultado;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ResultadoSemanalLoteRequest(
        @NotNull Long semanaId,
        @NotEmpty List<@Valid ResultadoSemanalLoteItemRequest> resultados
) {
}
