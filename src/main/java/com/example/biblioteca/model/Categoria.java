package com.example.biblioteca.model;

import jakarta.persistence.*; // Importamos las anotaciones de JPA
import jakarta.validation.constraints.NotBlank; // Importamos las anotaciones de validación
import java.util.List; // Importamos la colección List

@Entity
@Table(name = "categoria", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "nombre" }) // Aseguramos que el nombre de la categoría sea único
})
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Id autoincrementable
    private Long id;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío") // Validación de campo
    private String nombre;

    private String descripcion; // Atributo para la descripción de la categoría

    @OneToMany(mappedBy = "categoria") // Relación 1:N con la entidad Libro
    private List<Libro> libros;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}
