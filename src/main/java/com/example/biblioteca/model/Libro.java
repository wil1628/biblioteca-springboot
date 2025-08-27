package com.example.biblioteca.model;

import jakarta.persistence.*; // Importamos las anotaciones de JPA
import jakarta.validation.constraints.Min; // Importamos la anotación de validación Min
import jakarta.validation.constraints.NotBlank; // Importamos las anotaciones de validación

import java.util.ArrayList;
import java.util.HashSet; // Importamos la colección HashSet
import java.util.List; // Importamos la colección List 
import java.util.Set; // Importamos la colección Set

@Entity // Indicamos que esta clase es una entidad JPA
@Table(name = "libro")
public class Libro {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID
    private Long id;

    @NotBlank(message = "El título no puede estar vacío") // Validación de campo no vacío
    private String titulo;

    @NotBlank(message = "El ISBN no puede estar vacío") // Validación de campo no vacío
    private String isbn;

    private int anioPublicacion; // Atributo para el año de publicación del libro

    @Min(value = 1, message = "Debe haber al menos una unidad")
    private int unidades;

    @Column(length = 1000)
    private String descripcion; // Atributo para la descripción del libro

    // Relación muchos a muchos con la entidad Autor
    @ManyToMany
    @JoinTable(name = "libro_autor", // Nombre de la tabla
            joinColumns = @JoinColumn(name = "libro_id"), // Columna de unión para Libro
            inverseJoinColumns = @JoinColumn(name = "autor_id") // Columna de unión para Autor
    )
    private Set<Autor> autores = new HashSet<>(); // Colección de autores asociados al libro

    // Relación N:1 con categoria
    @ManyToOne
    @JoinColumn(name = "categoria_id") // Columna de unión para Categoria
    private Categoria categoria; // Atributo para la categoría del libro

    // Relación 1:N con Ejemplar
    @OneToMany(mappedBy = "libro", fetch = FetchType.LAZY)
    private List<Ejemplar> ejemplares = new ArrayList<>();

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Ejemplar> getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(List<Ejemplar> ejemplares) {
        this.ejemplares = ejemplares;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Libro))
            return false;
        Libro libro = (Libro) o;
        return id != null && id.equals(libro.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
