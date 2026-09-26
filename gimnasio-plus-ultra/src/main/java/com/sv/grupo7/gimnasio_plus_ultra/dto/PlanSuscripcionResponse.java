package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDateTime;

public class PlanSuscripcionResponse {

    private int id_plansuscripcion;
    private String nombre_estado;
    private String nombre_suscripcion;
    private String detalles;
    private double precio;
    private LocalDateTime creado_en;

    public PlanSuscripcionResponse() {
    }

    public int getIdPlanSuscripcion() {
        return id_plansuscripcion;
    }

    public void setIdPlanSuscripcion(int id_plansuscripcion) {
        this.id_plansuscripcion = id_plansuscripcion;
    }

    public String getNombreEstado() {
        return nombre_estado;
    }

    public void setNombreEstado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }

    public String getNombreSuscripcion() {
        return nombre_suscripcion;
    }

    public void setNombreSuscripcion(String nombre_suscripcion) {
        this.nombre_suscripcion = nombre_suscripcion;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDateTime getCreadoEn() {
        return creado_en;
    }

    public void setCreadoEn(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }

}
