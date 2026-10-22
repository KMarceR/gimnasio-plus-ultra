package com.sv.grupo7.gimnasio_plus_ultra.dto;

public class LoginRequest {
    private String nombreUsuario; // es el DUI
    private String contrasena;

    public LoginRequest() {
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
