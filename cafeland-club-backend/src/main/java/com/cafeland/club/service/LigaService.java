package com.cafeland.club.service;

import com.cafeland.club.dto.liga.LigaActualizarRequest;
import com.cafeland.club.dto.liga.LigaCrearRequest;
import com.cafeland.club.dto.liga.LigaResponse;

import java.util.List;
import java.util.Optional;

public interface LigaService {

    List<LigaResponse> listarTodas();

    Optional<LigaResponse> obtenerPorId(Long id);

    LigaResponse registrar(LigaCrearRequest request);

    Optional<LigaResponse> actualizar(Long id, LigaActualizarRequest request);
}
