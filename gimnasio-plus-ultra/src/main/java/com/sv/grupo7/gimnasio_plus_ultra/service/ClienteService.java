package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sv.grupo7.gimnasio_plus_ultra.dto.ClienteRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.ClienteResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Cliente;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.PlanSuscripcion;
import com.sv.grupo7.gimnasio_plus_ultra.repository.ClienteRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.PlanSuscripcionRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PlanSuscripcionRepository planSuscripcionRepository;
    private final EstadoRepository estadoRepository;

    public ClienteService(ClienteRepository clienteRepository,
            PlanSuscripcionRepository planSuscripcionRepository,
            EstadoRepository estadoRepository) {
        this.clienteRepository = clienteRepository;
        this.planSuscripcionRepository = planSuscripcionRepository;
        this.estadoRepository = estadoRepository;
    }

    public ClienteResponse crear(ClienteRequest request) {
        Cliente cliente = new Cliente();

        cliente.setNombres(request.getNombres());
        cliente.setApellidos(request.getApellidos());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());
        cliente.setGenero(request.getGenero());
        cliente.setDui(request.getDui());
        cliente.setFechaNacimiento(request.getFechaNacimiento());
        cliente.setNotas(request.getNotas());

        // Conversión segura de tipo primitivo int a objeto Integer para JpaRepository
        if (request.getIdPlanSuscripcion() > 0) {
            PlanSuscripcion plan = planSuscripcionRepository.findById(Integer.valueOf(request.getIdPlanSuscripcion()))
                    .orElse(null);
            cliente.setPlanSuscripcion(plan);
        }

        Estado estado = estadoRepository.findById(Integer.valueOf(request.getIdEstado()))
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        cliente.setEstado(estado);

        Cliente nuevoCliente = clienteRepository.save(cliente);
        return convertirAResponse(nuevoCliente);
    }

    public List<ClienteResponse> obtenerTodos() {
        return clienteRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    public ClienteResponse obtenerPorId(Integer id) {
        return clienteRepository.findById(id)
                .map(this::convertirAResponse)
                .orElse(null);
    }

    private ClienteResponse convertirAResponse(Cliente cliente) {
        ClienteResponse response = new ClienteResponse();

        response.setIdPersona(cliente.getIdPersona());
        response.setNombres(cliente.getNombres());
        response.setApellidos(cliente.getApellidos());
        response.setEmail(cliente.getEmail());
        response.setTelefono(cliente.getTelefono());
        response.setDireccion(cliente.getDireccion());
        response.setGenero(cliente.getGenero());
        response.setDui(cliente.getDui());
        response.setFechaNacimiento(cliente.getFechaNacimiento());

        response.setIdCliente(cliente.getIdCliente());
        response.setNotas(cliente.getNotas());
        response.setCreadoEn(cliente.getCreadoEn());

        if (cliente.getPlanSuscripcion() != null) {
            response.setNombreSuscripcion(cliente.getPlanSuscripcion().getNombreSuscripcion());
        } else {
            response.setNombreSuscripcion("Ninguno");
        }

        if (cliente.getEstado() != null) {
            response.setNombreEstado(cliente.getEstado().getNombreEstado());
        }

        return response;
    }
}
