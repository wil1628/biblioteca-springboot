package com.example.biblioteca.service;

import com.example.biblioteca.model.*;
import com.example.biblioteca.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final EjemplarRepository ejemplarRepository;

    // Constructor
    public PrestamoService(PrestamoRepository prestamoRepository, EjemplarRepository ejemplarRepository) {
        this.prestamoRepository = prestamoRepository;
        this.ejemplarRepository = ejemplarRepository;
    }

    // Listar préstamos
    public List<Prestamo> listarPrestamo() {
        List<Prestamo> prestamos = prestamoRepository.findAll();

        LocalDate hoy = LocalDate.now();

        // Recorre todos los préstamos y calcula multas si estan vencidas
        for (Prestamo p : prestamos) {
            if (p.getEstado() == Prestamo.EstadoPrestamo.ACTIVO && hoy.isAfter(p.getFechaVencimiento())) {
                long diasRetraso = ChronoUnit.DAYS.between(p.getFechaVencimiento(), hoy); // Días de retraso
                p.setMultaGenerada((int) (diasRetraso * 300)); // Multa: 300 por día
                prestamoRepository.save(p); // guardar el cálculo
            }
        }

        return prestamos;
    }

    // Crear préstamo
    public Prestamo crearPrestamo(Usuario usuario, Ejemplar ejemplar) throws Exception {
        validarEjemplarDisponible(ejemplar); // Validar que el ejemplar esté disponible
        validarLimitePrestamos(usuario); // Validar límite de préstamos
        validarMultasPendientes(usuario); // Validar que el usuario no tenga multas pendientes

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setEjemplar(ejemplar);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaVencimiento(LocalDate.now().plusDays(1)); //Fecha de vencimiento: 1 (para poder generar multas y verificar que funciona lo deje en 1 día)
        prestamo.setMultaGenerada(0); // Multa en 0
        prestamo.setEstado(Prestamo.EstadoPrestamo.ACTIVO);

        ejemplar.setEstado(Ejemplar.EstadoEjemplar.PRESTADO); // Cambia estado a prestado
        ejemplarRepository.save(ejemplar);

        return prestamoRepository.save(prestamo);

    }

    // Devolver ejemplar
    public Prestamo devolverPrestamo(Long idPrestamo) throws Exception {
        // Buscar préstamo por ID
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new Exception("Préstamo no encontrado."));

        // Verifica si el préstamo ya fue devuelto
        if (prestamo.getFechaDevolucion() != null) {
            throw new Exception("El préstamo ya fue devuelto.");
        }

        LocalDate hoy = LocalDate.now();
        prestamo.setFechaDevolucion(hoy); // Fecha de devolución
        prestamo.setEstado(Prestamo.EstadoPrestamo.DEVUELTO);

        // Si se devuelve tarde, calcula la multa
        if (hoy.isAfter(prestamo.getFechaVencimiento())) {
            long diasRetraso = ChronoUnit.DAYS.between(prestamo.getFechaVencimiento(), hoy);
            prestamo.setMultaGenerada((int) (diasRetraso * 300));
        }

        // Ejemplar vuelve a estar disponible
        Ejemplar ejemplar = prestamo.getEjemplar();
        ejemplar.setEstado(Ejemplar.EstadoEjemplar.DISPONIBLE);
        ejemplarRepository.save(ejemplar);

        return prestamoRepository.save(prestamo);
    }

    // Limpiar multa
    public Prestamo limpiarMulta(Long idPrestamo) throws Exception {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new Exception("Préstamo no encontrado."));
        prestamo.setMultaGenerada(0);
        return prestamoRepository.save(prestamo);
    }

    // Obtener todos los préstamos del usuario
    public List<Prestamo> obtenerPrestamosPorUsuario(Long idUsuario) {
        return prestamoRepository.findByUsuarioIdOrderByFechaPrestamoDesc(idUsuario);
    }

    // Validar que el ejemplar este disponible
    private void validarEjemplarDisponible(Ejemplar ejemplar) throws Exception {
        if (ejemplar.getEstado() != Ejemplar.EstadoEjemplar.DISPONIBLE) {
            throw new Exception("El ejemplar ya está prestado o no disponible.");
        }
    }

    // Validar límite de préstamos
    private void validarLimitePrestamos(Usuario usuario) throws Exception {
        int prestamosActivos = prestamoRepository.countByUsuarioAndFechaDevolucionIsNull(usuario);
        if (prestamosActivos >= 2) {
            throw new Exception("El usuario ya tiene 2 préstamos activos.");
        }
    }

    // Validar multas pendientes
    private void validarMultasPendientes(Usuario usuario) throws Exception {
        List<Prestamo> activos = prestamoRepository.findByUsuarioAndFechaDevolucionIsNull(usuario);
        boolean tieneMulta = activos.stream().anyMatch(p -> p.getMultaGenerada() != null && p.getMultaGenerada() > 0);
        if (tieneMulta) {
            throw new Exception("El usuario tiene multas pendientes.");
        }
    }

    // Eliminar préstamos
    public void eliminarPrestamo(Long id) throws Exception {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new Exception("Préstamo no encontrado."));
        prestamoRepository.delete(prestamo);
    }

}