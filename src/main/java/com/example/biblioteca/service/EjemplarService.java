package com.example.biblioteca.service;

import com.example.biblioteca.model.Ejemplar;
import com.example.biblioteca.model.Libro;
import com.example.biblioteca.model.Ejemplar.EstadoEjemplar;
import com.example.biblioteca.repository.EjemplarRepository;
import com.example.biblioteca.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EjemplarService {

    private final EjemplarRepository ejemplarRepository;
    public final LibroRepository libroRepository;

    // Constructor
    public EjemplarService(EjemplarRepository ejemplarRepository, LibroRepository libroRepository) {
        this.ejemplarRepository = ejemplarRepository;
        this.libroRepository = libroRepository;
    }

    // Listar ejemplares
    public List<Ejemplar> listarEjemplares() {
        return ejemplarRepository.findAll();
    }

    // Guardar ejemplar
    public void guardarEjemplar(Ejemplar ejemplar) {
        // Valida el código del ejemplar
        if (ejemplar.getCodigoEjemplar() == null || ejemplar.getCodigoEjemplar().trim().isEmpty()) {
            throw new RuntimeException("El código del ejemplar no puede estar vacío.");
        }

        // Validar libro
        if (ejemplar.getLibro() == null) {
            throw new RuntimeException("El ejemplar debe estar asociado a un libro.");
        }

        // Verifica si ya existe un ejemplar con el mismo código
        boolean existe = ejemplarRepository.findByCodigoEjemplar(ejemplar.getCodigoEjemplar()).isPresent();
        if (existe) {
            throw new RuntimeException("Ya existe un ejemplar con el código: " + ejemplar.getCodigoEjemplar());
        }
        // Cargar el libro desde la base de datos para asegurar que es válido
        Libro libro = libroRepository.findById(ejemplar.getLibro().getId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        ejemplar.setLibro(libro);
        ejemplarRepository.save(ejemplar);

        ejemplarRepository.save(ejemplar);
    }

    // Obtener ejemplar por ID
    public Ejemplar obtenerPorId(Long id) {
        return ejemplarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado"));
    }

    // Actualizar ejemplar
    public Ejemplar actualizarEjemplar(Long id, Ejemplar ejemplarActualizado) {
        Ejemplar existente = ejemplarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado"));

        if (ejemplarActualizado.getLibro() == null) {
            throw new RuntimeException("Debe seleccionar un libro válido");
        }

        actualizarDatos(existente, ejemplarActualizado);

        return ejemplarRepository.save(existente);
    }

    // Eliminar ejemplar
    public void eliminarEjemplar(Long id) {
        Ejemplar ejemplar = obtenerPorId(id);
        if (ejemplar.getEstado() == EstadoEjemplar.PRESTADO) {
            throw new RuntimeException("No se puede eliminar un ejemplar que está prestado");
        }
        ejemplarRepository.delete(ejemplar);
    }

    // Método para actualizar los datos de un ejemplar
    private void actualizarDatos(Ejemplar ejemplar, Ejemplar ejemplarActualizado) {
        ejemplar.setCodigoEjemplar(ejemplarActualizado.getCodigoEjemplar());
        ejemplar.setEstado(ejemplarActualizado.getEstado());
        ejemplar.setLibro(ejemplarActualizado.getLibro());
    }

    // Obtener ejemplar disponible por libro
    public Ejemplar obtenerEjemplarDisponiblePorLibro(Long libroId) throws Exception {
        return ejemplarRepository.findFirstByLibroIdAndEstado(libroId, Ejemplar.EstadoEjemplar.DISPONIBLE)
                .orElseThrow(() -> new Exception("Ejemplar no disponible para este libro."));
    }
}