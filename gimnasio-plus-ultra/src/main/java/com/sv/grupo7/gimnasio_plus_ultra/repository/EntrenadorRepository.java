package com.sv.grupo7.gimnasio_plus_ultra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sv.grupo7.gimnasio_plus_ultra.model.Entrenador;
import com.sv.grupo7.gimnasio_plus_ultra.model.Usuario;

import jakarta.persistence.LockModeType;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Integer> {

    boolean existsByUsuario(Usuario usuario);

    // SELECT ... FOR UPDATE: evita que dos sesiones se agenden a la vez con el mismo entrenador
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Entrenador e WHERE e.idEntrenador = :id")
    Optional<Entrenador> buscarParaActualizar(@Param("id") Integer id);
}
