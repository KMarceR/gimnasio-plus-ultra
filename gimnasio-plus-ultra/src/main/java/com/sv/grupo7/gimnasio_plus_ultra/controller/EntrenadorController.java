package com.sv.grupo7.gimnasio_plus_ultra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sv.grupo7.gimnasio_plus_ultra.dto.EntrenadorRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.EntrenadorResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.EntrenadorService;

@RestController
@RequestMapping("/api/entrenadores")
public class EntrenadorController extends CrudController<EntrenadorRequest, EntrenadorResponse> {

    public EntrenadorController(EntrenadorService entrenadorService) {
        super(entrenadorService);
    }
}
