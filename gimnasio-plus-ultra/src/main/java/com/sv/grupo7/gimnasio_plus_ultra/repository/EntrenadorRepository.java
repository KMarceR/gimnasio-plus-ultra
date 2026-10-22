package com.sv.grupo7.gimnasio_plus_ultra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sv.grupo7.gimnasio_plus_ultra.model.Entrenador;
import com.sv.grupo7.gimnasio_plus_ultra.model.Usuario;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Integer> {

    boolean existsByUsuario(Usuario usuario);

}
