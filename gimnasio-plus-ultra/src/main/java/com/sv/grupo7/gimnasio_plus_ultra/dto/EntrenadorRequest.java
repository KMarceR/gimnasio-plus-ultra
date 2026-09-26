package com.sv.grupo7.gimnasio_plus_ultra.dto;

public class EntrenadorRequest extends PersonaRequest {

    private int id_usuario;
    private int id_estado;
    private String especialidad;
    private double tarifa_por_sesion;

    public EntrenadorRequest() {
        super();
    }

    // Getters y Setters
    public int getIdUsuario() {
        return id_usuario;
    }

    public void setIdUsuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public double getTarifaPorSesion() {
        return tarifa_por_sesion;
    }

    public void setTarifaPorSesion(double tarifa_por_sesion) {
        this.tarifa_por_sesion = tarifa_por_sesion;
    }
}
