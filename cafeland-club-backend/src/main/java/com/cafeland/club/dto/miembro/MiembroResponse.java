package com.cafeland.club.dto.miembro;

import com.cafeland.club.entity.EstadoMiembro;

import java.time.LocalDate;

public record MiembroResponse(
        Long id,
        String nombre,
        Long rangoId,
        String rangoNombre,
        LocalDate fechaIngreso,
        EstadoMiembro estado
) {
}
