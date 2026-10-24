package com.sv.grupo7.gimnasio_plus_ultra.dto;


public class UsuarioRequest extends PersonaRequest {

    private Integer id_rol;
    private Integer id_estado;
    private String contrasena;

    public UsuarioRequest() {
        super();
    }

    // Getters y Setters
    public Integer getIdRol() {
        return id_rol;
    }

    public void setIdRol(Integer id_rol) {
        this.id_rol = id_rol;
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
}
