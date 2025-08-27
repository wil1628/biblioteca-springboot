package com.example.biblioteca.controller;

import com.example.biblioteca.model.Ejemplar;
import com.example.biblioteca.service.EjemplarService;
import com.example.biblioteca.model.Libro;
import com.example.biblioteca.service.LibroService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/ejemplares/ejemplares")
public class EjemplarController {

    private final EjemplarService ejemplarService;
    private final LibroService libroService;

    // Constructor
    public EjemplarController(EjemplarService ejemplarService, LibroService libroService) {
        this.ejemplarService = ejemplarService;
        this.libroService = libroService;
    }

    // Muestra la lista de ejemplares
    @GetMapping
    public String listarEjemplares(Model model) {
        model.addAttribute("listaEjemplares", ejemplarService.listarEjemplares());
        return "admin/ejemplares/ejemplares";
    }

    // Muestra el formulario para crear un nuevo ejemplar
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("ejemplar", new Ejemplar());
        model.addAttribute("libros", libroService.listarLibros());
        return "admin/ejemplares/crear_ejemplar";
    }

    // Guarda un nuevo ejemplar
    @PostMapping("/guardar")
    public String guardarEjemplar(@Valid @ModelAttribute Ejemplar ejemplar,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        // Verificamos si hay errores de validación
        if (result.hasErrors()) {
            model.addAttribute("libros", libroService.listarLibros());
            return "admin/ejemplares/crear_ejemplar";
        }

        // Guardamos el ejemplar
        try {
            ejemplarService.guardarEjemplar(ejemplar);
            return "redirect:/admin/ejemplares/ejemplares";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/ejemplares/ejemplares/nuevo";
        }
    }

    // Muestra el formulario para editar un ejemplar existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("ejemplar", ejemplarService.obtenerPorId(id));
        model.addAttribute("libros", libroService.listarLibros());
        return "admin/ejemplares/editar_ejemplar";
    }

    // Actualiza los datos de un ejemplar existente
    @PostMapping("/actualizar/{id}")
    public String actualizarEjemplar(@PathVariable Long id,
            @RequestParam Long libroId,
            @Valid @ModelAttribute Ejemplar ejemplar,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
                
        ejemplar.setId(id);

        // Verificamos si hay errores de validación
        if (result.hasErrors()) {
            model.addAttribute("libros", libroService.listarLibros());
            model.addAttribute("error", "Por favor corrija los errores del formulario.");
            return "admin/ejemplares/editar_ejemplar";
        }

        // Asignamos el libro al ejemplar
        try {
            Libro libro = libroService.obtenerPorId(libroId);
            ejemplar.setLibro(libro);

            ejemplarService.actualizarEjemplar(id, ejemplar);

            redirectAttributes.addFlashAttribute("exito", "Ejemplar actualizado correctamente.");
            return "redirect:/admin/ejemplares/ejemplares";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/ejemplares/ejemplares/editar/" + id;
        }
    }

    // Elimina un ejemplar por su ID
    @GetMapping("/eliminar/{id}")
    public String eliminarEjemplar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ejemplarService.eliminarEjemplar(id);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/ejemplares/ejemplares/nuevo";
    }
}