package com.sv.grupo7.gimnasio_plus_ultra.dto;

public class PlanSuscripcionRequest {

    private int id_estado;
    private String nombre_suscripcion;
    private String detalles;
    private double precio;

    public PlanSuscripcionRequest() {
    }

    public PlanSuscripcionRequest(int id_estado, String nombre_suscripcion, String detalles, double precio) {
        this.id_estado = id_estado;
        this.nombre_suscripcion = nombre_suscripcion;
        this.detalles = detalles;
        this.precio = precio;
    }

    public int getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(int id_estado) {
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
}
