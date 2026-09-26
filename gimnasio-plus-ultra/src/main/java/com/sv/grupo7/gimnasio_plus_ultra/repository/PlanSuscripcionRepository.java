package com.sv.grupo7.gimnasio_plus_ultra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sv.grupo7.gimnasio_plus_ultra.model.PlanSuscripcion;

@Repository
public interface PlanSuscripcionRepository extends JpaRepository<PlanSuscripcion, Integer> {

}
