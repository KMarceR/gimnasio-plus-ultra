package com.sv.grupo7.gimnasio_plus_ultra.dto;

public class RolRequest {

    private String nombre_rol;

    public RolRequest() {
    }

    public RolRequest(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }

    public String getNombreRol() {
        return nombre_rol;
    }

    public void setNombreRol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }
}
