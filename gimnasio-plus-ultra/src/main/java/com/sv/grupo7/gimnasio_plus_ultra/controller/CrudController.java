package com.sv.grupo7.gimnasio_plus_ultra.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sv.grupo7.gimnasio_plus_ultra.service.CrudService;

// Endpoints CRUD comunes. Cada controller concreto define su ruta con @RequestMapping
public abstract class CrudController<REQ, RES> {
protected final CrudService<REQ, RES> servicio;

    protected CrudController(CrudService<REQ, RES> servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<RES> listar() {
        return servicio.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RES> obtenerPorId(@PathVariable Integer id) {
        RES response = servicio.obtenerPorId(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<RES> crear(@RequestBody REQ request) {
        return ResponseEntity.ok(servicio.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RES> actualizar(@PathVariable Integer id, @RequestBody REQ request) {
        RES response = servicio.actualizar(id, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return servicio.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
