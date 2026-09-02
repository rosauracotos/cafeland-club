package com.cafeland.club.repository;

import com.cafeland.club.entity.EstadoMiembro;
import com.cafeland.club.entity.Miembro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MiembroRepository extends JpaRepository<Miembro, Long> {

    List<Miembro> findByEstado(EstadoMiembro estado);

    boolean existsByNombreIgnoreCase(String nombre);
}
