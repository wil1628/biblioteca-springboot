package com.example.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincrementable
    private Long id;

    @ManyToOne // Relación muchos a uno con ejemplar
    @JoinColumn(name = "ejemplar_id", nullable = false) // Columna referencia al ejemplar
    private Ejemplar ejemplar;

    @ManyToOne // Relación muchos a uno con usuario
    @JoinColumn(name = "usuario_id", nullable = false) // Columna referencia al usuario
    private Usuario usuario;

    @NotNull(message = "La fecha de préstamo es obligatoria") // Validación de campo no nulo
    private LocalDate fechaPrestamo;

    @NotNull(message = "La fecha de vencimiento es obligatoria") // Validación de campo no nulo
    private LocalDate fechaVencimiento;

    private LocalDate fechaDevolucion;

    private Integer multaGenerada = 0;

    // Estado del préstamo
    @Enumerated(EnumType.STRING)
    private EstadoPrestamo estado = EstadoPrestamo.ACTIVO;

    // Enum para los estados del préstamo
    public enum EstadoPrestamo {
        ACTIVO, DEVUELTO, VENCIDO
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public void setEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Integer getMultaGenerada() {
        return multaGenerada;
    }

    public void setMultaGenerada(Integer multaGenerada) {
        this.multaGenerada = multaGenerada;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }
}