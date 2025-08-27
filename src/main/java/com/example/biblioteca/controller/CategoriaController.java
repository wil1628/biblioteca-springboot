package com.example.biblioteca.controller;

import com.example.biblioteca.model.Categoria;
import com.example.biblioteca.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categorias/categorias")
public class CategoriaController {
    
    private final CategoriaService categoriaService;

    // Constructor
    public CategoriaController(CategoriaService categoriaService){
        this.categoriaService = categoriaService;
    }

    //Lista de categorias
    @GetMapping
    public String listarCategorias(Model model, @RequestParam(value = "error", required = false) String error){
        model.addAttribute("listarCategorias", categoriaService.listarCategorias());
        if (error != null){
            model.addAttribute("error", error);
        }
        return "admin/categorias/categorias";
    }

    //Método para crear una categoria
    @GetMapping("/nueva")
    public String mostrarCategoriaNueva(Model model){
        model.addAttribute("categoria", new Categoria());
        return "admin//categorias/crear_categoria";

    }

    //Método para guardar una categoria
    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria){
        categoriaService.guardarCategoria(categoria);
        return "redirect:/admin/categorias/categorias";
    }

    //Método para editar una categoria
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model){
        model.addAttribute("categoria", categoriaService.obtenerPorId(id));
        return "admin/categorias/editar_categoria";
    }

    //Método para actualizar una categoria
    @PostMapping("/actualizar/{id}")
    public String actualizarCategoria(@PathVariable Long id, @ModelAttribute Categoria categoria){
        categoriaService.actualizarCategoria(id, categoria);
        return "redirect:/admin/categorias/categorias";
    }

    //Método para eliminar una categoria
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try{
            categoriaService.eliminarCategoria(id);
        } catch (IllegalStateException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categorias/categorias";
    }
}
