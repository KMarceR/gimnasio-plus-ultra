package com.sv.grupo7.gimnasio_plus_ultra.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "entrenadores")
@PrimaryKeyJoinColumn(name = "id_persona")
public class Entrenador {

    @Column(name = "id_entrenador")
    private int id_entrenador;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    private String especialidad;

    @Column(name = "tarifa_por_sesion", nullable = false)
    private double tarifa_por_sesion;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creado_en;

    public Entrenador() {
        super(); 
    }

    // Getters y Setters con encapsulamiento estricto
    public int getIdEntrenador() { return id_entrenador; }
    public void setIdEntrenador(int id_entrenador) { this.id_entrenador = id_entrenador; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public double getTarifaPorSesion() { return tarifa_por_sesion; }
    public void setTarifaPorSesion(double tarifa_por_sesion) { this.tarifa_por_sesion = tarifa_por_sesion; }

    public LocalDateTime getCreadoEn() { return creado_en; }
    public void setCreadoEn(LocalDateTime creado_en) { this.creado_en = creado_en; }
}
