package com.example.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.biblioteca.model.Usuario;
import com.example.biblioteca.service.UsuarioService;

@Controller
@RequestMapping("admin/usuarios/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Constructor
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Listar Usuario
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("listaUsuarios", usuarioService.listarUsuarios());
        return "admin/usuarios/usuarios";
    }

    // Mostrar formulario para nuevo usuario
    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/usuarios/crear_usuario";
    }

    // Guardar usuario
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, Model model) {
        try {
            usuarioService.guardarUsuario(usuario);
            return "redirect:/admin/usuarios/usuarios";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "admin/usuarios/crear_usuario";
        }
    }

    // Editar usuario
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.obtenerPorId(id));
        return "admin/usuarios/editar_usuario";
    }

    // Actualizar usuario
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario, Model model) {
        try {
            usuarioService.actualizarUsuario(id, usuario);
            return "redirect:/admin/usuarios/usuarios";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "admin/usuarios/editar_usuario";
        }
    }

    // Eliminar usuario
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/admin/usuarios/usuarios";
    }
}
