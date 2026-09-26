package com.sv.grupo7.gimnasio_plus_ultra.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sesion")
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private int id_sesion;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_entrenador", nullable = false)
    private Entrenador entrenador;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @Column(name = "fecha_sesion", nullable = false)
    private LocalDate fecha_sesion;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime hora_inicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime hora_fin;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creado_en;

    public Sesion() {}

    // Getters y Setters
    public int getIdSesion() { return id_sesion; }
    public void setIdSesion(int id_sesion) { this.id_sesion = id_sesion; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Entrenador getEntrenador() { return entrenador; }
    public void setEntrenador(Entrenador entrenador) { this.entrenador = entrenador; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public LocalDate getFechaSesion() { return fecha_sesion; }
    public void setFechaSesion(LocalDate fecha_sesion) { this.fecha_sesion = fecha_sesion; }

    public LocalTime getHoraInicio() { return hora_inicio; }
    public void setHoraInicio(LocalTime hora_inicio) { this.hora_inicio = hora_inicio; }

    public LocalTime getHoraFin() { return hora_fin; }
    public void setHoraFin(LocalTime hora_fin) { this.hora_fin = hora_fin; }

    public LocalDateTime getCreadoEn() { return creado_en; }
    public void setCreadoEn(LocalDateTime creado_en) { this.creado_en = creado_en; }
}
