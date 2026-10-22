package com.sv.grupo7.gimnasio_plus_ultra.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
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
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int idUsuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_persona", nullable = false, unique = true)
    private Persona persona = new Persona();

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    @Column(nullable = false)
    private String contrasena;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creadoEn;

    public Usuario() {}

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

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

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}
