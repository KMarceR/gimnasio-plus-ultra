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

import com.sv.grupo7.gimnasio_plus_ultra.dto.EntrenadorRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.EntrenadorResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.EntrenadorService;

@RestController
@RequestMapping("/api/entrenadores")
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    public EntrenadorController(EntrenadorService entrenadorService) {
        this.entrenadorService = entrenadorService;
    }

    @GetMapping
    public List<EntrenadorResponse> listar() {
        return entrenadorService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrenadorResponse> obtenerPorId(@PathVariable Integer id) {
        EntrenadorResponse response = entrenadorService.obtenerPorId(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<EntrenadorResponse> crear(@RequestBody EntrenadorRequest request) {
        return ResponseEntity.ok(entrenadorService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntrenadorResponse> actualizar(@PathVariable Integer id, @RequestBody EntrenadorRequest request) {
        EntrenadorResponse response = entrenadorService.actualizar(id, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return entrenadorService.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
