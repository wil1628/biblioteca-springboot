package com.example.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.biblioteca.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    Categoria findByNombre(String nombre); // Método para buscar una categoría por nombre
}
