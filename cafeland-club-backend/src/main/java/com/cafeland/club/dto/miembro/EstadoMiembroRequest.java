package com.cafeland.club.dto.miembro;

import com.cafeland.club.entity.EstadoMiembro;
import jakarta.validation.constraints.NotNull;

public record EstadoMiembroRequest(
        @NotNull EstadoMiembro estado
) {
}
