package com.cafeland.club.service.impl;

import com.cafeland.club.dto.resultado.ResultadoSemanalActualizarRequest;
import com.cafeland.club.dto.resultado.ResultadoSemanalCrearRequest;
import com.cafeland.club.dto.resultado.ResultadoSemanalLoteItemRequest;
import com.cafeland.club.dto.resultado.ResultadoSemanalLoteRequest;
import com.cafeland.club.dto.resultado.ResultadoSemanalResponse;
import com.cafeland.club.entity.Miembro;
import com.cafeland.club.entity.ResultadoSemanal;
import com.cafeland.club.entity.Semana;
import com.cafeland.club.exception.ResultadoSemanalDuplicadoException;
import com.cafeland.club.repository.MiembroRepository;
import com.cafeland.club.repository.ResultadoSemanalRepository;
import com.cafeland.club.repository.SemanaRepository;
import com.cafeland.club.service.ResultadoSemanalService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ResultadoSemanalServiceImpl implements ResultadoSemanalService {

    private static final int MINIMO_DESAFIO = 2000;

    private final ResultadoSemanalRepository resultadoRepository;
    private final MiembroRepository miembroRepository;
    private final SemanaRepository semanaRepository;

    public ResultadoSemanalServiceImpl(
            ResultadoSemanalRepository resultadoRepository,
            MiembroRepository miembroRepository,
            SemanaRepository semanaRepository
    ) {
        this.resultadoRepository = resultadoRepository;
        this.miembroRepository = miembroRepository;
        this.semanaRepository = semanaRepository;
    }

    @Override
    public List<ResultadoSemanalResponse> listarTodos() {
        return resultadoRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public Optional<ResultadoSemanalResponse> obtenerPorId(Long id) {
        return resultadoRepository.findById(id)
                .map(this::convertirAResponse);
    }

    @Override
    public List<ResultadoSemanalResponse> listarPorSemana(Long semanaId) {
        return resultadoRepository.findBySemana_IdOrderByMiembro_NombreAsc(semanaId).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public List<ResultadoSemanalResponse> listarPorMiembro(Long miembroId) {
        return resultadoRepository.findByMiembro_IdOrderBySemana_NumeroSemanaAsc(miembroId).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    @Transactional
    public ResultadoSemanalResponse registrar(ResultadoSemanalCrearRequest request) {
        Miembro miembro = obtenerMiembro(request.miembroId());
        Semana semana = obtenerSemana(request.semanaId());
        validarResultadoDisponible(request.miembroId(), request.semanaId(), null);

        ResultadoSemanal resultado = new ResultadoSemanal(miembro, semana);
        resultado.setPuntosDesafio(request.puntosDesafio());
        resultado.setPuntosTorneo(request.puntosTorneo());

        try {
            return convertirAResponse(resultadoRepository.saveAndFlush(resultado));
        } catch (DataIntegrityViolationException exception) {
            throw new ResultadoSemanalDuplicadoException(request.miembroId(), request.semanaId());
        }
    }

    @Override
    @Transactional
    public List<ResultadoSemanalResponse> guardarLote(ResultadoSemanalLoteRequest request) {
        Semana semana = obtenerSemana(request.semanaId());
        Set<Long> miembrosProcesados = new HashSet<>();

        List<ResultadoSemanal> resultados = request.resultados().stream()
                .map(item -> prepararResultadoLote(item, semana, miembrosProcesados))
                .toList();

        return resultadoRepository.saveAllAndFlush(resultados).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    @Transactional
    public Optional<ResultadoSemanalResponse> actualizar(
            Long id,
            ResultadoSemanalActualizarRequest request
    ) {
        return resultadoRepository.findById(id)
                .map(resultado -> {
                    Miembro miembro = obtenerMiembro(request.miembroId());
                    Semana semana = obtenerSemana(request.semanaId());
                    validarResultadoDisponible(request.miembroId(), request.semanaId(), id);

                    resultado.setMiembro(miembro);
                    resultado.setSemana(semana);
                    resultado.setPuntosDesafio(request.puntosDesafio());
                    resultado.setPuntosTorneo(request.puntosTorneo());

                    try {
                        return convertirAResponse(resultadoRepository.saveAndFlush(resultado));
                    } catch (DataIntegrityViolationException exception) {
                        throw new ResultadoSemanalDuplicadoException(request.miembroId(), request.semanaId());
                    }
                });
    }

    private Miembro obtenerMiembro(Long miembroId) {
        return miembroRepository.findById(miembroId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe un miembro con id " + miembroId
                ));
    }

    private Semana obtenerSemana(Long semanaId) {
        return semanaRepository.findById(semanaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe una semana con id " + semanaId
                ));
    }

    private ResultadoSemanal prepararResultadoLote(
            ResultadoSemanalLoteItemRequest item,
            Semana semana,
            Set<Long> miembrosProcesados
    ) {
        if (!miembrosProcesados.add(item.miembroId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El miembro con id " + item.miembroId() + " está repetido en el lote"
            );
        }

        Miembro miembro = obtenerMiembro(item.miembroId());
        ResultadoSemanal resultado = resultadoRepository
                .findByMiembro_IdAndSemana_Id(item.miembroId(), semana.getId())
                .orElseGet(() -> new ResultadoSemanal(miembro, semana));

        resultado.setPuntosDesafio(item.puntosDesafio());
        resultado.setPuntosTorneo(item.puntosTorneo());
        return resultado;
    }

    private void validarResultadoDisponible(Long miembroId, Long semanaId, Long resultadoId) {
        boolean resultadoExistente = resultadoId == null
                ? resultadoRepository.existsByMiembro_IdAndSemana_Id(miembroId, semanaId)
                : resultadoRepository.existsByMiembro_IdAndSemana_IdAndIdNot(
                        miembroId,
                        semanaId,
                        resultadoId
                );

        if (resultadoExistente) {
            throw new ResultadoSemanalDuplicadoException(miembroId, semanaId);
        }
    }

    private ResultadoSemanalResponse convertirAResponse(ResultadoSemanal resultado) {
        int minimoTorneo = resultado.getSemana().getLiga().getMinimoPuntosTorneo();

        return new ResultadoSemanalResponse(
                resultado.getId(),
                resultado.getMiembro().getId(),
                resultado.getMiembro().getNombre(),
                resultado.getSemana().getId(),
                resultado.getSemana().getNumeroSemana(),
                resultado.getPuntosDesafio(),
                MINIMO_DESAFIO,
                resultado.getPuntosDesafio() >= MINIMO_DESAFIO,
                resultado.getPuntosTorneo(),
                minimoTorneo,
                resultado.getPuntosTorneo() >= minimoTorneo
        );
    }
}
