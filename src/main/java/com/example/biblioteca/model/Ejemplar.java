package com.example.biblioteca.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.persistence.*;

@Entity
@Table(name = "ejemplar") // Nombre de la tabla
public class Ejemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincrementable
    private Long id; // ID de ejemplar

    @NotBlank(message = "El código del ejemplar es obligatorio") // Validación de campo no vacío
    @Column(unique = true, nullable = false) // Columna única y no nula
    private String codigoEjemplar;

    @ManyToOne // Relación muchos a uno con libro
    @JoinColumn(name = "libro_id", nullable = false) // Columna referencia al libro
    private Libro libro;

    @NotNull(message = "Debe seleccionar un estado") // Validación de campo no nulo
    @Enumerated(EnumType.STRING) // Almacena el estado como una cadena
    private EstadoEjemplar estado = EstadoEjemplar.DISPONIBLE;

    // Enum para los estados del ejemplar
    public enum EstadoEjemplar { 
        DISPONIBLE, PRESTADO, DANADO
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoEjemplar() {
        return codigoEjemplar;
    }

    public void setCodigoEjemplar(String codigoEjemplar) {
        this.codigoEjemplar = codigoEjemplar;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public EstadoEjemplar getEstado() {
        return estado;
    }

    public void setEstado(EstadoEjemplar estado) {
        this.estado = estado;
    }

}