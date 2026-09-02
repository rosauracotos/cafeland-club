package com.cafeland.club.controller;

import com.cafeland.club.dto.semana.SemanaActualizarRequest;
import com.cafeland.club.dto.semana.SemanaCrearRequest;
import com.cafeland.club.dto.semana.SemanaResponse;
import com.cafeland.club.exception.NumeroSemanaDuplicadoException;
import com.cafeland.club.exception.SemanaDuplicadaException;
import com.cafeland.club.service.SemanaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/semanas")
public class SemanaController {

    private final SemanaService semanaService;

    public SemanaController(SemanaService semanaService) {
        this.semanaService = semanaService;
    }

    @GetMapping
    public List<SemanaResponse> listarTodas() {
        return semanaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SemanaResponse> obtenerPorId(@PathVariable Long id) {
        return semanaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SemanaResponse> registrar(@Valid @RequestBody SemanaCrearRequest request) {
        SemanaResponse semanaRegistrada = semanaService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(semanaRegistrada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SemanaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SemanaActualizarRequest request
    ) {
        return semanaService.actualizar(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(SemanaDuplicadaException.class)
    public ResponseEntity<Map<String, String>> manejarSemanaDuplicada(SemanaDuplicadaException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(NumeroSemanaDuplicadoException.class)
    public ResponseEntity<Map<String, String>> manejarNumeroSemanaDuplicado(
            NumeroSemanaDuplicadoException exception
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", exception.getMessage()));
    }
}
