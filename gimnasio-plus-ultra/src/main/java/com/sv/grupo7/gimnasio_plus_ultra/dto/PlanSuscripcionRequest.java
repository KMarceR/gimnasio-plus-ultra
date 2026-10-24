package com.sv.grupo7.gimnasio_plus_ultra.dto;

public class PlanSuscripcionRequest {

    private Integer id_estado;
    private String nombre_suscripcion;
    private String detalles;
    private double precio;
    private String tipoPlan;

    public PlanSuscripcionRequest() {
    }

    public Integer getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(Integer id_estado) {
        this.id_estado = id_estado;
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

    public String getTipoPlan() {
        return tipoPlan;
    }

    public void setTipoPlan(String tipoPlan) {
        this.tipoPlan = tipoPlan;
    }
}
