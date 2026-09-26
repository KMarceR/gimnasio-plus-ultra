package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDateTime;

public class ClienteResponse extends PersonaResponse {

    private int id_cliente;
    private String nombre_suscripcion;
    private String nombre_estado;
    private String notas;
    private LocalDateTime creado_en;

    public ClienteResponse() {
        super();
    }

    public int getIdCliente() {
        return id_cliente;
    }

    public void setIdCliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombreSuscripcion() {
        return nombre_suscripcion;
    }

    public void setNombreSuscripcion(String nombre_suscripcion) {
        this.nombre_suscripcion = nombre_suscripcion;
    }

    public String getNombreEstado() {
        return nombre_estado;
    }

    public void setNombreEstado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public LocalDateTime getCreadoEn() {
        return creado_en;
    }

    public void setCreadoEn(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }
}
