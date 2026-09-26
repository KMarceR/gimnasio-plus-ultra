package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDateTime;

public class EntrenadorResponse extends PersonaResponse {

    private int id_entrenador;
    private String nombre_usuario;
    private String nombre_estado;
    private String especialidad;
    private double tarifa_por_sesion;
    private LocalDateTime creado_en;

    public EntrenadorResponse() {
        super();
    }

    public int getIdEntrenador() {
        return id_entrenador;
    }

    public void setIdEntrenador(int id_entrenador) {
        this.id_entrenador = id_entrenador;
    }

    public String getNombreUsuario() {
        return nombre_usuario;
    }

    public void setNombreUsuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getNombreEstado() {
        return nombre_estado;
    }

    public void setNombreEstado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
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

    public LocalDateTime getCreadoEn() {
        return creado_en;
    }

    public void setCreadoEn(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }
}
