package com.cafeland.club.service.impl;

import com.cafeland.club.entity.Rango;
import com.cafeland.club.repository.RangoRepository;
import com.cafeland.club.service.RangoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RangoServiceImpl implements RangoService {

    private final RangoRepository rangoRepository;

    public RangoServiceImpl(RangoRepository rangoRepository) {
        this.rangoRepository = rangoRepository;
    }

    @Override
    public List<Rango> listarTodos() {
        return rangoRepository.findAll();
    }

    @Override
    public Optional<Rango> obtenerPorId(Long id) {
        return rangoRepository.findById(id);
    }

    @Override
    @Transactional
    public Rango guardar(Rango rango) {
        return rangoRepository.save(rango);
    }
}
