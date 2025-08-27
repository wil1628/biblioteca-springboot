package com.example.biblioteca.model;

import jakarta.persistence.Entity; //Importamos las anotaciones de JPA
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank; // Importamos las anotaciones de validación
import java.util.HashSet; // Importamos la colección HashSet
import java.util.Set; // Importamos la colección Set

@Entity // Indicamos que esta clase es una entidad JPA
@Table(name = "autor", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "nombre", "pais" })
})
public class Autor {
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío") // Validación de campo no vacío
    private String nombre;

    @NotBlank(message = "El país no puede estar vacío") // Validación de campo no vacío
    private String pais;

    @ManyToMany(mappedBy = "autores") // Relación muchos a muchos con la entidad Libro
    private Set<Libro> libros = new HashSet<>(); // Colección de libros asociados al autor

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Set<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
    }
}
