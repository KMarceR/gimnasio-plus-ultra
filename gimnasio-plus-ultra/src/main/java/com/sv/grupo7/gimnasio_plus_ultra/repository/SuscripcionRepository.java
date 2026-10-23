package com.sv.grupo7.gimnasio_plus_ultra.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sv.grupo7.gimnasio_plus_ultra.model.Suscripcion;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Integer> {

    // Historial del cliente
    List<Suscripcion> findByClienteIdClienteOrderByFechaVencimientoDesc(int idCliente);

    // Suscripciones activas que cubren la fecha indicada
    @Query("SELECT s FROM Suscripcion s WHERE s.cliente.idCliente = :idCliente "
            + "AND s.estado.nombreEstado = 'Activo' AND :hoy BETWEEN s.fechaInicio AND s.fechaVencimiento")
    List<Suscripcion> buscarVigentes(@Param("idCliente") int idCliente, @Param("hoy") LocalDate hoy);

    // Última fecha de vencimiento activa
    @Query("SELECT MAX(s.fechaVencimiento) FROM Suscripcion s "
            + "WHERE s.cliente.idCliente = :idCliente AND s.estado.nombreEstado = 'Activo'")
    LocalDate ultimaFechaVencimiento(@Param("idCliente") int idCliente);
}