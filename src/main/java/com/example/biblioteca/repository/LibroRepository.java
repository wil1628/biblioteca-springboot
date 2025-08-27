package com.example.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.biblioteca.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long>{ 
    Libro findByIsbn(String isbn); // Método para buscar por isbn
}
