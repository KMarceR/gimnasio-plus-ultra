package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sv.grupo7.gimnasio_plus_ultra.dto.SesionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.SesionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Cliente;
import com.sv.grupo7.gimnasio_plus_ultra.model.Entrenador;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.Sesion;
import com.sv.grupo7.gimnasio_plus_ultra.repository.ClienteRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EntrenadorRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.SesionRepository;

@Service
public class SesionService {

    private final SesionRepository sesionRepository;
    private final ClienteRepository clienteRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final EstadoRepository estadoRepository;

    public SesionService(SesionRepository sesionRepository, ClienteRepository clienteRepository,
            EntrenadorRepository entrenadorRepository, EstadoRepository estadoRepository) {
        this.sesionRepository = sesionRepository;
        this.clienteRepository = clienteRepository;
        this.entrenadorRepository = entrenadorRepository;
        this.estadoRepository = estadoRepository;
    }

    public SesionResponse crear(SesionRequest request) {
        Sesion sesion = new Sesion();
        aplicarDatos(sesion, request);

        Sesion nuevaSesion = sesionRepository.save(sesion);
        return convertirAResponse(nuevaSesion);
    }

    public List<SesionResponse> obtenerTodos() {
        return sesionRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    public SesionResponse obtenerPorId(Integer id) {
        return sesionRepository.findById(id)
                .map(this::convertirAResponse)
                .orElse(null);
    }

    public SesionResponse actualizar(Integer id, SesionRequest request) {
        Sesion sesion = sesionRepository.findById(id).orElse(null);
        if (sesion == null) {
            return null;
        }
        aplicarDatos(sesion, request);
        return convertirAResponse(sesionRepository.save(sesion));
    }

    public boolean eliminar(Integer id) {
        if (!sesionRepository.existsById(id)) {
            return false;
        }
        sesionRepository.deleteById(id);
        return true;
    }

    private void aplicarDatos(Sesion sesion, SesionRequest request) {
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
