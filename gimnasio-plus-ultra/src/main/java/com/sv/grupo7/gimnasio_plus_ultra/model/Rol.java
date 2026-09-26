package com.sv.grupo7.gimnasio_plus_ultra.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private int id_rol;

    private String nombre_rol;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creado_en;

    public Rol() {}

    // Getters y Setters específicos de Rol
    public int getIdRol() { return id_rol; }
    public void setIdRol(int id_rol) { this.id_rol = id_rol; }

    public String getNombreRol() { return nombre_rol; }
    public void setNombreRol(String nombre_rol) { this.nombre_rol = nombre_rol; }

    public LocalDateTime getCreadoEn() { return creado_en; }
    public void setCreadoEn(LocalDateTime creado_en) { this.creado_en = creado_en; }
}
