package com.sv.grupo7.gimnasio_plus_ultra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sv.grupo7.gimnasio_plus_ultra.dto.PersonaRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.PersonaResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.PersonaService;

@RestController
@RequestMapping("/api/personas")
public class PersonaController extends CrudController<PersonaRequest, PersonaResponse> {

    public PersonaController(PersonaService personaService) {
        super(personaService);
    }
}
