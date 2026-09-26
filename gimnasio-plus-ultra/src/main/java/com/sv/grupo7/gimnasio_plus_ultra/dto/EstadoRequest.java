package com.sv.grupo7.gimnasio_plus_ultra.dto;

public class EstadoRequest {

    private String nombre_estado;

    public EstadoRequest() {
    }

    public EstadoRequest(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }

    public String getNombreEstado() {
        return nombre_estado;
    }

    public void setNombreEstado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }
}
