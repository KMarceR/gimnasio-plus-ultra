package com.sv.grupo7.gimnasio_plus_ultra.dto;


public class ClienteRequest extends PersonaRequest {

    private Integer id_plansuscripcion;
    private Integer id_estado;
    private String notas;

    public ClienteRequest() {
        super();
    }

    // Getters y Setters
    public Integer getIdPlanSuscripcion() {
        return id_plansuscripcion;
    }

    public void setIdPlanSuscripcion(Integer id_plansuscripcion) {
        this.id_plansuscripcion = id_plansuscripcion;
    }

    public Integer getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(Integer id_estado) {
        this.id_estado = id_estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
