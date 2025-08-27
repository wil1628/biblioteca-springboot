package com.example.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ErrorController {

    // Manejo de errores
    @GetMapping("/acceso-denegado")
    public String accessDenied() {
        return "error/acceso-denegado";
    }
}
