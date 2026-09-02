package com.cafeland.club.service.impl;

import com.cafeland.club.dto.liga.LigaActualizarRequest;
import com.cafeland.club.dto.liga.LigaCrearRequest;
import com.cafeland.club.dto.liga.LigaResponse;
import com.cafeland.club.entity.Liga;
import com.cafeland.club.exception.LigaDuplicadaException;
import com.cafeland.club.repository.LigaRepository;
import com.cafeland.club.service.LigaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LigaServiceImpl implements LigaService {

    private final LigaRepository ligaRepository;

    public LigaServiceImpl(LigaRepository ligaRepository) {
        this.ligaRepository = ligaRepository;
    }

    @Override
    public List<LigaResponse> listarTodas() {
        return ligaRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public Optional<LigaResponse> obtenerPorId(Long id) {
        return ligaRepository.findById(id)
                .map(this::convertirAResponse);
    }

    @Override
    @Transactional
    public LigaResponse registrar(LigaCrearRequest request) {
        validarNumeroDisponible(request.numero(), null);

        Liga liga = new Liga(request.numero(), request.minimoPuntosTorneo());

        try {
            return convertirAResponse(ligaRepository.saveAndFlush(liga));
        } catch (DataIntegrityViolationException exception) {
            throw new LigaDuplicadaException(request.numero());
        }
    }

    @Override
    @Transactional
    public Optional<LigaResponse> actualizar(Long id, LigaActualizarRequest request) {
        return ligaRepository.findById(id)
                .map(liga -> {
                    validarNumeroDisponible(request.numero(), id);

                    liga.setNumero(request.numero());
                    liga.setMinimoPuntosTorneo(request.minimoPuntosTorneo());

                    try {
                        return convertirAResponse(ligaRepository.saveAndFlush(liga));
                    } catch (DataIntegrityViolationException exception) {
                        throw new LigaDuplicadaException(request.numero());
                    }
                });
    }

    private void validarNumeroDisponible(Integer numero, Long ligaId) {
        boolean numeroOcupado = ligaId == null
                ? ligaRepository.existsByNumero(numero)
                : ligaRepository.existsByNumeroAndIdNot(numero, ligaId);

        if (numeroOcupado) {
            throw new LigaDuplicadaException(numero);
        }
    }

    private LigaResponse convertirAResponse(Liga liga) {
        return new LigaResponse(
                liga.getId(),
                liga.getNumero(),
                liga.getMinimoPuntosTorneo()
        );
    }
}
