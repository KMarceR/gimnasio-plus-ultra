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

import com.sv.grupo7.gimnasio_plus_ultra.dto.PlanSuscripcionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.PlanSuscripcionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.PlanSuscripcionService;

@RestController
@RequestMapping("/api/planes-suscripcion")
public class PlanSuscripcionController {

    private final PlanSuscripcionService planSuscripcionService;

    public PlanSuscripcionController(PlanSuscripcionService planSuscripcionService) {
        this.planSuscripcionService = planSuscripcionService;
    }

    @GetMapping
    public List<PlanSuscripcionResponse> listar() {
        return planSuscripcionService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanSuscripcionResponse> obtenerPorId(@PathVariable Integer id) {
        PlanSuscripcionResponse response = planSuscripcionService.obtenerPorId(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PlanSuscripcionResponse> crear(@RequestBody PlanSuscripcionRequest request) {
        return ResponseEntity.ok(planSuscripcionService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanSuscripcionResponse> actualizar(@PathVariable Integer id, @RequestBody PlanSuscripcionRequest request) {
        PlanSuscripcionResponse response = planSuscripcionService.actualizar(id, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return planSuscripcionService.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
