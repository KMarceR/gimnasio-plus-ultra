package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AsistenciaResponse {

    private int id_asistencia;
    private String nombre_cliente;
    private String nombre_estado;
    private LocalDate fecha_asistencia;
    private LocalTime hora_sesion;
    private LocalDateTime creado_en;

    public AsistenciaResponse() {
    }

    public AsistenciaResponse(int id_asistencia, String nombre_cliente, String nombre_estado,
            LocalDate fecha_asistencia, LocalTime hora_sesion, LocalDateTime creado_en) {
        this.id_asistencia = id_asistencia;
        this.nombre_cliente = nombre_cliente;
        this.nombre_estado = nombre_estado;
        this.fecha_asistencia = fecha_asistencia;
        this.hora_sesion = hora_sesion;
        this.creado_en = creado_en;
    }

    public int getIdAsistencia() {
        return id_asistencia;
    }

    public void setIdAsistencia(int id_asistencia) {
        this.id_asistencia = id_asistencia;
    }

    public String getNombreCliente() {
        return nombre_cliente;
    }

    public void setNombreCliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getNombreEstado() {
        return nombre_estado;
    }

    public void setNombreEstado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }

    public LocalDate getFechaAsistencia() {
        return fecha_asistencia;
    }

    public void setFechaAsistencia(LocalDate fecha_asistencia) {
        this.fecha_asistencia = fecha_asistencia;
    }

    public LocalTime getHoraSesion() {
        return hora_sesion;
    }

    public void setHoraSesion(LocalTime hora_sesion) {
        this.hora_sesion = hora_sesion;
    }

    public LocalDateTime getCreadoEn() {
        return creado_en;
    }

    public void setCreadoEn(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }
}
