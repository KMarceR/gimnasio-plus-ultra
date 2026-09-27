package com.sv.grupo7.gimnasio_plus_ultra.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sv.grupo7.gimnasio_plus_ultra.dto.PersonaRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.PersonaResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.PersonaService;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public List<PersonaResponse> listar() {
        return personaService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaResponse> obtenerPorId(@PathVariable Integer id) {
        PersonaResponse response = personaService.obtenerPorId(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PersonaResponse> crear(@RequestBody PersonaRequest request) {
        return ResponseEntity.ok(personaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaResponse> actualizar(@PathVariable Integer id, @RequestBody PersonaRequest request) {
        PersonaResponse response = personaService.actualizar(id, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return personaService.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
