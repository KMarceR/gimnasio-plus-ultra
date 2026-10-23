package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sv.grupo7.gimnasio_plus_ultra.dto.ClienteRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.ClienteResponse;
import com.sv.grupo7.gimnasio_plus_ultra.dto.SuscripcionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.model.Cliente;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.PlanSuscripcion;
import com.sv.grupo7.gimnasio_plus_ultra.repository.ClienteRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.PlanSuscripcionRepository;

@Service
public class ClienteService {

    private static final String ESTADO_ACTIVO = "Activo";

    private final ClienteRepository clienteRepository;
    private final PlanSuscripcionRepository planSuscripcionRepository;
    private final EstadoRepository estadoRepository;
    private final SuscripcionService suscripcionService;

    public ClienteService(ClienteRepository clienteRepository,
            PlanSuscripcionRepository planSuscripcionRepository,
            EstadoRepository estadoRepository,
            SuscripcionService suscripcionService) {
        this.clienteRepository = clienteRepository;
        this.planSuscripcionRepository = planSuscripcionRepository;
        this.estadoRepository = estadoRepository;
        this.suscripcionService = suscripcionService;
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        Cliente cliente = new Cliente();
        aplicarDatos(cliente, request);
        cliente.setCodigoQr(UUID.randomUUID().toString()); // carnet QR unico; se genera solo al crear

        Cliente nuevoCliente = clienteRepository.save(cliente);

        // Si se registra activo y con plan, se le vende su primera suscripcion
        boolean activo = ESTADO_ACTIVO.equals(nuevoCliente.getEstado().getNombreEstado());
        if (activo && nuevoCliente.getPlanSuscripcion() != null) {
            SuscripcionRequest venta = new SuscripcionRequest();
            venta.setIdCliente(nuevoCliente.getIdCliente());
            venta.setIdPlanSuscripcion(nuevoCliente.getPlanSuscripcion().getIdPlanSuscripcion());
            suscripcionService.vender(venta);
        }
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

    public ClienteResponse actualizar(Integer id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente == null) {
            return null;
        }
        aplicarDatos(cliente, request); // el codigo QR no cambia al editar
        return convertirAResponse(clienteRepository.save(cliente));
    }

    public boolean eliminar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            return false;
        }
        clienteRepository.deleteById(id);
        return true;
    }

    private void aplicarDatos(Cliente cliente, ClienteRequest request) {
        cliente.setNombres(request.getNombres());
        cliente.setApellidos(request.getApellidos());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());
        cliente.setGenero(request.getGenero());
        cliente.setDui(request.getDui());
        cliente.setFechaNacimiento(request.getFechaNacimiento());
        cliente.setNotas(request.getNotas());

        if (request.getIdPlanSuscripcion() != null && request.getIdPlanSuscripcion() > 0) {
            PlanSuscripcion plan = planSuscripcionRepository.findById(Integer.valueOf(request.getIdPlanSuscripcion()))
                    .orElse(null);
            cliente.setPlanSuscripcion(plan);
        } else {
            cliente.setPlanSuscripcion(null);
        }

        Estado estado = estadoRepository.findById(Integer.valueOf(request.getIdEstado()))
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        cliente.setEstado(estado);
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
        response.setCodigoQr(cliente.getCodigoQr());
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
