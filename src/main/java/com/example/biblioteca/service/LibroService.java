package com.example.biblioteca.service;

import org.springframework.stereotype.Service; // Importamos la anotación Service

import com.example.biblioteca.model.Libro;
import com.example.biblioteca.repository.LibroRepository;

import java.util.List;

@Service // Anotación que indica que esta clase es un servicio de Spring
public class LibroService {

    private final LibroRepository libroRepository; // Repositorio para manejar operaciones con Libro

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository; // Inicializamos el repositorio a través de la inyección de dependencias
    }

    public List<Libro> listarLibros() {
        return libroRepository.findAll(); // Retorna una lista de todos los libros utilizando el repositorio
    }

    // Método para guardar un nuevo libro
    public void guardarLibro(Libro libro) {
        boolean existe = libroRepository.findByIsbn(libro.getIsbn()) != null; // Verifica si ya existe un libro con el mismo ISBN
        if (existe) {
            throw new RuntimeException("Ya existe un libro con ese ISBN"); // Lanza una excepción si el libro ya existe
        }
        libroRepository.save(libro); // Guarda el nuevo libro en la base de datos
    }

    // Método para Buscar un libro por su ID
    public Libro obtenerPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado")); // Retorna el libro si existe, o lanza una excepción si no
    }

    // Método para actualizar un libro
    public void actualizarLibro(Long id, Libro libroActualizado) {
        Libro libro = obtenerPorId(id); // Obtiene el libro existente por su ID
        libro.setTitulo(libroActualizado.getTitulo()); // Actualiza el título del libro
        libro.setAutores(libroActualizado.getAutores()); // Actualiza el autor
        libro.setIsbn(libroActualizado.getIsbn()); // Actualiza el isbn
        libro.setAnioPublicacion(libroActualizado.getAnioPublicacion()); // Actualiza la fecha de publicación
        libro.setCategoria(libroActualizado.getCategoria()); // Actualiza la categoría del libro
        libro.setUnidades(libroActualizado.getUnidades()); // Actualiza la cantidad
        libroRepository.save(libro); // Guarda los cambios en la base de datos
    }

    // Método para eliminar un libro
    public void eliminarLibro(Long id) {
        Libro libro = obtenerPorId(id); // Obtiene el libro existente por su ID
        libroRepository.delete(libro); // Elimina el libro de la base de datos
    }
    
}
