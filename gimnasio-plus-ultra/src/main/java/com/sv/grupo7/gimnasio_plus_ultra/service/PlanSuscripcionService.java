package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sv.grupo7.gimnasio_plus_ultra.dto.PlanSuscripcionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.PlanSuscripcionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.PlanSuscripcion;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.PlanSuscripcionRepository;

@Service
public class PlanSuscripcionService {

    private final PlanSuscripcionRepository planSuscripcionRepository;
    private final EstadoRepository estadoRepository;

    public PlanSuscripcionService(PlanSuscripcionRepository planSuscripcionRepository, EstadoRepository estadoRepository) {
        this.planSuscripcionRepository = planSuscripcionRepository;
        this.estadoRepository = estadoRepository;
    }

    public PlanSuscripcionResponse crear(PlanSuscripcionRequest request) {
        PlanSuscripcion plan = new PlanSuscripcion();
        plan.setNombreSuscripcion(request.getNombreSuscripcion());
        plan.setDetalles(request.getDetalles());
        plan.setPrecio(request.getPrecio());

        Estado estado = estadoRepository.findById(Integer.valueOf(request.getIdEstado()))
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        plan.setEstado(estado);

        PlanSuscripcion nuevoPlan = planSuscripcionRepository.save(plan);
        return convertirAResponse(nuevoPlan);
    }

    public List<PlanSuscripcionResponse> obtenerTodos() {
        return planSuscripcionRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    private PlanSuscripcionResponse convertirAResponse(PlanSuscripcion plan) {
        PlanSuscripcionResponse response = new PlanSuscripcionResponse();
        response.setIdPlanSuscripcion(plan.getIdPlanSuscripcion());
        response.setNombreSuscripcion(plan.getNombreSuscripcion());
        response.setDetalles(plan.getDetalles());
        response.setPrecio(plan.getPrecio());
        response.setCreadoEn(plan.getCreadoEn());
        
        if (plan.getEstado() != null) {
            response.setNombreEstado(plan.getEstado().getNombreEstado());
        }
        return response;
    }
}
