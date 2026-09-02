package com.cafeland.club.service;

import com.cafeland.club.dto.semana.SemanaActualizarRequest;
import com.cafeland.club.dto.semana.SemanaCrearRequest;
import com.cafeland.club.dto.semana.SemanaResponse;

import java.util.List;
import java.util.Optional;

public interface SemanaService {

    List<SemanaResponse> listarTodas();

    Optional<SemanaResponse> obtenerPorId(Long id);

    SemanaResponse registrar(SemanaCrearRequest request);

    Optional<SemanaResponse> actualizar(Long id, SemanaActualizarRequest request);
}
