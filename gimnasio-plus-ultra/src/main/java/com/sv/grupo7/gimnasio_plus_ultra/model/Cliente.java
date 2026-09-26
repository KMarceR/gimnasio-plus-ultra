
package com.sv.grupo7.gimnasio_plus_ultra.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "id_persona")
public class Cliente {

        @Column(name = "id_cliente")
    private int id_cliente;

    @ManyToOne
    @JoinColumn(name = "id_plansuscripcion") // Mapea la asociación al objeto completo Plan
    private PlanSuscripcion planSuscripcion;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Asistencia> asistencias = new ArrayList<>();

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creado_en;

    public Cliente() {
        super(); 
    }

    // Getters y Setters con encapsulamiento estricto
    public int getIdCliente() { return id_cliente; }
    public void setIdCliente(int id_cliente) { this.id_cliente = id_cliente; }

    public PlanSuscripcion getPlanSuscripcion() { return planSuscripcion; }
    public void setPlanSuscripcion(PlanSuscripcion planSuscripcion) { this.planSuscripcion = planSuscripcion; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public List<Asistencia> getAsistencias() { return asistencias; }
    public void setAsistencias(List<Asistencia> asistencias) { this.asistencias = asistencias; }

    public LocalDateTime getCreadoEn() { return creado_en; }
    public void setCreadoEn(LocalDateTime creado_en) { this.creado_en = creado_en; }
}
