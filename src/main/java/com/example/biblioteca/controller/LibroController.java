package com.example.biblioteca.controller;

import com.example.biblioteca.model.Autor;
import com.example.biblioteca.model.Libro;
import com.example.biblioteca.service.LibroService;
import com.example.biblioteca.service.CategoriaService;
import com.example.biblioteca.service.AutorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controlador para gestionar las operaciones CRUD de libros.

@Controller
@RequestMapping("/admin/libros/libros")
public class LibroController {

    private final LibroService libroService;
    private final CategoriaService categoriaService;
    private final AutorService autorService;

    // Constructor para inyectar los servicios necesarios.
    public LibroController(LibroService libroService, CategoriaService categoriaService, AutorService autorService) {
        this.libroService = libroService;
        this.categoriaService = categoriaService;
        this.autorService = autorService;
    }

    // Muestra la lista de libros.
    @GetMapping
    public String listarLibros(Model model) {
        model.addAttribute("listaLibros", libroService.listarLibros());
        return "admin/libros/libros";
    }

    // Muestra el formulario para crear un nuevo libro.
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("autores", autorService.listarAutores());
        return "admin/libros/crear_libro";
    }

    // Guarda un nuevo libro en la base de datos.

    @PostMapping("/guardar")
    public String guardarLibro(@ModelAttribute Libro libro) {
        libroService.guardarLibro(libro);
        return "redirect:/admin/libros/libros";
    }

    // Muestra el formulario para editar un libro existente.

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("libro", libroService.obtenerPorId(id));
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("autores", autorService.listarAutores());
        return "/admin/libros/editar_libros";
    }

    // Actualiza los datos de un libro existente.

    @PostMapping("/actualizar/{id}")
    public String actualizarLibro(@PathVariable Long id, @ModelAttribute Libro libro) {
        libroService.actualizarLibro(id, libro);
        return "redirect:/admin/libros/libros";
    }

    // Elimina un libro por su ID.
    @GetMapping("/eliminar/{id}")
    public String eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return "redirect:/admin/libros/libros";
    }

    // Agregar un autor nuevo
    @PostMapping("/autor/nuevo")
    public String guardarAutorDesdeFormularioLibro(@RequestParam String nombre,
            @RequestParam String pais) {
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setPais(pais);
        autorService.guardarAutor(autor);

        return "redirect:/admin/libros/libros/libros/nuevo";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleLibro(@PathVariable Long id, Model model) {
        Libro libro = libroService.obtenerPorId(id);
        model.addAttribute("libro", libro);
        model.addAttribute("ejemplares", libro.getEjemplares());
        return "admin/libros/detalle_libro";
    }
}
