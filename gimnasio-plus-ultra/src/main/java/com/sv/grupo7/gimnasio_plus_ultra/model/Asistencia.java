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
@Table(name = "asistencia")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia")
    private int id_asistencia;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @Column(name = "fecha_asistencia", nullable = false)
    private LocalDate fecha_asistencia;

    @Column(name = "hora_sesion", nullable = false)
    private LocalTime hora_sesion;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creado_en;

    public Asistencia() {}

    // Getters y Setters con encapsulamiento estricto
    public int getIdAsistencia() { return id_asistencia; }
    public void setIdAsistencia(int id_asistencia) { this.id_asistencia = id_asistencia; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public LocalDate getFechaAsistencia() { return fecha_asistencia; }
    public void setFechaAsistencia(LocalDate fecha_asistencia) { this.fecha_asistencia = fecha_asistencia; }

    public LocalTime getHoraSesion() { return hora_sesion; }
    public void setHoraSesion(LocalTime hora_sesion) { this.hora_sesion = hora_sesion; }

    public LocalDateTime getCreadoEn() { return creado_en; }
    public void setCreadoEn(LocalDateTime creado_en) { this.creado_en = creado_en; }
}
