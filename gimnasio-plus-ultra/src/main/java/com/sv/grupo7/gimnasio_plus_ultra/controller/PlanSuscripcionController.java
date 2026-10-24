package com.sv.grupo7.gimnasio_plus_ultra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sv.grupo7.gimnasio_plus_ultra.dto.PlanSuscripcionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.PlanSuscripcionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.PlanSuscripcionService;

@RestController
@RequestMapping("/api/planes-suscripcion")
public class PlanSuscripcionController extends CrudController<PlanSuscripcionRequest, PlanSuscripcionResponse> {

    public PlanSuscripcionController(PlanSuscripcionService planSuscripcionService) {
        super(planSuscripcionService);
    }
}
