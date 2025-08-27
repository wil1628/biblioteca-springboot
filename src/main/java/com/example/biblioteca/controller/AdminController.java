
package com.example.biblioteca.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.ui.Model;
import com.example.biblioteca.service.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    // Servicios
    private final UsuarioService usuarioService;
    private final LibroService libroService;
    private final CategoriaService categoriaService;
    private final AutorService autorService;
    private final EjemplarService ejemplarService;
    private final PrestamoService prestamoService;

    // Contructor
    public AdminController(UsuarioService usuarioService, LibroService libroService, CategoriaService categoriaService,
            AutorService autorService, EjemplarService ejemplarService, PrestamoService prestamoService) {
        this.usuarioService = usuarioService;
        this.libroService = libroService;
        this.categoriaService = categoriaService;
        this.autorService = autorService;
        this.ejemplarService = ejemplarService;
        this.prestamoService = prestamoService;
    }

    // PANEL PRINCIPAL 
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsuarios", usuarioService.listarUsuarios().size());
        model.addAttribute("totalLibros", libroService.listarLibros().size());
        model.addAttribute("totalCategorias", categoriaService.listarCategorias().size());
        model.addAttribute("totalAutores", autorService.listarAutores().size());
        model.addAttribute("totalEjemplares", ejemplarService.listarEjemplares().size());
        model.addAttribute("totalPrestamos", prestamoService.listarPrestamo().size());
        return "admin/dashboard";
    }

}
