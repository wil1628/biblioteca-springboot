package com.example.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.biblioteca.model.Usuario; 
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByUsername(String username); // Método para buscar un usuario por username

    
    boolean existsByUsername(String username); // Para verificar si ya existe
    boolean existsByEmail(String email); // Por si quieres validar emails repetidos también
}