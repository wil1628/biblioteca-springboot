package com.example.biblioteca.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.biblioteca.model.Usuario;
import com.example.biblioteca.repository.UsuarioRepository;

import java.util.Optional;

@Component
public class DataSeeder implements CommandLineRunner {

    // Inyectamos el repositorio de usuario
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Inyectamos el codificador de contraseñas
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método que se ejecuta al iniciar la aplicación
    @Override
    public void run(String... args) throws Exception {
        Optional<Usuario> adminExistente = usuarioRepository.findByUsername("admin");

        // Verificamos si el usuario ya existe
        if (adminExistente.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin123")); // Contraseña cifrada
            admin.setEmail("admin@biblioteca.com");
            admin.setRol("ADMIN"); // Rol como String

            usuarioRepository.save(admin);
            System.out.println("Usuario ADMIN creado por defecto");
        } else {
            System.out.println("Usuario ADMIN ya existe");
        }
    }
}