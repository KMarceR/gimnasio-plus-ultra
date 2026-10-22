package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;

public class EntrenadorRequest extends PersonaRequest {

    private Integer id_estado;
    private String contrasena;
    private String especialidad;
    private Double tarifa_por_sesion;

    public EntrenadorRequest() {
        super();
    }

    public EntrenadorRequest(String nombres, String apellidos, String genero, LocalDate fechaNacimiento,
            String direccion, String email, String telefono, String dui,
            Integer id_estado, String contrasena, String especialidad, Double tarifa_por_sesion) {
        super(nombres, apellidos, genero, fechaNacimiento, direccion, email, telefono, dui);
        this.id_estado = id_estado;
        this.contrasena = contrasena;
        this.especialidad = especialidad;
        this.tarifa_por_sesion = tarifa_por_sesion;
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
