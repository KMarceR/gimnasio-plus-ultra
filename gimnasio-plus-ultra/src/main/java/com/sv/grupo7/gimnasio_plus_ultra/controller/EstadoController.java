package com.sv.grupo7.gimnasio_plus_ultra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sv.grupo7.gimnasio_plus_ultra.dto.EstadoRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.EstadoResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.EstadoService;

@RestController
@RequestMapping("/api/estados")
public class EstadoController extends CrudController<EstadoRequest, EstadoResponse> {

    public EstadoController(EstadoService estadoService) {
        super(estadoService);
    }
}
