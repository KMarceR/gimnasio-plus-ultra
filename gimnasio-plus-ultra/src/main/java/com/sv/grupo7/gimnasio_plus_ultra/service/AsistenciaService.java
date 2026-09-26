package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sv.grupo7.gimnasio_plus_ultra.dto.AsistenciaRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.AsistenciaResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Asistencia;
import com.sv.grupo7.gimnasio_plus_ultra.model.Cliente;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.repository.AsistenciaRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.ClienteRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;

@Service
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final ClienteRepository clienteRepository;
    private final EstadoRepository estadoRepository;

    public AsistenciaService(AsistenciaRepository asistenciaRepository, ClienteRepository clienteRepository, EstadoRepository estadoRepository) {
        this.asistenciaRepository = asistenciaRepository;
        this.clienteRepository = clienteRepository;
        this.estadoRepository = estadoRepository;
    }

    public AsistenciaResponse crear(AsistenciaRequest request) {
        Asistencia asistencia = new Asistencia();
        asistencia.setFechaAsistencia(request.getFechaAsistencia());
        asistencia.setHoraSesion(request.getHoraSesion());

        Cliente cliente = clienteRepository.findById(Integer.valueOf(request.getIdCliente()))
                .orElseThrow(() -> new RuntimeException("Cliente no registrado"));
        asistencia.setCliente(cliente);

        Estado estado = estadoRepository.findById(Integer.valueOf(request.getIdEstado()))
                .orElseThrow(() -> new RuntimeException("Estado inválido"));
        asistencia.setEstado(estado);

        Asistencia nuevaAsistencia = asistenciaRepository.save(asistencia);
        return convertirAResponse(nuevaAsistencia);
    }

    public List<AsistenciaResponse> obtenerTodos() {
        return asistenciaRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    private AsistenciaResponse convertirAResponse(Asistencia asistencia) {
        AsistenciaResponse response = new AsistenciaResponse();
        response.setIdAsistencia(asistencia.getIdAsistencia());
        response.setFechaAsistencia(asistencia.getFechaAsistencia());
        response.setHoraSesion(asistencia.getHoraSesion());
        response.setCreadoEn(asistencia.getCreadoEn());

        if (asistencia.getCliente() != null) {
            String completo = asistencia.getCliente().getNombres() + " " + asistencia.getCliente().getApellidos();
            response.setNombreCliente(completo);
        }
        if (asistencia.getEstado() != null) {
            response.setNombreEstado(asistencia.getEstado().getNombreEstado());
        }
        return response;
    }
}
