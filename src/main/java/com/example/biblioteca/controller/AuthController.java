package com.example.biblioteca.controller;

import com.example.biblioteca.model.Usuario;
import com.example.biblioteca.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    // Constructor
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login";
    }

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }

    // Procesar el registro
    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Usuario usuario) {
        usuario.setRol("USER"); // Asigna el rol
        usuarioService.guardarUsuario(usuario);
        return "redirect:/auth/login?exito";
    }

    // Redireccionar según el rol del usuario
    @GetMapping("/default")
    public String redireccionPorRol(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/catalogo";
        }
    }

    // Acceso denegado
    @GetMapping("/acceso-denegado")
    public String mostrarAccesoDenegado() {
        return "auth/acceso-denegado";
    }

}
