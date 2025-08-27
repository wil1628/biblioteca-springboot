package com.example.biblioteca.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.biblioteca.model.Usuario;
import com.example.biblioteca.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {

    // Repositorio para acceder a los datos de Usuario
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor
    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Lista todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Guarda un nuevo usuario, validando username y rol
    public void guardarUsuario(Usuario usuario) {
        // Verifica si ya existe un usuario con el mismo username
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("Ya existe un usuario con ese username.");
        }
        // Verifica si ya existe un usuario con el mismo email
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email.");
        }

        // Verifica el rol
        String rol = usuario.getRol().toUpperCase();
        if (!rol.equals("ADMIN") && !rol.equals("USER")) {
            throw new RuntimeException("Rol inválido. Solo se permite ADMIN o USER.");
        }
        usuario.setRol(rol);

        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash())); // Encripta la contraseña
        usuarioRepository.save(usuario);
    }

    // Obtiene un usuario por su ID, lanza excepción si no existe
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Actualiza los datos de un usuario existente
    public void actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuario = obtenerPorId(id);

        // Verifica si ya existe un usuario con el mismo username
        if (!usuario.getUsername().equals(usuarioActualizado.getUsername()) &&
                usuarioRepository.existsByUsername(usuarioActualizado.getUsername())) {
            throw new RuntimeException("Ya existe otro usuario con ese username.");
        }

        // Verifica si ya existe un usuario con el mismo email
        if (!usuario.getEmail().equals(usuarioActualizado.getEmail()) &&
                usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
            throw new RuntimeException("Ya existe otro usuario con ese email.");
        }

        usuario.setUsername(usuarioActualizado.getUsername());
        usuario.setEmail(usuarioActualizado.getEmail());

        // Comprueba si la contraseña ha sido actualizada
        if (usuarioActualizado.getPasswordHash() != null &&
                !usuarioActualizado.getPasswordHash().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(usuarioActualizado.getPasswordHash()));
        }

        String nuevoRol = usuarioActualizado.getRol().toUpperCase();
        if (!nuevoRol.equals("ADMIN") && !nuevoRol.equals("USER")) {
            throw new RuntimeException("Rol inválido. Solo se permite ADMIN o USER.");
        }
        usuario.setRol(nuevoRol);

        usuarioRepository.save(usuario);
    }

    // Elimina un usuario por su ID
    public void eliminarUsuario(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }

    // Buscar un usuario por su username
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
