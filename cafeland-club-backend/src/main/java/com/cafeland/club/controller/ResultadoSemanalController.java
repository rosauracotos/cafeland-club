package com.cafeland.club.controller;

import com.cafeland.club.dto.resultado.ResultadoSemanalActualizarRequest;
import com.cafeland.club.dto.resultado.ResultadoSemanalCrearRequest;
import com.cafeland.club.dto.resultado.ResultadoSemanalLoteRequest;
import com.cafeland.club.dto.resultado.ResultadoSemanalResponse;
import com.cafeland.club.exception.ResultadoSemanalDuplicadoException;
import com.cafeland.club.service.ResultadoSemanalService;
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
@RequestMapping("/api/resultados-semanales")
public class ResultadoSemanalController {

    private final ResultadoSemanalService resultadoService;

    public ResultadoSemanalController(ResultadoSemanalService resultadoService) {
        this.resultadoService = resultadoService;
    }

    @GetMapping
    public List<ResultadoSemanalResponse> listarTodos() {
        return resultadoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoSemanalResponse> obtenerPorId(@PathVariable Long id) {
        return resultadoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/semana/{semanaId}")
    public List<ResultadoSemanalResponse> listarPorSemana(@PathVariable Long semanaId) {
        return resultadoService.listarPorSemana(semanaId);
    }

    @GetMapping("/miembro/{miembroId}")
    public List<ResultadoSemanalResponse> listarPorMiembro(@PathVariable Long miembroId) {
        return resultadoService.listarPorMiembro(miembroId);
    }

    @PostMapping
    public ResponseEntity<ResultadoSemanalResponse> registrar(
            @Valid @RequestBody ResultadoSemanalCrearRequest request
    ) {
        ResultadoSemanalResponse resultadoRegistrado = resultadoService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultadoRegistrado);
    }

    @PostMapping("/lote")
    public ResponseEntity<List<ResultadoSemanalResponse>> guardarLote(
            @Valid @RequestBody ResultadoSemanalLoteRequest request
    ) {
        return ResponseEntity.ok(resultadoService.guardarLote(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultadoSemanalResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResultadoSemanalActualizarRequest request
    ) {
        return resultadoService.actualizar(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(ResultadoSemanalDuplicadoException.class)
    public ResponseEntity<Map<String, String>> manejarResultadoDuplicado(
            ResultadoSemanalDuplicadoException exception
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", exception.getMessage()));
    }
}
