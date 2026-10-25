package com.sv.grupo7.gimnasio_plus_ultra.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sv.grupo7.gimnasio_plus_ultra.model.Sesion;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Integer> {

    // Sesiones no canceladas del entrenador o del cliente que se cruzan con el horario indicado
    @Query("SELECT s FROM Sesion s WHERE s.fechaSesion = :fecha AND s.estado.nombreEstado <> 'Cancelada' "
            + "AND s.idSesion <> :idExcluir AND s.horaInicio < :fin AND s.horaFin > :inicio "
            + "AND (s.entrenador.idEntrenador = :idEntrenador OR s.cliente.idCliente = :idCliente)")
    List<Sesion> buscarChoques(@Param("fecha") LocalDate fecha, @Param("inicio") LocalTime inicio,
            @Param("fin") LocalTime fin, @Param("idEntrenador") int idEntrenador,
            @Param("idCliente") int idCliente, @Param("idExcluir") int idExcluir);
}
