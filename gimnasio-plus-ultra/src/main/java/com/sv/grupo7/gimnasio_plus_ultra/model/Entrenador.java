package com.sv.grupo7.gimnasio_plus_ultra.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "entrenadores")
public class Entrenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrenador")
    private int idEntrenador;

    @OneToOne
    @JoinColumn(name = "id_persona", nullable = false, unique = true)
    private Persona persona = new Persona();

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    private String especialidad;

    @Column(name = "tarifa_por_sesion", nullable = false)
    private double tarifaPorSesion;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creadoEn;

    public Entrenador() {}

    public int getIdEntrenador() { return idEntrenador; }
    public void setIdEntrenador(int idEntrenador) { this.idEntrenador = idEntrenador; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    // Delegados hacia Persona
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

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public double getTarifaPorSesion() { return tarifaPorSesion; }
    public void setTarifaPorSesion(double tarifaPorSesion) { this.tarifaPorSesion = tarifaPorSesion; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}
