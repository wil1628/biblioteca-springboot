package com.example.biblioteca.controller;

import com.example.biblioteca.model.Usuario;
import com.example.biblioteca.model.Prestamo;
import com.example.biblioteca.service.PrestamoService;
import com.example.biblioteca.service.UsuarioService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PerfilController {

    private final UsuarioService usuarioService;
    private final PrestamoService prestamoService;

    public PerfilController(UsuarioService usuarioService,
            PrestamoService prestamoService) {
        this.usuarioService = usuarioService;
        this.prestamoService = prestamoService;
    }

    // Ver perfil con préstamos del usuario
    @GetMapping("/usuario/perfil")
    public String mostrarPerfil(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Usuario usuario = usuarioService.buscarPorUsername(userDetails.getUsername());
        Long idUsuario = usuario.getId();

        // Obtenemos los préstamos del usuario
        List<Prestamo> prestamos = prestamoService.obtenerPrestamosPorUsuario(idUsuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("prestamos", prestamos);

        return "usuario/perfil";
    }

    // Devolver el préstamo
    @PostMapping("/usuario/perfil/devolver/{id}")
    public String devolverPrestamoDesdePerfil(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            prestamoService.devolverPrestamo(id);
            redirectAttributes.addFlashAttribute("exito", "Préstamo devuelto correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo devolver el préstamo.");
        }
        return "redirect:/usuario/perfil";
    }
}