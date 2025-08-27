package com.example.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.biblioteca.model.Autor;


public interface AutorRepository extends JpaRepository<Autor, Long>{
    Autor findByNombreAndPais(String nombre, String pais); // Método para buscar un autor por nombre y país
}