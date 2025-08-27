package com.example.biblioteca.controller;

import com.example.biblioteca.model.*;
import com.example.biblioteca.service.*;
import com.example.biblioteca.repository.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/prestamos/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;
    private final UsuarioRepository usuarioRepository;
    private final EjemplarRepository ejemplarRepository;

    public PrestamoController(PrestamoService prestamoService,
            UsuarioRepository usuarioRepository,
            EjemplarRepository ejemplarRepository) {
        this.prestamoService = prestamoService;
        this.usuarioRepository = usuarioRepository;
        this.ejemplarRepository = ejemplarRepository;
    }

    // Listar préstamos
    @GetMapping
    public String listarPrestamos(Model model) {
        model.addAttribute("listaPrestamos", prestamoService.listarPrestamo());
        return "admin/prestamos/prestamos";
    }

    // Mostrar formulario para nuevo préstamo
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("ejemplares", ejemplarRepository.findAll());
        return "admin/prestamos/crear_prestamo";
    }

    // Guardar nuevo préstamo
    @PostMapping("/guardar")
    public String guardarPrestamo(@RequestParam Long usuarioId,
            @RequestParam Long ejemplarId,
            RedirectAttributes redirectAttributes,
            Model model) {
        // Verificamos si hay errores de validación
        try {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            Ejemplar ejemplar = ejemplarRepository.findById(ejemplarId)
                    .orElseThrow(() -> new IllegalArgumentException("Ejemplar no encontrado"));

            prestamoService.crearPrestamo(usuario, ejemplar);
            redirectAttributes.addFlashAttribute("exito", "Préstamo creado correctamente.");
            return "redirect:/admin/prestamos/prestamos";

        // Si ocurre un error, regresamos al formulario
        } catch (Exception e) {
            model.addAttribute("usuarios", usuarioRepository.findAll());
            model.addAttribute("ejemplares", ejemplarRepository.findAll());
            model.addAttribute("error", e.getMessage());
            return "admin/prestamos/crear_prestamo";
        }
    }

    // Devolver préstamo
    @PostMapping("/devolver/{id}")
    public String devolverPrestamo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            prestamoService.devolverPrestamo(id);
            redirectAttributes.addFlashAttribute("exito", "Préstamo devuelto correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/prestamos/prestamos";
    }

    // Limpiar multa
    @PostMapping("/limpiar-multa/{id}")
    public String limpiarMulta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            prestamoService.limpiarMulta(id);
            redirectAttributes.addFlashAttribute("exito", "Multa limpiada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/prestamos/prestamos";
    }

    // Eliminar préstamo
    @GetMapping("/eliminar/{id}")
    public String eliminarPrestamo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            prestamoService.eliminarPrestamo(id);
            redirectAttributes.addFlashAttribute("exito", "Préstamo eliminado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/prestamos/prestamos";
    }
}
