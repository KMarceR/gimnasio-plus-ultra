package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SesionRequest {

    private int id_cliente;
    private int id_entrenador;
    private int id_estado;
    private LocalDate fecha_sesion;
    private LocalTime hora_inicio;
    private LocalTime hora_fin;

    public SesionRequest() {
    }

    public SesionRequest(int id_cliente, int id_entrenador, int id_estado,
            LocalDate fecha_sesion, LocalTime hora_inicio, LocalTime hora_fin) {
        this.id_cliente = id_cliente;
        this.id_entrenador = id_entrenador;
        this.id_estado = id_estado;
        this.fecha_sesion = fecha_sesion;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
    }

    public int getIdCliente() {
        return id_cliente;
    }

    public void setIdCliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getIdEntrenador() {
        return id_entrenador;
    }

    public void setIdEntrenador(int id_entrenador) {
        this.id_entrenador = id_entrenador;
    }

    public int getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(int id_estado) {
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
