package com.cafeland.club.repository;

import com.cafeland.club.entity.ResultadoSemanal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResultadoSemanalRepository extends JpaRepository<ResultadoSemanal, Long> {

    List<ResultadoSemanal> findBySemana_IdOrderByMiembro_NombreAsc(Long semanaId);

    List<ResultadoSemanal> findByMiembro_IdOrderBySemana_NumeroSemanaAsc(Long miembroId);

    boolean existsByMiembro_IdAndSemana_Id(Long miembroId, Long semanaId);

    boolean existsByMiembro_IdAndSemana_IdAndIdNot(Long miembroId, Long semanaId, Long id);

    Optional<ResultadoSemanal> findByMiembro_IdAndSemana_Id(Long miembroId, Long semanaId);
}
