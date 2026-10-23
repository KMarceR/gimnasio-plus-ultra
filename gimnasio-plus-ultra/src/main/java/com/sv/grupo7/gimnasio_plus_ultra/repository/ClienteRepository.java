package com.sv.grupo7.gimnasio_plus_ultra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sv.grupo7.gimnasio_plus_ultra.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByCodigoQr(String codigoQr);
}
