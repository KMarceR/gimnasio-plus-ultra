package com.sv.grupo7.gimnasio_plus_ultra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import com.sv.grupo7.gimnasio_plus_ultra.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByCodigoQr(String codigoQr);

    // Bloquea la fila del cliente hasta que termine la transaccion
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cliente c WHERE c.idCliente = :id")
    Optional<Cliente> buscarParaActualizar(@Param("id") Integer id);
}
