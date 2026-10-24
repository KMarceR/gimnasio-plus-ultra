package com.sv.grupo7.gimnasio_plus_ultra.dto;


public class EntrenadorRequest extends PersonaRequest {

    private Integer id_estado;
    private String contrasena;
    private String especialidad;
    private Double tarifa_por_sesion;

    public EntrenadorRequest() {
        super();
    }

    public Integer getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(Integer id_estado) {
        this.id_estado = id_estado;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Double getTarifaPorSesion() {
        return tarifa_por_sesion;
    }

    public void setTarifaPorSesion(Double tarifa_por_sesion) {
        this.tarifa_por_sesion = tarifa_por_sesion;
    }
}
