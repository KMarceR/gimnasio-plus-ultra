package com.sv.grupo7.gimnasio_plus_ultra.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sv.grupo7.gimnasio_plus_ultra.dto.AccesoQrRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.AccesoQrResponse;
import com.sv.grupo7.gimnasio_plus_ultra.dto.AsistenciaRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.AsistenciaResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.AsistenciaService;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController extends CrudController<AsistenciaRequest, AsistenciaResponse> {

    // Referencia concreta para los endpoints propios del acceso QR
    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        super(asistenciaService);
        this.asistenciaService = asistenciaService;
    }

    @PostMapping("/qr")
    public ResponseEntity<AccesoQrResponse> registrarAccesoQr(@RequestBody AccesoQrRequest request) {
        return ResponseEntity.ok(asistenciaService.registrarAccesoQr(request.getCodigoQr()));
    }

    @GetMapping("/presentes")
    public List<AsistenciaResponse> presentes() {
        return asistenciaService.obtenerPresentes();
    }

    @GetMapping("/hoy")
    public List<AsistenciaResponse> deHoy() {
        return asistenciaService.obtenerDeHoy();
    }
}