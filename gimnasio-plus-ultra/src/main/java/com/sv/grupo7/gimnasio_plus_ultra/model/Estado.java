package com.sv.grupo7.gimnasio_plus_ultra.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private int id_estado;

    private String nombre_estado;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creado_en;

    public Estado() {}

    // Getters y Setters específicos de Estado
    public int getIdEstado() { return id_estado; } 
    public void setIdEstado(int id_estado) { this.id_estado = id_estado; } 

    public String getNombreEstado() { return nombre_estado; }
    public void setNombreEstado(String nombre_estado) { this.nombre_estado = nombre_estado; } 

    public LocalDateTime getCreadoEn() { return creado_en; }
    public void setCreadoEn(LocalDateTime creado_en) { this.creado_en = creado_en; }
}
