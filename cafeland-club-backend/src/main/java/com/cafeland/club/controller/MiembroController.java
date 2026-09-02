package com.cafeland.club.controller;

import com.cafeland.club.dto.miembro.EstadoMiembroRequest;
import com.cafeland.club.dto.miembro.MiembroActualizarRequest;
import com.cafeland.club.dto.miembro.MiembroCrearRequest;
import com.cafeland.club.dto.miembro.MiembroResponse;
import com.cafeland.club.entity.EstadoMiembro;
import com.cafeland.club.exception.MiembroDuplicadoException;
import com.cafeland.club.service.MiembroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/miembros")
public class MiembroController {

    private final MiembroService miembroService;

    public MiembroController(MiembroService miembroService) {
        this.miembroService = miembroService;
    }

    @GetMapping
    public List<MiembroResponse> listarTodos() {
        return miembroService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MiembroResponse> obtenerPorId(@PathVariable Long id) {
        return miembroService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/activos")
    public List<MiembroResponse> listarActivos() {
        return miembroService.listarPorEstado(EstadoMiembro.ACTIVO);
    }

    @PostMapping
    public ResponseEntity<MiembroResponse> registrar(@Valid @RequestBody MiembroCrearRequest request) {
        MiembroResponse miembroRegistrado = miembroService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(miembroRegistrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MiembroResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MiembroActualizarRequest request
    ) {
        return miembroService.actualizar(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<MiembroResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoMiembroRequest request
    ) {
        return miembroService.cambiarEstado(id, request.estado())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(MiembroDuplicadoException.class)
    public ResponseEntity<Map<String, String>> manejarMiembroDuplicado(MiembroDuplicadoException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", exception.getMessage()));
    }
}
