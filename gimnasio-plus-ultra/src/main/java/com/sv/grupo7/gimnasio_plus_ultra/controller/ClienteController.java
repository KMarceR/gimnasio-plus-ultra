package com.sv.grupo7.gimnasio_plus_ultra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sv.grupo7.gimnasio_plus_ultra.dto.ClienteRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.ClienteResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController extends CrudController<ClienteRequest, ClienteResponse> {

    public ClienteController(ClienteService clienteService) {
        super(clienteService);
    }
}