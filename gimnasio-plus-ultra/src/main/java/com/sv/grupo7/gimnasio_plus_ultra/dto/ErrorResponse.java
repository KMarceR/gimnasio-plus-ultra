package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDateTime;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private int estado;
    private String error;
    private String mensaje;
    private String ruta;

    public ErrorResponse(LocalDateTime timestamp, int estado, String error, String mensaje, String ruta) {
        this.timestamp = timestamp;
        this.estado = estado;
        this.error = error;
        this.mensaje = mensaje;
        this.ruta = ruta;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getEstado() {
        return estado;
    }

    public String getError() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getRuta() {
        return ruta;
    }
}