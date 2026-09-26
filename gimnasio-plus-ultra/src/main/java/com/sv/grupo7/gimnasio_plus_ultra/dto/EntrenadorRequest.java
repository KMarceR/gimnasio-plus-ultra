package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;

public class EntrenadorRequest extends PersonaRequest {

    private int id_usuario;
    private int id_estado;
    private String especialidad;
    private double tarifa_por_sesion;

    public EntrenadorRequest() {
        super();
    }

    public EntrenadorRequest(String nombres, String apellidos, String genero, LocalDate fechaNacimiento,
            String direccion, String email, String telefono, String dui,
            int id_usuario, int id_estado, String especialidad, double tarifa_por_sesion) {
        super(nombres, apellidos, genero, fechaNacimiento, direccion, email, telefono, dui);
        this.id_usuario = id_usuario;
        this.id_estado = id_estado;
        this.especialidad = especialidad;
        this.tarifa_por_sesion = tarifa_por_sesion;
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
