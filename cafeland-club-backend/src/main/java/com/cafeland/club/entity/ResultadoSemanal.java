package com.cafeland.club.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(
        name = "resultados_semanales",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_resultados_semanales_miembro_semana",
                columnNames = {"miembro_id", "semana_id"}
        )
)
public class ResultadoSemanal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "miembro_id", nullable = false)
    private Miembro miembro;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "semana_id", nullable = false)
    private Semana semana;

    @NotNull
    @PositiveOrZero
    @Column(name = "puntos_desafio", nullable = false)
    private Integer puntosDesafio = 0;

    @NotNull
    @PositiveOrZero
    @Column(name = "puntos_torneo", nullable = false)
    private Integer puntosTorneo = 0;

    public ResultadoSemanal() {
    }

    public ResultadoSemanal(Miembro miembro, Semana semana) {
        this.miembro = miembro;
        this.semana = semana;
    }

    public Long getId() {
        return id;
    }

    public Miembro getMiembro() {
        return miembro;
    }

    public void setMiembro(Miembro miembro) {
        this.miembro = miembro;
    }

    public Semana getSemana() {
        return semana;
    }

    public void setSemana(Semana semana) {
        this.semana = semana;
    }

    public Integer getPuntosDesafio() {
        return puntosDesafio;
    }

    public void setPuntosDesafio(Integer puntosDesafio) {
        this.puntosDesafio = puntosDesafio;
    }

    public Integer getPuntosTorneo() {
        return puntosTorneo;
    }

    public void setPuntosTorneo(Integer puntosTorneo) {
        this.puntosTorneo = puntosTorneo;
    }
}
