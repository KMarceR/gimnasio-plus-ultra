package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SuscripcionResponse {
    private int idSuscripcion;
    private int idCliente;
    private String nombreCliente;
    private String nombrePlan;
    private String tipoPlan;
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;
    private double precioPagado;
    private String nombreEstado;
    private boolean vigente;
    private LocalDateTime creadoEn;

    public SuscripcionResponse() {
    }

    public SuscripcionResponse(int idSuscripcion, int idCliente, String nombreCliente, String nombrePlan,
            String tipoPlan, LocalDate fechaInicio, LocalDate fechaVencimiento, double precioPagado,
            String nombreEstado, boolean vigente, LocalDateTime creadoEn) {
        this.idSuscripcion = idSuscripcion;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.nombrePlan = nombrePlan;
        this.tipoPlan = tipoPlan;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.precioPagado = precioPagado;
        this.nombreEstado = nombreEstado;
        this.vigente = vigente;
        this.creadoEn = creadoEn;
    }

    public int getIdSuscripcion() {
        return idSuscripcion;
    }

    public void setIdSuscripcion(int idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombrePlan() {
        return nombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public String getTipoPlan() {
        return tipoPlan;
    }

    public void setTipoPlan(String tipoPlan) {
        this.tipoPlan = tipoPlan;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public double getPrecioPagado() {
        return precioPagado;
    }

    public void setPrecioPagado(double precioPagado) {
        this.precioPagado = precioPagado;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public boolean isVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}
