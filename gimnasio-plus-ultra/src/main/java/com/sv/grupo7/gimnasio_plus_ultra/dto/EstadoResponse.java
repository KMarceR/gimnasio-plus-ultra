package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDateTime;

public class EstadoResponse {

    private int id_estado;
    private String nombre_estado;
    private LocalDateTime creado_en;

    public EstadoResponse() {
    }

    public EstadoResponse(int id_estado, String nombre_estado, LocalDateTime creado_en) {
        this.id_estado = id_estado;
        this.nombre_estado = nombre_estado;
        this.creado_en = creado_en;
    }

    public int getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getNombreEstado() {
        return nombre_estado;
    }

    public void setNombreEstado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }

    public LocalDateTime getCreadoEn() {
        return creado_en;
    }

    public void setCreadoEn(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }
}
