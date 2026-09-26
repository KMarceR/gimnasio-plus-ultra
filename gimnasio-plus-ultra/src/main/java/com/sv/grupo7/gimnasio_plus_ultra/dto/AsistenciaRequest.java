package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AsistenciaRequest {

    private int id_cliente;
    private int id_estado;
    private LocalDate fecha_asistencia;
    private LocalTime hora_sesion;

    public AsistenciaRequest() {
    }

    public AsistenciaRequest(int id_cliente, int id_estado, LocalDate fecha_asistencia, LocalTime hora_sesion) {
        this.id_cliente = id_cliente;
        this.id_estado = id_estado;
        this.fecha_asistencia = fecha_asistencia;
        this.hora_sesion = hora_sesion;
    }

    public int getIdCliente() {
        return id_cliente;
    }

    public void setIdCliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(int id_estado) {
        this.id_estado = id_estado;
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
}
