package com.example.biblioteca.service;

import org.springframework.stereotype.Service; // Importamos la anotación Service

import com.example.biblioteca.model.Autor;
import com.example.biblioteca.repository.AutorRepository;

import java.util.List;

@Service // Anotación que indica que esta clase es un servicio de Spring
public class AutorService {

    private final AutorRepository autorRepository; // Repositorio para manejar operaciones con Autor

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository; // Inicializamos el repositorio a través de la inyección de dependencias
    }

    public List<Autor> listarAutores() {
        return autorRepository.findAll(); // Retorna una lista de todos los autores utilizando el repositorio
    }

    // Método para guardar un nuevo autor
    public void guardarAutor(Autor autor) {
        boolean existe = autorRepository.findByNombreAndPais(
                autor.getNombre(),
                autor.getPais()) != null; // Verifica si ya existe un autor con el mismo nombre y país
        if (existe) {
            throw new RuntimeException("Ya existe un autor con ese nombre y país"); // Lanza una excepción si el autor ya existe
        }
        autorRepository.save(autor); // Guarda el nuevo autor en la base de datos
    }

    // Método para Buscar un autor por su ID
    public Autor obtenerPorId(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado")); // Retorna el autor si existe, o lanza una excepción si no
    }

    // Método para actualizar un autor
    public void actualizarAutor(Long id, Autor autorActualizado) {
        Autor autor = obtenerPorId(id); // Obtiene el autor existente por su ID
        autor.setNombre(autorActualizado.getNombre()); // Actualiza el nombre del autor
        autor.setPais(autorActualizado.getPais()); // Actualiza el país del autor
        autorRepository.save(autor); // Guarda los cambios en la base de datos
    }

    // Método para eliminar un autor
    public void eliminarAutor(Long id) {
        Autor autor = obtenerPorId(id); // Obtiene el autor existente por su ID
        autorRepository.delete(autor); // Elimina el autor de la base de datos
    }

    // Para verificar que tiene libros asociados
    public boolean tieneLibrosAsociados(Long autorId) {
    Autor autor = obtenerPorId(autorId);
    return autor.getLibros() != null && !autor.getLibros().isEmpty();
}

}
