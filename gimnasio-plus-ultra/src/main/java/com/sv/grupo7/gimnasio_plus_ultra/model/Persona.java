package com.sv.grupo7.gimnasio_plus_ultra.model;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private int id_persona;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String genero;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fecha_nacimiento;

    private String direccion;

    @Column(nullable = false, unique = true)
    private String email;

    private String telefono;

    @Column(nullable = false, unique = true)
    private String dui;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creado_en;

    public Persona() {}

    public int getIdPersona() { return id_persona; }
    public void setIdPersona(int id_persona) { this.id_persona = id_persona; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public LocalDate getFechaNacimiento() { return fecha_nacimiento; }
    public void setFechaNacimiento(LocalDate fecha_nacimiento) { this.fecha_nacimiento = fecha_nacimiento; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDui() { return dui; }
    public void setDui(String dui) { this.dui = dui; }

    public LocalDateTime getCreadoEn() { return creado_en; }
    public void setCreadoEn(LocalDateTime creado_en) { this.creado_en = creado_en; }
}
