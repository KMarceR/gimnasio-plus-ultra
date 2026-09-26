package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDateTime;

public class RolResponse {

    private int id_rol;
    private String nombre_rol;
    private LocalDateTime creado_en;

    public RolResponse() {
    }

    public int getIdRol() {
        return id_rol;
    }

    public void setIdRol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getNombreRol() {
        return nombre_rol;
    }

    public void setNombreRol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }

    public LocalDateTime getCreadoEn() {
        return creado_en;
    }

    public void setCreadoEn(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }
}
