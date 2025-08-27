package com.example.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.biblioteca.model.Ejemplar;
import com.example.biblioteca.model.Prestamo;
import com.example.biblioteca.model.Usuario;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

        // Cuenta los préstamos activos de un usuario
        int countByUsuarioAndFechaDevolucionIsNull(Usuario usuario);

        // Encuentra préstamos activos por usuario
        List<Prestamo> findByUsuarioAndFechaDevolucionIsNull(Usuario usuario);

        // Busca préstamos por usuario
        List<Prestamo> findByUsuarioOrderByFechaPrestamoDesc(Usuario usuario);

        // Verifica si un préstamo existe por ejemplar y estado
        boolean existsByEjemplarAndFechaDevolucionIsNull(Ejemplar ejemplar);

        // Busca préstamos por usuario y estado
        List<Prestamo> findByUsuarioIdAndEstado(Long idUsuario, Prestamo.EstadoPrestamo estado);

        // Busca préstamos por usuario y ordena por fecha de préstamo
        List<Prestamo> findByUsuarioIdOrderByFechaPrestamoDesc(Long idUsuario);

}