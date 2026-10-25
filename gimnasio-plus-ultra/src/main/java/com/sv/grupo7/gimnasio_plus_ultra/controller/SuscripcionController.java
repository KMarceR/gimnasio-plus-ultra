package com.sv.grupo7.gimnasio_plus_ultra.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sv.grupo7.gimnasio_plus_ultra.dto.SuscripcionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.SuscripcionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.dto.VistaCancelacionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.SuscripcionService;

@RestController
@RequestMapping("/api/suscripciones")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    public SuscripcionController(SuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

    @GetMapping
    public List<SuscripcionResponse> listar() {
        return suscripcionService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuscripcionResponse> obtenerPorId(@PathVariable Integer id) {
        SuscripcionResponse r = suscripcionService.obtenerPorId(id);
        return r != null ? ResponseEntity.ok(r) : ResponseEntity.notFound().build();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<SuscripcionResponse> listarPorCliente(@PathVariable Integer idCliente) {
        return suscripcionService.obtenerPorCliente(idCliente);
    }

    @PostMapping
    public ResponseEntity<SuscripcionResponse> vender(@RequestBody SuscripcionRequest request) {
        return ResponseEntity.ok(suscripcionService.vender(request));
    }

    @GetMapping("/{id}/cancelacion")
    public ResponseEntity<VistaCancelacionResponse> vistaCancelacion(@PathVariable Integer id) {
        VistaCancelacionResponse r = suscripcionService.vistaCancelacion(id);
        return r != null ? ResponseEntity.ok(r) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<SuscripcionResponse> cancelar(@PathVariable Integer id) {
        SuscripcionResponse r = suscripcionService.cancelar(id);
        return r != null ? ResponseEntity.ok(r) : ResponseEntity.notFound().build();
    }
}