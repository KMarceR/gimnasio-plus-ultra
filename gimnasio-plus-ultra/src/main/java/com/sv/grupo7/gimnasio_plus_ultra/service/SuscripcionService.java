package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sv.grupo7.gimnasio_plus_ultra.dto.SuscripcionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.SuscripcionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Cliente;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.PlanSuscripcion;
import com.sv.grupo7.gimnasio_plus_ultra.model.Suscripcion;
import com.sv.grupo7.gimnasio_plus_ultra.model.TipoPlan;
import com.sv.grupo7.gimnasio_plus_ultra.repository.ClienteRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.PlanSuscripcionRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.SuscripcionRepository;

@Service
public class SuscripcionService {

    private static final String ESTADO_ACTIVO = "Activo";
    private static final String ESTADO_CANCELADA = "Cancelada";

    private final SuscripcionRepository suscripcionRepository;
    private final ClienteRepository clienteRepository;
    private final PlanSuscripcionRepository planSuscripcionRepository;
    private final EstadoRepository estadoRepository;

    public SuscripcionService(SuscripcionRepository suscripcionRepository,
            ClienteRepository clienteRepository,
            PlanSuscripcionRepository planSuscripcionRepository,
            EstadoRepository estadoRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.clienteRepository = clienteRepository;
        this.planSuscripcionRepository = planSuscripcionRepository;
        this.estadoRepository = estadoRepository;
    }

    @Transactional
    public SuscripcionResponse vender(SuscripcionRequest request) {
        if (request.getIdCliente() == null || request.getIdPlanSuscripcion() == null) {
            throw new RuntimeException("Debe indicar el cliente y el plan");
        }
        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        if (!ESTADO_ACTIVO.equals(cliente.getEstado().getNombreEstado())) {
            throw new RuntimeException("El cliente no está activo");
        }
        PlanSuscripcion plan = planSuscripcionRepository.findById(request.getIdPlanSuscripcion())
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
        if (!ESTADO_ACTIVO.equals(plan.getEstado().getNombreEstado())) {
            throw new RuntimeException("El plan no está disponible para la venta");
        }
        Estado activo = estadoRepository.buscarPorNombre(ESTADO_ACTIVO)
                .orElseThrow(() -> new RuntimeException("No existe el estado Activo"));

        LocalDate inicio = calcularFechaInicio(cliente, request.getFechaInicio());
        TipoPlan tipo = TipoPlan.valueOf(plan.getTipoPlan());

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setCliente(cliente);
        suscripcion.setPlanSuscripcion(plan);
        suscripcion.setEstado(activo);
        suscripcion.setFechaInicio(inicio);
        suscripcion.setFechaVencimiento(tipo.calcularVencimiento(inicio));
        suscripcion.setPrecioPagado(plan.getPrecio()); // precio al momento de la venta

        cliente.setPlanSuscripcion(plan); // plan actual del cliente
        clienteRepository.save(cliente);

        return convertirAResponse(suscripcionRepository.save(suscripcion));
    }

    public List<SuscripcionResponse> obtenerTodos() {
        return suscripcionRepository.findAll().stream().map(this::convertirAResponse).toList();
    }

    public SuscripcionResponse obtenerPorId(Integer id) {
        return suscripcionRepository.findById(id).map(this::convertirAResponse).orElse(null);
    }

    public List<SuscripcionResponse> obtenerPorCliente(Integer idCliente) {
        return suscripcionRepository.findByClienteIdClienteOrderByFechaVencimientoDesc(idCliente)
                .stream().map(this::convertirAResponse).toList();
    }

    @Transactional
    public SuscripcionResponse cancelar(Integer id) {
        Suscripcion suscripcion = suscripcionRepository.findById(id).orElse(null);
        if (suscripcion == null) {
            return null;
        }
        Estado cancelada = estadoRepository.buscarPorNombre(ESTADO_CANCELADA)
                .orElseThrow(() -> new RuntimeException("No existe el estado Cancelada"));
        suscripcion.setEstado(cancelada);
        return convertirAResponse(suscripcionRepository.save(suscripcion));
    }

    // Si el cliente aún tiene una suscripción activa, la nueva empieza al día
    // siguiente de que termine (renovación)
    private LocalDate calcularFechaInicio(Cliente cliente, LocalDate fechaSolicitada) {
        LocalDate inicio = (fechaSolicitada != null) ? fechaSolicitada : LocalDate.now();
        LocalDate ultimoVencimiento = suscripcionRepository.ultimaFechaVencimiento(cliente.getIdCliente());
        if (ultimoVencimiento != null && !ultimoVencimiento.isBefore(inicio)) {
            inicio = ultimoVencimiento.plusDays(1);
        }
        return inicio;
    }

    private SuscripcionResponse convertirAResponse(Suscripcion s) {
        SuscripcionResponse r = new SuscripcionResponse();
        r.setIdSuscripcion(s.getIdSuscripcion());
        r.setIdCliente(s.getCliente().getIdCliente());
        r.setNombreCliente(s.getCliente().getNombres() + " " + s.getCliente().getApellidos());
        r.setNombrePlan(s.getPlanSuscripcion().getNombreSuscripcion());
        r.setTipoPlan(s.getPlanSuscripcion().getTipoPlan());
        r.setFechaInicio(s.getFechaInicio());
        r.setFechaVencimiento(s.getFechaVencimiento());
        r.setPrecioPagado(s.getPrecioPagado());
        r.setNombreEstado(s.getEstado().getNombreEstado());
        r.setVigente(s.estaVigente(LocalDate.now()));
        r.setCreadoEn(s.getCreadoEn());
        return r;
    }
}
