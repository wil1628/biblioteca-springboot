package com.example.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import jakarta.validation.constraints.Email; 

@Entity 
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "username" }) // Aseguramos que el username del usuario sea único
})
public class Usuario {
    @Id // Clave primaria 
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Id autoincrementable
    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar vacío") // Validación de campo
    @Column(nullable = false, unique = true) // Aseguramos que el username sea único y no nulo
    private String username;

    @NotBlank
    private String passwordHash; // Atributo para el hash de la contraseña del usuario

    @NotBlank
    private String rol; // Atributo para el rol del usuario (ADMIN, USER)

    @Email
    @NotBlank
    private String email; // Atributo para el email del usuario

    @OneToMany(mappedBy = "usuario") // Relación 1:N con la entidad Libro
    private List<Prestamo> prestamos; // Colección de préstamos asociados al usuario

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

}
