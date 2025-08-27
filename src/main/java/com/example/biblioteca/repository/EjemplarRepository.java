package com.example.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.biblioteca.model.Ejemplar;

import java.util.Optional;

import java.util.List;

public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {

    // Encuentra ejemplares por libro y estado
    List<Ejemplar> findByLibroIdAndEstadoNot(Long libroId, Ejemplar.EstadoEjemplar estado);

    // Encuentra el primer ejemplar por libro y estado
    Optional<Ejemplar> findFirstByLibroIdAndEstado(Long libroId, Ejemplar.EstadoEjemplar estado);

    // Verifica si un ejemplar existe por ID y estado
    boolean existsByIdAndEstado(Long id, Ejemplar.EstadoEjemplar estado);

    // Encuentra un ejemplar por su código
    Optional<Ejemplar> findByCodigoEjemplar(String codigoEjemplar);
}
