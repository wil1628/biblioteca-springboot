package com.example.biblioteca.controller;

import com.example.biblioteca.model.Autor;
import com.example.biblioteca.service.AutorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador para gestionar CRUD de autores
@Controller
@RequestMapping("/admin/autores/autores")
public class AutorController {

    private final AutorService autorService;

    // Costructor para inyectar el servicio de autor

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    // Muestra la lista de autores.
    @GetMapping
    public String listarAutores(Model model) {
        model.addAttribute("listaAutores", autorService.listarAutores());
        return "admin/autores/autores";
    }

    // Muestra el formulario para crear un nuevo autor.
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("autor", new Autor());
        return "admin/autores/crear_autor";
    }

    // Guarda un nuevo autor.

    @PostMapping("/guardar")
    public String guardarAutor(@ModelAttribute Autor autor) {
        autorService.guardarAutor(autor);
        return "redirect:/admin/autores/autores";
    }

    // Muestra el formulario para editar un autor existente.

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("autor", autorService.obtenerPorId(id));
        return "admin/autores/editar_autor";
    }

    // Actualiza los datos de un autor existente.

    @PostMapping("/actualizar/{id}")
    public String actualizarAutor(@PathVariable Long id, @ModelAttribute Autor autor) {
        autorService.actualizarAutor(id, autor);
        return "redirect:/admin/autores/autores";
    }

    // Elimina un autor por su ID.
    @GetMapping("/eliminar/{id}")
    public String eliminarAutor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (autorService.tieneLibrosAsociados(id)) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar el autor porque tiene libros asociados.");
            return "redirect:/admin/autores/autores";
        }
        autorService.eliminarAutor(id);
        return "redirect:/admin/autores/autores";
    }

    // Agregar un autor nuevo
    @PostMapping("/autor/nuevo")
    public String guardarAutorDesdeFormularioLibro(@RequestParam String nombre,
            @RequestParam String pais) {
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setPais(pais);
        autorService.guardarAutor(autor);

        return "redirect:/admin/libros/nuevo";
    }

}
