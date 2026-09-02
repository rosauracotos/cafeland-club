package com.cafeland.club.controller;

import com.cafeland.club.dto.liga.LigaActualizarRequest;
import com.cafeland.club.dto.liga.LigaCrearRequest;
import com.cafeland.club.dto.liga.LigaResponse;
import com.cafeland.club.exception.LigaDuplicadaException;
import com.cafeland.club.service.LigaService;
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
@RequestMapping("/api/ligas")
public class LigaController {

    private final LigaService ligaService;

    public LigaController(LigaService ligaService) {
        this.ligaService = ligaService;
    }

    @GetMapping
    public List<LigaResponse> listarTodas() {
        return ligaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LigaResponse> obtenerPorId(@PathVariable Long id) {
        return ligaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LigaResponse> registrar(@Valid @RequestBody LigaCrearRequest request) {
        LigaResponse ligaRegistrada = ligaService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ligaRegistrada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LigaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LigaActualizarRequest request
    ) {
        return ligaService.actualizar(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(LigaDuplicadaException.class)
    public ResponseEntity<Map<String, String>> manejarLigaDuplicada(LigaDuplicadaException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", exception.getMessage()));
    }
}
