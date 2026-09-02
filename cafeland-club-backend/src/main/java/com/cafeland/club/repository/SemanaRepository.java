package com.cafeland.club.repository;

import com.cafeland.club.entity.Semana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SemanaRepository extends JpaRepository<Semana, Long> {

    List<Semana> findAllByOrderByNumeroSemanaAsc();

    boolean existsByNumeroSemana(Integer numeroSemana);

    boolean existsByNumeroSemanaAndIdNot(Integer numeroSemana, Long id);

    boolean existsByFechaInicioAndFechaFin(LocalDate fechaInicio, LocalDate fechaFin);

    boolean existsByFechaInicioAndFechaFinAndIdNot(LocalDate fechaInicio, LocalDate fechaFin, Long id);
}
