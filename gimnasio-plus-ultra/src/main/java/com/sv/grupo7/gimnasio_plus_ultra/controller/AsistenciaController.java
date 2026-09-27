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

import com.sv.grupo7.gimnasio_plus_ultra.dto.AsistenciaRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.AsistenciaResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.AsistenciaService;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @GetMapping
    public List<AsistenciaResponse> listar() {
        return asistenciaService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaResponse> obtenerPorId(@PathVariable Integer id) {
        AsistenciaResponse response = asistenciaService.obtenerPorId(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<AsistenciaResponse> crear(@RequestBody AsistenciaRequest request) {
        return ResponseEntity.ok(asistenciaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaResponse> actualizar(@PathVariable Integer id, @RequestBody AsistenciaRequest request) {
        AsistenciaResponse response = asistenciaService.actualizar(id, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return asistenciaService.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
