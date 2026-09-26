package com.sv.grupo7.gimnasio_plus_ultra.model;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity 
@Table(name = "usuarios")
@PrimaryKeyJoinColumn(name = "id_persona") 
public class Usuario extends Persona { 

    @Id 
    @Column(name = "id_usuario")
    private int id_usuario;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol id_rol; 

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado id_estado;

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombre_usuario;
    
    @Column(nullable = false)
    private String contrasena;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creado_en;

    public Usuario() {
        super();
    }

    // Getters y Setters específicos de Usuario
    public int getIdUsuario() { return id_usuario; }
    public void setIdUsuario(int id_usuario) { this.id_usuario = id_usuario; }

    public Rol getIdRol() { return id_rol; }
    public void setIdRol(Rol id_rol) { this.id_rol = id_rol; }

    public Estado getIdEstado() { return id_estado; }
    public void setIdEstado(Estado id_estado) { this.id_estado = id_estado; }

    public String getNombreUsuario() { return nombre_usuario; }
    public void setNombreUsuario(String nombre_usuario) { this.nombre_usuario = nombre_usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public LocalDateTime getCreadoEn() { return creado_en; }
    public void setCreadoEn(LocalDateTime creado_en) { this.creado_en = creado_en; }
}