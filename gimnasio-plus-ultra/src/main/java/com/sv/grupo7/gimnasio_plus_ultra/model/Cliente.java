package com.sv.grupo7.gimnasio_plus_ultra.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private int idCliente;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_persona", nullable = false, unique = true)
    private Persona persona = new Persona();

    @ManyToOne
    @JoinColumn(name = "id_plansuscripcion")
    private PlanSuscripcion planSuscripcion;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Asistencia> asistencias = new ArrayList<>();

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creadoEn;

    public Cliente() {}

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    // Delegados hacia Persona (datos personales compartidos)
    public int getIdPersona() { return persona.getIdPersona(); }
    public String getNombres() { return persona.getNombres(); }
    public void setNombres(String nombres) { persona.setNombres(nombres); }
    public String getApellidos() { return persona.getApellidos(); }
    public void setApellidos(String apellidos) { persona.setApellidos(apellidos); }
    public String getGenero() { return persona.getGenero(); }
    public void setGenero(String genero) { persona.setGenero(genero); }
    public LocalDate getFechaNacimiento() { return persona.getFechaNacimiento(); }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { persona.setFechaNacimiento(fechaNacimiento); }
    public String getDireccion() { return persona.getDireccion(); }
    public void setDireccion(String direccion) { persona.setDireccion(direccion); }
    public String getEmail() { return persona.getEmail(); }
    public void setEmail(String email) { persona.setEmail(email); }
    public String getTelefono() { return persona.getTelefono(); }
    public void setTelefono(String telefono) { persona.setTelefono(telefono); }
    public String getDui() { return persona.getDui(); }
    public void setDui(String dui) { persona.setDui(dui); }

    public PlanSuscripcion getPlanSuscripcion() { return planSuscripcion; }
    public void setPlanSuscripcion(PlanSuscripcion planSuscripcion) { this.planSuscripcion = planSuscripcion; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public List<Asistencia> getAsistencias() { return asistencias; }
    public void setAsistencias(List<Asistencia> asistencias) { this.asistencias = asistencias; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}
