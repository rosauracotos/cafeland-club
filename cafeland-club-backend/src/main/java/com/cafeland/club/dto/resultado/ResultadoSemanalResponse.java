package com.cafeland.club.dto.resultado;

public record ResultadoSemanalResponse(
        Long id,
        Long miembroId,
        String nombreMiembro,
        Long semanaId,
        Integer numeroSemana,
        Integer puntosDesafio,
        Integer minimoDesafio,
        boolean cumpleDesafio,
        Integer puntosTorneo,
        Integer minimoTorneo,
        boolean cumpleTorneo
) {
}
