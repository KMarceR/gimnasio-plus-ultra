package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SesionResponse {

    private int id_sesion;
    private String nombre_cliente;
    private String nombre_entrenador;
    private String nombre_estado;
    private LocalDate fecha_sesion;
    private LocalTime hora_inicio;
    private LocalTime hora_fin;
    private LocalDateTime creado_en;

    public SesionResponse() {
    }

    public int getIdSesion() {
        return id_sesion;
    }

    public void setIdSesion(int id_sesion) {
        this.id_sesion = id_sesion;
    }

    public String getNombreCliente() {
        return nombre_cliente;
    }

    public void setNombreCliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getNombreEntrenador() {
        return nombre_entrenador;
    }

    public void setNombreEntrenador(String nombre_entrenador) {
        this.nombre_entrenador = nombre_entrenador;
    }

    public String getNombreEstado() {
        return nombre_estado;
    }

    public void setNombreEstado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }

    public LocalDate getFechaSesion() {
        return fecha_sesion;
    }

    public void setFechaSesion(LocalDate fecha_sesion) {
        this.fecha_sesion = fecha_sesion;
    }

    public LocalTime getHoraInicio() {
        return hora_inicio;
    }

    public void setHoraInicio(LocalTime hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public LocalTime getHoraFin() {
        return hora_fin;
    }

    public void setHoraFin(LocalTime hora_fin) {
        this.hora_fin = hora_fin;
    }

    public LocalDateTime getCreadoEn() {
        return creado_en;
    }

    public void setCreadoEn(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }
}
