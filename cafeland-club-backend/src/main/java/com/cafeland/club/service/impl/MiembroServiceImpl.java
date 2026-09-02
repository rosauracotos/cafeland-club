package com.cafeland.club.service.impl;

import com.cafeland.club.dto.miembro.MiembroActualizarRequest;
import com.cafeland.club.dto.miembro.MiembroCrearRequest;
import com.cafeland.club.dto.miembro.MiembroResponse;
import com.cafeland.club.entity.EstadoMiembro;
import com.cafeland.club.entity.Miembro;
import com.cafeland.club.entity.Rango;
import com.cafeland.club.exception.MiembroDuplicadoException;
import com.cafeland.club.repository.MiembroRepository;
import com.cafeland.club.repository.RangoRepository;
import com.cafeland.club.service.MiembroService;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MiembroServiceImpl implements MiembroService {

    private final MiembroRepository miembroRepository;
    private final RangoRepository rangoRepository;

    public MiembroServiceImpl(MiembroRepository miembroRepository, RangoRepository rangoRepository) {
        this.miembroRepository = miembroRepository;
        this.rangoRepository = rangoRepository;
    }

    @Override
    public List<MiembroResponse> listarTodos() {
        return miembroRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public Optional<MiembroResponse> obtenerPorId(Long id) {
        return miembroRepository.findById(id)
                .map(this::convertirAResponse);
    }

    @Override
    public List<MiembroResponse> listarPorEstado(EstadoMiembro estado) {
        return miembroRepository.findByEstado(estado).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    @Transactional
    public MiembroResponse registrar(MiembroCrearRequest request) {
        if (miembroRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new MiembroDuplicadoException(request.nombre());
        }

        Rango rango = rangoRepository.findById(request.rangoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe un rango con id " + request.rangoId()
                ));

        Miembro miembro = new Miembro(
                request.nombre(),
                rango,
                request.fechaIngreso(),
                request.estado()
        );

        try {
            return convertirAResponse(miembroRepository.saveAndFlush(miembro));
        } catch (DataIntegrityViolationException exception) {
            throw new MiembroDuplicadoException(request.nombre());
        }
    }

    @Override
    @Transactional
    public Optional<MiembroResponse> actualizar(Long id, MiembroActualizarRequest request) {
        return miembroRepository.findById(id)
                .map(miembro -> {
                    Rango rango = rangoRepository.findById(request.rangoId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "No existe un rango con id " + request.rangoId()
                            ));

                    miembro.setNombre(request.nombre());
                    miembro.setRango(rango);
                    miembro.setFechaIngreso(request.fechaIngreso());

                    return convertirAResponse(miembro);
                });
    }

    @Override
    @Transactional
    public Optional<MiembroResponse> cambiarEstado(Long id, EstadoMiembro estado) {
        return miembroRepository.findById(id)
                .map(miembro -> {
                    miembro.setEstado(estado);
                    return convertirAResponse(miembro);
                });
    }

    private MiembroResponse convertirAResponse(Miembro miembro) {
        return new MiembroResponse(
                miembro.getId(),
                miembro.getNombre(),
                miembro.getRango().getId(),
                miembro.getRango().getNombre(),
                miembro.getFechaIngreso(),
                miembro.getEstado()
        );
    }
}
