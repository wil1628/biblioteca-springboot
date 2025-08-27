package com.example.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Pantalla de "Inicio"
    @GetMapping("/")
    public String mostrarInicio() {
        return "index";
    }

    // Pantalla de "Nosotros"
    @GetMapping("/nosotros")
    public String mostrarNosotros() {
        return "nosotros";
    }

    // Pantalla de "Contacto"
    @GetMapping("/contacto")
    public String mostrarContacto() {
        return "contacto";
    }
}
