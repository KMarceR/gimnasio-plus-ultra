package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;

public class SuscripcionRequest {

    private Integer idCliente;
    private Integer idPlanSuscripcion;
    private LocalDate fechaInicio; // opcional: si no viene, hoy

    public SuscripcionRequest() {
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdPlanSuscripcion() {
        return idPlanSuscripcion;
    }

    public void setIdPlanSuscripcion(Integer idPlanSuscripcion) {
        this.idPlanSuscripcion = idPlanSuscripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}
