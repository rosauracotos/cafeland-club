package com.cafeland.club.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "ligas")
public class Liga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private Integer numero;

    @NotNull
    @PositiveOrZero
    @Column(name = "minimo_puntos_torneo", nullable = false)
    private Integer minimoPuntosTorneo;

    public Liga() {
    }

    public Liga(Integer numero, Integer minimoPuntosTorneo) {
        this.numero = numero;
        this.minimoPuntosTorneo = minimoPuntosTorneo;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getMinimoPuntosTorneo() {
        return minimoPuntosTorneo;
    }

    public void setMinimoPuntosTorneo(Integer minimoPuntosTorneo) {
        this.minimoPuntosTorneo = minimoPuntosTorneo;
    }
}
