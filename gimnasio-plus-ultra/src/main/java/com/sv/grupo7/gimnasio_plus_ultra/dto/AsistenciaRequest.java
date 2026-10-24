package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AsistenciaRequest {

    private Integer id_cliente;
    private Integer id_estado;
    private LocalDate fecha_asistencia;
    private LocalTime hora_sesion;
    private LocalTime horaSalida;   // para que recepcion corrija una marcacion

    public AsistenciaRequest() {
    }

    public Integer getIdCliente() {
        return id_cliente;
    }

    public void setIdCliente(Integer id_cliente) {
        this.id_cliente = id_cliente;
    }

    public Integer getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(Integer id_estado) {
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

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }
}
