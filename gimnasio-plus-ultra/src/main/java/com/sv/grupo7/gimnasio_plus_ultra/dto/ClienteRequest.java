package com.sv.grupo7.gimnasio_plus_ultra.dto;

import java.time.LocalDate;

public class ClienteRequest extends PersonaRequest {

    private int id_plansuscripcion;
    private int id_estado;
    private String notas;

    public ClienteRequest() {
        super();
    }

    public ClienteRequest(String nombres, String apellidos, String genero, LocalDate fechaNacimiento,
            String direccion, String email, String telefono, String dui,
            int id_plansuscripcion, int id_estado, String notas) {
        super(nombres, apellidos, genero, fechaNacimiento, direccion, email, telefono, dui); // Invoca al padre
        this.id_plansuscripcion = id_plansuscripcion;
        this.id_estado = id_estado;
        this.notas = notas;
    }

    // Getters y Setters
    public int getIdPlanSuscripcion() {
        return id_plansuscripcion;
    }

    public void setIdPlanSuscripcion(int id_plansuscripcion) {
        this.id_plansuscripcion = id_plansuscripcion;
    }

    public int getIdEstado() {
        return id_estado;
    }

    public void setIdEstado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
