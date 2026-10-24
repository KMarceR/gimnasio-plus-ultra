package com.sv.grupo7.gimnasio_plus_ultra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sv.grupo7.gimnasio_plus_ultra.dto.RolRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.RolResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.RolService;

@RestController
@RequestMapping("/api/roles")
public class RolController extends CrudController<RolRequest, RolResponse> {

    public RolController(RolService rolService) {
        super(rolService);
    }
}
