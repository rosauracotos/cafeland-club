package com.cafeland.club.service;

import com.cafeland.club.dto.miembro.MiembroActualizarRequest;
import com.cafeland.club.dto.miembro.MiembroCrearRequest;
import com.cafeland.club.dto.miembro.MiembroResponse;
import com.cafeland.club.entity.EstadoMiembro;

import java.util.List;
import java.util.Optional;

public interface MiembroService {

    List<MiembroResponse> listarTodos();

    Optional<MiembroResponse> obtenerPorId(Long id);

    List<MiembroResponse> listarPorEstado(EstadoMiembro estado);

    MiembroResponse registrar(MiembroCrearRequest request);

    Optional<MiembroResponse> actualizar(Long id, MiembroActualizarRequest request);

    Optional<MiembroResponse> cambiarEstado(Long id, EstadoMiembro estado);
}
