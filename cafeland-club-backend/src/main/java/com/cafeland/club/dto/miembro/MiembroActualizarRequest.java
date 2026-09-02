package com.cafeland.club.dto.miembro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MiembroActualizarRequest(
        @NotBlank String nombre,
        @NotNull Long rangoId,
        @NotNull LocalDate fechaIngreso
) {
}
