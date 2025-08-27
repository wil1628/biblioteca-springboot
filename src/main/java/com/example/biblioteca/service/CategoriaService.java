package com.example.biblioteca.service;

import org.springframework.stereotype.Service; // Importamos la anotación Service

import com.example.biblioteca.model.Categoria;
import com.example.biblioteca.repository.CategoriaRepository;

import java.util.List;

@Service // Anotación que indica que esta clase es un servicio de Spring
public class CategoriaService {

    private final CategoriaRepository categoriaRepository; // Repositorio para manejar operaciones con Categoria

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository; // Inicializamos el repositorio a través de la inyección de dependencias
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll(); // Retorna una lista de todas las categorías utilizando el repositorio
    }

    // Método para guardar una nueva categoría
    public void guardarCategoria(Categoria categoria) {
        boolean existe = categoriaRepository.findByNombre(categoria.getNombre()) != null; // Verifica si ya existe una categoria con el mismo nombre
        if (existe) {
            throw new RuntimeException("Ya existe una categoría con ese nombre"); // Lanza una excepción si la categoría ya existe
        }
        categoriaRepository.save(categoria); // Guarda la nueva categoría en la base de datos
    }

    // Método para Buscar una categoría
    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada")); // Retorna la categoría si existe
    }

    // Método para actualizar una categoría
    public void actualizarCategoria(Long id, Categoria categoriaActualizada) {
        Categoria categoria = obtenerPorId(id); // Obtiene la categoría existente por su ID
        categoria.setNombre(categoriaActualizada.getNombre()); // Actualiza el nombre de la categoría
        categoria.setDescripcion(categoriaActualizada.getDescripcion());
        categoriaRepository.save(categoria); // Guarda los cambios en la base de datos
    }

    // Método para eliminar una categoría
    public void eliminarCategoria(Long id) {

        Categoria categoria = obtenerPorId(id); // Obtiene la categoría existente por su ID
        if (!categoria.getLibros().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar una categoría asociada a libros");
        }
        categoriaRepository.delete(categoria); // Elimina la categoría de la base de datos
    }
}
