package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UsuarioResponse extends PersonaResponse {

    private int id_usuario;
    private String nombre_usuario;
    private String nombre_rol;
    private String nombre_estado;
    private LocalDateTime creado_en;

    public UsuarioResponse() {
        super();
    }

    public UsuarioResponse(int idPersona, String nombres, String apellidos, String genero,
            LocalDate fechaNacimiento, String direccion, String email,
            String telefono, String dui, LocalDateTime creadoEn,
            int id_usuario, String nombre_usuario, String nombre_rol, String nombre_estado) {
        super(idPersona, nombres, apellidos, genero, fechaNacimiento, direccion, email, telefono, dui, creadoEn);
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.nombre_rol = nombre_rol;
        this.nombre_estado = nombre_estado;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return id_usuario;
    }

    public void setIdUsuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombreUsuario() {
        return nombre_usuario;
    }

    public void setNombreUsuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getNombreRol() {
        return nombre_rol;
    }

    public void setNombreRol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }

    public String getNombreEstado() {
        return nombre_estado;
    }

    public void setNombreEstado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }

    public LocalDateTime getCreadoEn() {
        return creado_en;
    }

    public void setCreadoEn(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }
}
