package com.cafeland.club.service;

import com.cafeland.club.entity.Rango;

import java.util.List;
import java.util.Optional;

public interface RangoService {

    List<Rango> listarTodos();

    Optional<Rango> obtenerPorId(Long id);

    Rango guardar(Rango rango);
}
