package com.sv.grupo7.gimnasio_plus_ultra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sv.grupo7.gimnasio_plus_ultra.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("SELECT u FROM Usuario u WHERE u.nombre_usuario = :nombreUsuario")
    Optional<Usuario> buscarPorNombreUsuario(@Param("nombreUsuario") String nombreUsuario);
}
