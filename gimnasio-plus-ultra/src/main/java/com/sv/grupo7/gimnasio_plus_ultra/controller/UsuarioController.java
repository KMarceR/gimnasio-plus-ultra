package com.sv.grupo7.gimnasio_plus_ultra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sv.grupo7.gimnasio_plus_ultra.dto.UsuarioRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.UsuarioResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController extends CrudController<UsuarioRequest, UsuarioResponse> {

    public UsuarioController(UsuarioService usuarioService) {
        super(usuarioService);
    }
}
