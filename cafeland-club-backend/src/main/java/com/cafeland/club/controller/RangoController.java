package com.cafeland.club.controller;

import com.cafeland.club.entity.Rango;
import com.cafeland.club.service.RangoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rangos")
public class RangoController {

    private final RangoService rangoService;

    public RangoController(RangoService rangoService) {
        this.rangoService = rangoService;
    }

    @GetMapping
    public List<Rango> listarTodos() {
        return rangoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rango> obtenerPorId(@PathVariable Long id) {
        return rangoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rango> guardar(@Valid @RequestBody Rango rango) {
        Rango rangoGuardado = rangoService.guardar(rango);
        return ResponseEntity.status(HttpStatus.CREATED).body(rangoGuardado);
    }
}
