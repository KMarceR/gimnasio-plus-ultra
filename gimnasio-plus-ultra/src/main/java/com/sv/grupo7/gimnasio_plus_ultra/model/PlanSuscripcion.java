package com.sv.grupo7.gimnasio_plus_ultra.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_suscripcion")
public class PlanSuscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plansuscripcion")
    private int id_plansuscripcion;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado; // Asociación orientada a objetos

    @Column(name = "nombre_suscripcion", nullable = false, unique = true, length = 100)
    private String nombre_suscripcion;

    @Column(columnDefinition = "TEXT")
    private String detalles;

    @Column(nullable = false)
    private double precio; // Tipo primitivo double coincidente con tu diseño

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creado_en;

    public PlanSuscripcion() {}

    // Getters y Setters
    public int getIdPlanSuscripcion() { return id_plansuscripcion; }
    public void setIdPlanSuscripcion(int id_plansuscripcion) { this.id_plansuscripcion = id_plansuscripcion; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getNombreSuscripcion() { return nombre_suscripcion; }
    public void setNombreSuscripcion(String nombre_suscripcion) { this.nombre_suscripcion = nombre_suscripcion; }

    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public LocalDateTime getCreadoEn() { return creado_en; }
    public void setCreadoEn(LocalDateTime creado_en) { this.creado_en = creado_en; }
}

