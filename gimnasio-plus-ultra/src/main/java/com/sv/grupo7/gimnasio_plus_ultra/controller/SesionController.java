package com.sv.grupo7.gimnasio_plus_ultra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sv.grupo7.gimnasio_plus_ultra.dto.SesionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.SesionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.SesionService;

@RestController
@RequestMapping("/api/sesiones")
public class SesionController extends CrudController<SesionRequest, SesionResponse> {

    public SesionController(SesionService sesionService) {
        super(sesionService);
    }
}
