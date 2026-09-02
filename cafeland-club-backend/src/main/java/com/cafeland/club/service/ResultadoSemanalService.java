package com.cafeland.club.service;

import com.cafeland.club.dto.resultado.ResultadoSemanalActualizarRequest;
import com.cafeland.club.dto.resultado.ResultadoSemanalCrearRequest;
import com.cafeland.club.dto.resultado.ResultadoSemanalLoteRequest;
import com.cafeland.club.dto.resultado.ResultadoSemanalResponse;

import java.util.List;
import java.util.Optional;

public interface ResultadoSemanalService {

    List<ResultadoSemanalResponse> listarTodos();

    Optional<ResultadoSemanalResponse> obtenerPorId(Long id);

    List<ResultadoSemanalResponse> listarPorSemana(Long semanaId);

    List<ResultadoSemanalResponse> listarPorMiembro(Long miembroId);

    ResultadoSemanalResponse registrar(ResultadoSemanalCrearRequest request);

    List<ResultadoSemanalResponse> guardarLote(ResultadoSemanalLoteRequest request);

    Optional<ResultadoSemanalResponse> actualizar(Long id, ResultadoSemanalActualizarRequest request);
}
