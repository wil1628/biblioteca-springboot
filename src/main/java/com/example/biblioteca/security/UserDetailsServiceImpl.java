package com.example.biblioteca.security;

import com.example.biblioteca.model.Usuario;
import com.example.biblioteca.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

// Implementación de UserDetailsService
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inyección del repositorio de usuarios
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para cargar un usuario por su nombre de usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new User(
                usuario.getUsername(),
                usuario.getPasswordHash(),
                true, 
                true, 
                true, 
                true, 
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + usuario.getRol())));
    }
}