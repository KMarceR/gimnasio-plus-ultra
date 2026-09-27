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

import com.sv.grupo7.gimnasio_plus_ultra.dto.EstadoRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.EstadoResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.EstadoService;

@RestController
@RequestMapping("/api/estados")
public class EstadoController {

    private final EstadoService estadoService;

    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }

    @GetMapping
    public List<EstadoResponse> listar() {
        return estadoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponse> obtenerPorId(@PathVariable Integer id) {
        EstadoResponse response = estadoService.obtenerPorId(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<EstadoResponse> crear(@RequestBody EstadoRequest request) {
        return ResponseEntity.ok(estadoService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoResponse> actualizar(@PathVariable Integer id, @RequestBody EstadoRequest request) {
        EstadoResponse response = estadoService.actualizar(id, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return estadoService.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
