package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;

public class CambioFechaSuscripcion {

    private int idSuscripcion;
    private String nombrePlan;
    private LocalDate fechaInicioActual;
    private LocalDate fechaVencimientoActual;
    private LocalDate nuevaFechaInicio;
    private LocalDate nuevaFechaVencimiento;

    public CambioFechaSuscripcion() {
    }

    public int getIdSuscripcion() {
        return idSuscripcion;
    }

    public void setIdSuscripcion(int idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }

    public String getNombrePlan() {
        return nombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public LocalDate getFechaInicioActual() {
        return fechaInicioActual;
    }

    public void setFechaInicioActual(LocalDate fechaInicioActual) {
        this.fechaInicioActual = fechaInicioActual;
    }

    public LocalDate getFechaVencimientoActual() {
        return fechaVencimientoActual;
    }

    public void setFechaVencimientoActual(LocalDate fechaVencimientoActual) {
        this.fechaVencimientoActual = fechaVencimientoActual;
    }

    public LocalDate getNuevaFechaInicio() {
        return nuevaFechaInicio;
    }

    public void setNuevaFechaInicio(LocalDate nuevaFechaInicio) {
        this.nuevaFechaInicio = nuevaFechaInicio;
    }

    public LocalDate getNuevaFechaVencimiento() {
        return nuevaFechaVencimiento;
    }

    public void setNuevaFechaVencimiento(LocalDate nuevaFechaVencimiento) {
        this.nuevaFechaVencimiento = nuevaFechaVencimiento;
    }
}
