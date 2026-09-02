package com.cafeland.club.dto.miembro;

import com.cafeland.club.entity.EstadoMiembro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MiembroCrearRequest(
        @NotBlank String nombre,
        @NotNull Long rangoId,
        @NotNull LocalDate fechaIngreso,
        @NotNull EstadoMiembro estado
) {
}
