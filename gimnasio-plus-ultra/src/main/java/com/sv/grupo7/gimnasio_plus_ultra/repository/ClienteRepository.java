package com.sv.grupo7.gimnasio_plus_ultra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sv.grupo7.gimnasio_plus_ultra.model.Persona;

public interface ClienteRepository extends JpaRepository<Persona, Integer>  {

}
