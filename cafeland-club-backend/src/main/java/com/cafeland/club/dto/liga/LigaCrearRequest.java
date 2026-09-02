package com.cafeland.club.dto.liga;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record LigaCrearRequest(
        @NotNull Integer numero,
        @NotNull @PositiveOrZero Integer minimoPuntosTorneo
) {
}
