package com.sv.grupo7.gimnasio_plus_ultra.dto;

public class ClienteRequest extends PersonaRequest {

    private int id_plansuscripcion;
    private int id_estado;
    private String notas;

    public ClienteRequest() {
        super();
    }

    // Getters y Setters
    public int getIdPlanSuscripcion() {
        return id_plansuscripcion;
    }

    public void setIdPlanSuscripcion(int id_plansuscripcion) {
        this.id_plansuscripcion = id_plansuscripcion;
    }

    public int getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
