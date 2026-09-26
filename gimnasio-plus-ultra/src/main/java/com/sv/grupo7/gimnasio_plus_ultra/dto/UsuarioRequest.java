package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;

public class UsuarioRequest extends PersonaRequest {

    private int id_rol;
    private int id_estado;
    private String nombre_usuario;
    private String contrasena;

    public UsuarioRequest() {
        super();
    }

    public UsuarioRequest(String nombres, String apellidos, String genero, LocalDate fechaNacimiento,
            String direccion, String email, String telefono, String dui,
            int id_rol, int id_estado, String nombre_usuario, String contrasena) {
        super(nombres, apellidos, genero, fechaNacimiento, direccion, email, telefono, dui);
        this.id_rol = id_rol;
        this.id_estado = id_estado;
        this.nombre_usuario = nombre_usuario;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public int getIdRol() {
        return id_rol;
    }

    public void setIdRol(int id_rol) {
        this.id_rol = id_rol;
    }

    public int getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getNombreUsuario() {
        return nombre_usuario;
    }

    public void setNombreUsuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
