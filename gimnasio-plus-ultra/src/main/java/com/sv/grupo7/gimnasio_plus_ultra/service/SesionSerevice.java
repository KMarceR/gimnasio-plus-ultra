package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sv.grupo7.gimnasio_plus_ultra.dto.SesionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.SesionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Cliente;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.Sesion;
import com.sv.grupo7.gimnasio_plus_ultra.repository.ClienteRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EntrenadorRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.SesionRepository;

@Service
public class SesionSerevice {

    private final SesionRepository sesionRepository;
    private final ClienteRepository clienteRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final EstadoRepository estadoRepository;

    public SesionSerevice(SesionRepository sesionRepository, ClienteRepository clienteRepository,
            EntrenadorRepository entrenadorRepository, EstadoRepository estadoRepository) {
        this.sesionRepository = sesionRepository;
        this.clienteRepository = clienteRepository;
        this.entrenadorRepository = entrenadorRepository;
        this.estadoRepository = estadoRepository;
    }

    public SesionResponse crear(SesionRequest request) {
        Sesion sesion = new Sesion();
        sesion.setFechaSesion(request.getFechaSesion());
        sesion.setHoraInicio(request.getHoraInicio());
        sesion.setHoraFin(request.getHoraFin());

        Cliente cliente = clienteRepository.findById(Integer.valueOf(request.getIdCliente()))
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        sesion.setCliente(cliente);

        Entrenador entrenador = entrenadorRepository.findById(Integer.valueOf(request.getIdEntrenador()))
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));
        sesion.setEntrenador(entrenador);

        Estado estado = estadoRepository.findById(Integer.valueOf(request.getIdEstado()))
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        sesion.setEstado(estado);

        Sesion nuevaSesion = sesionRepository.save(sesion);
        return convertirAResponse(nuevaSesion);
    }

    public List<SesionResponse> obtenerTodos() {
        return sesionRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    private SesionResponse convertirAResponse(Sesion sesion) {
        SesionResponse response = new SesionResponse();
        response.setIdSesion(sesion.getIdSesion());
        response.setFechaSesion(sesion.getFechaSesion());
        response.setHoraInicio(sesion.getHoraInicio());
        response.setHoraFin(sesion.getHoraFin());
        response.setCreadoEn(sesion.getCreadoEn());

        if (sesion.getCliente() != null) {
            response.setNombreCliente(sesion.getCliente().getNombres() + " " + sesion.getCliente().getApellidos());
        }
        if (sesion.getEntrenador() != null) {
            response.setNombreEntrenador(
                    sesion.getEntrenador().getNombres() + " " + sesion.getEntrenador().getApellidos());
        }
        if (sesion.getEstado() != null) {
            response.setNombreEstado(sesion.getEstado().getNombreEstado());
        }
        return response;
    }
}
