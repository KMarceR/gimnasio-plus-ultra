package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sv.grupo7.gimnasio_plus_ultra.dto.AccesoQrResponse;
import com.sv.grupo7.gimnasio_plus_ultra.dto.AsistenciaRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.AsistenciaResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Asistencia;
import com.sv.grupo7.gimnasio_plus_ultra.model.Cliente;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.Suscripcion;
import com.sv.grupo7.gimnasio_plus_ultra.model.TipoMovimiento;
import com.sv.grupo7.gimnasio_plus_ultra.repository.AsistenciaRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.ClienteRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.SuscripcionRepository;

@Service
public class AsistenciaService implements CrudService<AsistenciaRequest, AsistenciaResponse> {

    private static final String CLIENTE_ACTIVO = "Activo";
    private static final String ESTADO_DENTRO = "Activo";        // entro y esta dentro
    private static final String ESTADO_SALIO = "Completada";     // entro y ya salio
    private static final String ESTADO_RECHAZADO = "Cancelada";  // no se le permitio entrar
    private static final long MINUTOS_MINIMOS_ENTRE_MARCAS = 10;

    private final AsistenciaRepository asistenciaRepository;
    private final ClienteRepository clienteRepository;
    private final EstadoRepository estadoRepository;
    private final SuscripcionRepository suscripcionRepository;

    public AsistenciaService(AsistenciaRepository asistenciaRepository, ClienteRepository clienteRepository,
            EstadoRepository estadoRepository, SuscripcionRepository suscripcionRepository) {
        this.asistenciaRepository = asistenciaRepository;
        this.clienteRepository = clienteRepository;
        this.estadoRepository = estadoRepository;
        this.suscripcionRepository = suscripcionRepository;
    }

    // ===================== CRUD (registro y correccion manual) =====================

    @Override
    public AsistenciaResponse crear(AsistenciaRequest request) {
        Asistencia asistencia = new Asistencia();
        aplicarDatos(asistencia, request);

        Asistencia nuevaAsistencia = asistenciaRepository.save(asistencia);
        return convertirAResponse(nuevaAsistencia);
    }

    @Override
    public List<AsistenciaResponse> obtenerTodos() {
        return asistenciaRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    @Override
    public AsistenciaResponse obtenerPorId(Integer id) {
        return asistenciaRepository.findById(id)
                .map(this::convertirAResponse)
                .orElse(null);
    }

    @Override
    public AsistenciaResponse actualizar(Integer id, AsistenciaRequest request) {
        Asistencia asistencia = asistenciaRepository.findById(id).orElse(null);
        if (asistencia == null) {
            return null;
        }
        aplicarDatos(asistencia, request);
        return convertirAResponse(asistenciaRepository.save(asistencia));
    }

    @Override
    public boolean eliminar(Integer id) {
        if (!asistenciaRepository.existsById(id)) {
            return false;
        }
        asistenciaRepository.deleteById(id);
        return true;
    }

    public List<AsistenciaResponse> obtenerPresentes() {
        return asistenciaRepository.buscarPresentes(LocalDate.now()).stream()
                .map(this::convertirAResponse).toList();
    }

    public List<AsistenciaResponse> obtenerDeHoy() {
        return asistenciaRepository.findByFechaAsistenciaOrderByHoraSesionDesc(LocalDate.now()).stream()
                .map(this::convertirAResponse).toList();
    }

    // ===================== ACCESO POR QR (entrada / salida) =====================

    @Transactional
    public AccesoQrResponse registrarAccesoQr(String codigoQr) {
        if (codigoQr == null || codigoQr.isBlank()) {
            throw new RuntimeException("Debe enviar el código QR");
        }
        Cliente cliente = clienteRepository.findByCodigoQr(codigoQr.trim())
                .orElseThrow(() -> new RuntimeException("Código QR no válido"));

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now().withNano(0);

        // 1. Ya esta dentro? -> es una SALIDA
        List<Asistencia> abiertas = asistenciaRepository.buscarEntradasAbiertas(cliente.getIdCliente(), hoy);
        if (!abiertas.isEmpty()) {
            return registrarSalida(abiertas.get(0), cliente, ahora);
        }
        // 2. No esta dentro -> intenta una ENTRADA
        return registrarEntrada(cliente, hoy, ahora);
    }

    private AccesoQrResponse registrarSalida(Asistencia entrada, Cliente cliente, LocalTime ahora) {
        long minutos = Duration.between(entrada.getHoraSesion(), ahora).toMinutes();
        if (minutos < MINUTOS_MINIMOS_ENTRE_MARCAS) {
            // Doble escaneo accidental: no se registra la salida
            return crearRespuesta(TipoMovimiento.DUPLICADO, true,
                    "Ya tiene una entrada registrada a las " + entrada.getHoraSesion(), cliente, entrada);
        }
        entrada.setHoraSalida(ahora);
        entrada.setEstado(buscarEstado(ESTADO_SALIO));
        asistenciaRepository.save(entrada);
        return crearRespuesta(TipoMovimiento.SALIDA, true, "Salida registrada", cliente, entrada);
    }

    private AccesoQrResponse registrarEntrada(Cliente cliente, LocalDate hoy, LocalTime ahora) {
        boolean clienteActivo = CLIENTE_ACTIVO.equals(cliente.getEstado().getNombreEstado());
        List<Suscripcion> vigentes = suscripcionRepository.buscarVigentes(cliente.getIdCliente(), hoy);
        boolean permitido = clienteActivo && !vigentes.isEmpty();

        String mensaje;
        if (!clienteActivo) {
            mensaje = "Acceso denegado: el cliente está inactivo";
        } else if (vigentes.isEmpty()) {
            mensaje = "Acceso denegado: no tiene una suscripción vigente";
        } else {
            mensaje = "Entrada registrada";
        }

        // Se guarda siempre: si fue rechazado, queda como historial del intento
        Asistencia asistencia = new Asistencia();
        asistencia.setCliente(cliente);
        asistencia.setFechaAsistencia(hoy);
        asistencia.setHoraSesion(ahora);
        asistencia.setEstado(buscarEstado(permitido ? ESTADO_DENTRO : ESTADO_RECHAZADO));
        asistenciaRepository.save(asistencia);

        AccesoQrResponse respuesta = crearRespuesta(
                permitido ? TipoMovimiento.ENTRADA : TipoMovimiento.RECHAZADO,
                permitido, mensaje, cliente, asistencia);
        if (!vigentes.isEmpty()) {
            respuesta.setNombrePlan(vigentes.get(0).getPlanSuscripcion().getNombreSuscripcion());
            respuesta.setFechaVencimiento(vigentes.get(0).getFechaVencimiento());
        }
        return respuesta;
    }

    // ===================== METODOS DE APOYO =====================

    private Estado buscarEstado(String nombre) {
        return estadoRepository.buscarPorNombre(nombre)
                .orElseThrow(() -> new RuntimeException("No existe el estado " + nombre));
    }

    private AccesoQrResponse crearRespuesta(TipoMovimiento tipo, boolean permitido, String mensaje,
            Cliente cliente, Asistencia asistencia) {
        AccesoQrResponse r = new AccesoQrResponse();
        r.setTipoMovimiento(tipo);
        r.setPermitido(permitido);
        r.setMensaje(mensaje);
        r.setIdAsistencia(asistencia.getIdAsistencia());
        r.setNombreCliente(cliente.getNombres() + " " + cliente.getApellidos());
        r.setFecha(asistencia.getFechaAsistencia());
        r.setHoraEntrada(asistencia.getHoraSesion());
        r.setHoraSalida(asistencia.getHoraSalida());
        return r;
    }

    private void aplicarDatos(Asistencia asistencia, AsistenciaRequest request) {
        asistencia.setFechaAsistencia(request.getFechaAsistencia());
        asistencia.setHoraSesion(request.getHoraSesion());
        asistencia.setHoraSalida(request.getHoraSalida());

        Cliente cliente = clienteRepository.findById(Integer.valueOf(request.getIdCliente()))
                .orElseThrow(() -> new RuntimeException("Cliente no registrado"));
        asistencia.setCliente(cliente);

        Estado estado = estadoRepository.findById(Integer.valueOf(request.getIdEstado()))
                .orElseThrow(() -> new RuntimeException("Estado inválido"));
        asistencia.setEstado(estado);
    }

    private AsistenciaResponse convertirAResponse(Asistencia asistencia) {
        AsistenciaResponse response = new AsistenciaResponse();
        response.setIdAsistencia(asistencia.getIdAsistencia());
        response.setFechaAsistencia(asistencia.getFechaAsistencia());
        response.setHoraSesion(asistencia.getHoraSesion());
        response.setHoraSalida(asistencia.getHoraSalida());
        response.setCreadoEn(asistencia.getCreadoEn());

        if (asistencia.getCliente() != null) {
            response.setIdCliente(asistencia.getCliente().getIdCliente());
            String completo = asistencia.getCliente().getNombres() + " " + asistencia.getCliente().getApellidos();
            response.setNombreCliente(completo);
        }
        if (asistencia.getEstado() != null) {
            response.setNombreEstado(asistencia.getEstado().getNombreEstado());
        }
        return response;
    }
}
