package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SesionRequest {

    private Integer id_cliente;
    private Integer id_entrenador;
    private Integer id_estado;
    private LocalDate fecha_sesion;
    private LocalTime hora_inicio;
    private LocalTime hora_fin;

    public SesionRequest() {
    }

    public SesionRequest(Integer id_cliente, Integer id_entrenador, Integer id_estado,
            LocalDate fecha_sesion, LocalTime hora_inicio, LocalTime hora_fin) {
        this.id_cliente = id_cliente;
        this.id_entrenador = id_entrenador;
        this.id_estado = id_estado;
        this.fecha_sesion = fecha_sesion;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
    }

    public Integer getIdCliente() {
        return id_cliente;
    }

    public void setIdCliente(Integer id_cliente) {
        this.id_cliente = id_cliente;
    }

    public Integer getIdEntrenador() {
        return id_entrenador;
    }

    public void setIdEntrenador(Integer id_entrenador) {
        this.id_entrenador = id_entrenador;
    }

    public Integer getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(Integer id_estado) {
        this.id_estado = id_estado;
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
}
