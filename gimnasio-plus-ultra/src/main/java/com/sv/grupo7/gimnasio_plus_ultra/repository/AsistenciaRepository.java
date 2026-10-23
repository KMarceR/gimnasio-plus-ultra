package com.sv.grupo7.gimnasio_plus_ultra.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sv.grupo7.gimnasio_plus_ultra.model.Asistencia;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {

    // Entrada "abierta" de un cliente: hoy, estado Activo y todavia sin salida
    @Query("SELECT a FROM Asistencia a WHERE a.cliente.idCliente = :idCliente "
            + "AND a.fechaAsistencia = :fecha AND a.horaSalida IS NULL AND a.estado.nombreEstado = 'Activo'")
    List<Asistencia> buscarEntradasAbiertas(@Param("idCliente") int idCliente, @Param("fecha") LocalDate fecha);

    // Todos los que estan dentro del gimnasio en este momento
    @Query("SELECT a FROM Asistencia a WHERE a.fechaAsistencia = :fecha "
            + "AND a.horaSalida IS NULL AND a.estado.nombreEstado = 'Activo' ORDER BY a.horaSesion")
    List<Asistencia> buscarPresentes(@Param("fecha") LocalDate fecha);

    // Log del dia, del mas reciente al mas antiguo
    List<Asistencia> findByFechaAsistenciaOrderByHoraSesionDesc(LocalDate fecha);
}
