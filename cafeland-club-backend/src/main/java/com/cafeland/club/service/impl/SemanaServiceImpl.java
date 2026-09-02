package com.cafeland.club.service.impl;

import com.cafeland.club.dto.semana.SemanaActualizarRequest;
import com.cafeland.club.dto.semana.SemanaCrearRequest;
import com.cafeland.club.dto.semana.SemanaResponse;
import com.cafeland.club.entity.Liga;
import com.cafeland.club.entity.Semana;
import com.cafeland.club.exception.NumeroSemanaDuplicadoException;
import com.cafeland.club.exception.SemanaDuplicadaException;
import com.cafeland.club.repository.LigaRepository;
import com.cafeland.club.repository.SemanaRepository;
import com.cafeland.club.service.SemanaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SemanaServiceImpl implements SemanaService {

    private final SemanaRepository semanaRepository;
    private final LigaRepository ligaRepository;

    public SemanaServiceImpl(SemanaRepository semanaRepository, LigaRepository ligaRepository) {
        this.semanaRepository = semanaRepository;
        this.ligaRepository = ligaRepository;
    }

    @Override
    public List<SemanaResponse> listarTodas() {
        return semanaRepository.findAllByOrderByNumeroSemanaAsc().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public Optional<SemanaResponse> obtenerPorId(Long id) {
        return semanaRepository.findById(id)
                .map(this::convertirAResponse);
    }

    @Override
    @Transactional
    public SemanaResponse registrar(SemanaCrearRequest request) {
        Liga liga = obtenerLiga(request.ligaId());
        validarNumeroDisponible(request.numeroSemana(), null);
        validarPeriodoDisponible(request.fechaInicio(), request.fechaFin(), null);

        Semana semana = new Semana(request.numeroSemana(), request.fechaInicio(), request.fechaFin(), liga);

        try {
            return convertirAResponse(semanaRepository.saveAndFlush(semana));
        } catch (DataIntegrityViolationException exception) {
            throw convertirConflicto(exception, request.numeroSemana(), request.fechaInicio(), request.fechaFin());
        }
    }

    @Override
    @Transactional
    public Optional<SemanaResponse> actualizar(Long id, SemanaActualizarRequest request) {
        return semanaRepository.findById(id)
                .map(semana -> {
                    Liga liga = obtenerLiga(request.ligaId());
                    validarNumeroDisponible(request.numeroSemana(), id);
                    validarPeriodoDisponible(request.fechaInicio(), request.fechaFin(), id);

                    semana.setNumeroSemana(request.numeroSemana());
                    semana.setFechaInicio(request.fechaInicio());
                    semana.setFechaFin(request.fechaFin());
                    semana.setLiga(liga);

                    try {
                        return convertirAResponse(semanaRepository.saveAndFlush(semana));
                    } catch (DataIntegrityViolationException exception) {
                        throw convertirConflicto(
                                exception,
                                request.numeroSemana(),
                                request.fechaInicio(),
                                request.fechaFin()
                        );
                    }
                });
    }

    private Liga obtenerLiga(Long ligaId) {
        return ligaRepository.findById(ligaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe una liga con id " + ligaId
                ));
    }

    private void validarNumeroDisponible(Integer numeroSemana, Long semanaId) {
        boolean numeroOcupado = semanaId == null
                ? semanaRepository.existsByNumeroSemana(numeroSemana)
                : semanaRepository.existsByNumeroSemanaAndIdNot(numeroSemana, semanaId);

        if (numeroOcupado) {
            throw new NumeroSemanaDuplicadoException(numeroSemana);
        }
    }

    private void validarPeriodoDisponible(LocalDate fechaInicio, LocalDate fechaFin, Long semanaId) {
        boolean periodoOcupado = semanaId == null
                ? semanaRepository.existsByFechaInicioAndFechaFin(fechaInicio, fechaFin)
                : semanaRepository.existsByFechaInicioAndFechaFinAndIdNot(fechaInicio, fechaFin, semanaId);

        if (periodoOcupado) {
            throw new SemanaDuplicadaException(fechaInicio, fechaFin);
        }
    }

    private RuntimeException convertirConflicto(
            DataIntegrityViolationException exception,
            Integer numeroSemana,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        String detalle = exception.getMostSpecificCause().getMessage();
        if (detalle != null && detalle.contains("uk_semanas_numero_semana")) {
            return new NumeroSemanaDuplicadoException(numeroSemana);
        }
        return new SemanaDuplicadaException(fechaInicio, fechaFin);
    }

    private SemanaResponse convertirAResponse(Semana semana) {
        return new SemanaResponse(
                semana.getId(),
                semana.getNumeroSemana(),
                semana.getFechaInicio(),
                semana.getFechaFin(),
                semana.getLiga().getId(),
                semana.getLiga().getNumero(),
                semana.getLiga().getMinimoPuntosTorneo()
        );
    }
}
