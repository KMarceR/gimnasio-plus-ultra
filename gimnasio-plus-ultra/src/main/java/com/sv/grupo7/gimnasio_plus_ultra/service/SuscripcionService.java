package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sv.grupo7.gimnasio_plus_ultra.dto.CambioFechaSuscripcion;
import com.sv.grupo7.gimnasio_plus_ultra.dto.SuscripcionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.SuscripcionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.dto.VistaCancelacionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.exception.DatosInvalidosException;
import com.sv.grupo7.gimnasio_plus_ultra.exception.RecursoNoEncontradoException;
import com.sv.grupo7.gimnasio_plus_ultra.exception.ReglaNegocioException;
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
    private static final String ESTADO_VENCIDA = "Vencida";
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
            throw new DatosInvalidosException("Debe indicar el cliente y el plan");
        }
        Cliente cliente = clienteRepository.buscarParaActualizar(request.getIdCliente())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        if (!ESTADO_ACTIVO.equals(cliente.getEstado().getNombreEstado())) {
            throw new ReglaNegocioException("El cliente no está activo");
        }
        PlanSuscripcion plan = planSuscripcionRepository.findById(request.getIdPlanSuscripcion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Plan no encontrado"));
        if (!ESTADO_ACTIVO.equals(plan.getEstado().getNombreEstado())) {
            throw new ReglaNegocioException("El plan no está disponible para la venta");
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

        Suscripcion guardada = suscripcionRepository.save(suscripcion);
        actualizarPlanActual(cliente);
        return convertirAResponse(guardada);
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

    // Muestra que pasaria al cancelar, sin guardar nada
    public VistaCancelacionResponse vistaCancelacion(Integer id) {
        Suscripcion suscripcion = suscripcionRepository.findById(id).orElse(null);
        if (suscripcion == null) {
            return null;
        }
        validarCancelable(suscripcion);
        List<Reprogramacion> reprogramaciones = calcularReprogramaciones(suscripcion);

        List<CambioFechaSuscripcion> cambios = new ArrayList<>();
        for (Reprogramacion r : reprogramaciones) {
            CambioFechaSuscripcion cambio = new CambioFechaSuscripcion();
            cambio.setIdSuscripcion(r.suscripcion().getIdSuscripcion());
            cambio.setNombrePlan(r.suscripcion().getPlanSuscripcion().getNombreSuscripcion());
            cambio.setFechaInicioActual(r.suscripcion().getFechaInicio());
            cambio.setFechaVencimientoActual(r.suscripcion().getFechaVencimiento());
            cambio.setNuevaFechaInicio(r.inicio());
            cambio.setNuevaFechaVencimiento(r.vencimiento());
            cambios.add(cambio);
        }
        VistaCancelacionResponse vista = new VistaCancelacionResponse();
        vista.setCambios(cambios);
        vista.setQuedaSinPlanVigente(suscripcion.estaVigente(LocalDate.now()) && cambios.isEmpty());
        return vista;
    }

    // Cancela y adelanta las suscripciones activas que venian despues
    @Transactional
    public SuscripcionResponse cancelar(Integer id) {
        Suscripcion suscripcion = suscripcionRepository.findById(id).orElse(null);
        if (suscripcion == null) {
            return null;
        }
        validarCancelable(suscripcion);
        clienteRepository.buscarParaActualizar(suscripcion.getCliente().getIdCliente()); // bloqueo, igual que al vender

        List<Reprogramacion> reprogramaciones = calcularReprogramaciones(suscripcion);
        Estado cancelada = estadoRepository.buscarPorNombre(ESTADO_CANCELADA)
                .orElseThrow(() -> new RuntimeException("No existe el estado Cancelada"));
        suscripcion.setEstado(cancelada);
        Suscripcion guardada = suscripcionRepository.save(suscripcion);

        for (Reprogramacion r : reprogramaciones) {
            r.suscripcion().setFechaInicio(r.inicio());
            r.suscripcion().setFechaVencimiento(r.vencimiento());
            suscripcionRepository.save(r.suscripcion());
        }
        actualizarPlanActual(suscripcion.getCliente());
        return convertirAResponse(guardada);
    }

    private void validarCancelable(Suscripcion suscripcion) {
        if (!ESTADO_ACTIVO.equals(suscripcion.getEstado().getNombreEstado())) {
            throw new ReglaNegocioException("Solo se pueden cancelar suscripciones activas");
        }
    }

    private record Reprogramacion(Suscripcion suscripcion, LocalDate inicio, LocalDate vencimiento) {
    }

    // La siguiente empieza hoy si la cancelada ya estaba corriendo, o en la fecha en que iba a empezar la cancelada
    private List<Reprogramacion> calcularReprogramaciones(Suscripcion cancelada) {
        LocalDate hoy = LocalDate.now();
        LocalDate inicio = cancelada.getFechaInicio().isAfter(hoy) ? cancelada.getFechaInicio() : hoy;
        List<Suscripcion> siguientes = suscripcionRepository.buscarActivasPosteriores(
                cancelada.getCliente().getIdCliente(), cancelada.getFechaInicio());

        List<Reprogramacion> resultado = new ArrayList<>();
        for (Suscripcion siguiente : siguientes) {
            TipoPlan tipo = TipoPlan.valueOf(siguiente.getPlanSuscripcion().getTipoPlan());
            LocalDate vencimiento = tipo.calcularVencimiento(inicio);
            resultado.add(new Reprogramacion(siguiente, inicio, vencimiento));
            inicio = vencimiento.plusDays(1);
        }
        return resultado;
    }

    @Transactional
    public int marcarVencidas() {
        List<Suscripcion> vencidas = suscripcionRepository.buscarVencidas(LocalDate.now());
        if (vencidas.isEmpty()) {
            return 0;
        }
        Estado vencida = estadoRepository.buscarPorNombre(ESTADO_VENCIDA)
                .orElseThrow(() -> new RuntimeException("No existe el estado Vencida"));
        for (Suscripcion suscripcion : vencidas) {
            suscripcion.setEstado(vencida);
        }
        suscripcionRepository.saveAll(vencidas);
        vencidas.forEach(suscripcion -> actualizarPlanActual(suscripcion.getCliente()));
        return vencidas.size();
    }

    // El plan actual del cliente es el de su suscripcion vigente hoy; sin vigente, queda sin plan
    private void actualizarPlanActual(Cliente cliente) {
        List<Suscripcion> vigentes = suscripcionRepository.buscarVigentes(cliente.getIdCliente(), LocalDate.now());
        cliente.setPlanSuscripcion(vigentes.isEmpty() ? null : vigentes.get(0).getPlanSuscripcion());
        clienteRepository.save(cliente);
    }

    // Si el cliente aún tiene una suscripción activa, la nueva empieza al díasiguiente de que termine (renovación)
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
