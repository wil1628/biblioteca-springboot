package com.example.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.biblioteca.service.EjemplarService;
import com.example.biblioteca.service.LibroService;
import com.example.biblioteca.service.PrestamoService;
import com.example.biblioteca.service.UsuarioService;

import org.springframework.security.core.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    private final UsuarioService usuarioService;
    private final EjemplarService ejemplarService;
    private final PrestamoService prestamoService;
    private final LibroService libroService;

    // Constructor
    public CatalogoController(LibroService libroService,
            UsuarioService usuarioService,
            EjemplarService ejemplarService,
            PrestamoService prestamoService) {
        this.libroService = libroService;
        this.usuarioService = usuarioService;
        this.ejemplarService = ejemplarService;
        this.prestamoService = prestamoService;
    }

    // Muestra el catálogo público
    @GetMapping
    public String mostrarCatalogo(Model model, @RequestParam(required = false) String alquilado) {
        model.addAttribute("libros", libroService.listarLibros());
        model.addAttribute("alquilado", alquilado);
        return "catalogo";
    }

    // Acción de alquilar libro (requiere login)
    @GetMapping("/alquilar/{id}")
    public String alquilarLibro(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        // Verificamos si el usuario esta autenticado
        try {
            if (userDetails == null) {
                model.addAttribute("error", "Debe iniciar sesión para alquilar.");
                return "catalogo";
            }

            var usuario = usuarioService.buscarPorUsername(userDetails.getUsername()); // Obtenemos el usuario
            var ejemplar = ejemplarService.obtenerPorId(id); // Obtenemos el ejemplar
            var prestamo = prestamoService.crearPrestamo(usuario, ejemplar); // Creamos el préstamo

            // Redirigimos a la página del catálogo con el ID del préstamo
            return "redirect:/catalogo?alquilado=" + prestamo.getId();
        // Manejo de excepciones
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("libros", libroService.listarLibros());
            return "catalogo";
        }
    }
}