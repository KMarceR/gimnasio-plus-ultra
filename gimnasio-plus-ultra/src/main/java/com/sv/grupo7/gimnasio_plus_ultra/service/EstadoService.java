package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sv.grupo7.gimnasio_plus_ultra.dto.EstadoRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.EstadoResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;

@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;

    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    public EstadoResponse crear(EstadoRequest request) {
        Estado estado = new Estado();
        estado.setNombreEstado(request.getNombreEstado());
        Estado nuevoEstado = estadoRepository.save(estado);
        return convertirAResponse(nuevoEstado);
    }

    public List<EstadoResponse> obtenerTodos() {
        return estadoRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    private EstadoResponse convertirAResponse(Estado estado) {
        EstadoResponse response = new EstadoResponse();
        response.setIdEstado(estado.getIdEstado());
        response.setNombreEstado(estado.getNombreEstado());
        response.setCreadoEn(estado.getCreadoEn());
        return response;
    }
}