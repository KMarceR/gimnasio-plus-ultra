package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sv.grupo7.gimnasio_plus_ultra.dto.SesionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.SesionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.exception.DatosInvalidosException;
import com.sv.grupo7.gimnasio_plus_ultra.exception.RecursoNoEncontradoException;
import com.sv.grupo7.gimnasio_plus_ultra.exception.ReglaNegocioException;
import com.sv.grupo7.gimnasio_plus_ultra.model.Cliente;
import com.sv.grupo7.gimnasio_plus_ultra.model.Entrenador;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.Sesion;
import com.sv.grupo7.gimnasio_plus_ultra.repository.ClienteRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EntrenadorRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.SesionRepository;
import com.sv.grupo7.gimnasio_plus_ultra.util.Validaciones;

@Service
public class SesionService implements CrudService<SesionRequest, SesionResponse> {

    private static final String ACTIVO = "Activo";
    private static final String PROGRAMADA = "Programada";
    private static final String CONFIRMADA = "Confirmada";
    private static final String COMPLETADA = "Completada";
    private static final String CANCELADA = "Cancelada";

    // Estados a los que puede pasar una sesion; Completada y Cancelada ya no cambian
    private static final Map<String, Set<String>> TRANSICIONES = Map.of(
            PROGRAMADA, Set.of(CONFIRMADA, CANCELADA),
            CONFIRMADA, Set.of(COMPLETADA, CANCELADA));

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

    // Toda sesion nueva empieza como Programada
    @Override
    @Transactional
    public SesionResponse crear(SesionRequest request) {
        validarHorario(request);
        if (request.getFechaSesion().isBefore(LocalDate.now())) {
            throw new DatosInvalidosException("No se puede agendar una sesión en una fecha pasada");
        }
        Cliente cliente = buscarCliente(request.getIdCliente());
        Entrenador entrenador = buscarEntrenadorBloqueado(request.getIdEntrenador());
        validarActivos(cliente, entrenador);
        validarChoques(request, cliente, entrenador, 0);

        Sesion sesion = new Sesion();
        aplicarDatos(sesion, request, cliente, entrenador, buscarEstadoPorNombre(PROGRAMADA));
        return convertirAResponse(sesionRepository.save(sesion));
    }

    @Override
    public List<SesionResponse> obtenerTodos() {
        return sesionRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    @Override
    public SesionResponse obtenerPorId(Integer id) {
        return sesionRepository.findById(id)
                .map(this::convertirAResponse)
                .orElse(null);
    }

    @Override
    @Transactional
    public SesionResponse actualizar(Integer id, SesionRequest request) {
        Sesion sesion = sesionRepository.findById(id).orElse(null);
        if (sesion == null) {
            return null;
        }
        String estadoActual = sesion.getEstado().getNombreEstado();
        if (!TRANSICIONES.containsKey(estadoActual)) {
            throw new ReglaNegocioException("No se puede modificar una sesión " + estadoActual.toLowerCase());
        }
        validarHorario(request);
        Cliente cliente = buscarCliente(request.getIdCliente());
        Entrenador entrenador = buscarEntrenadorBloqueado(request.getIdEntrenador());
        boolean cambioPersonas = cliente.getIdCliente() != sesion.getCliente().getIdCliente()
                || entrenador.getIdEntrenador() != sesion.getEntrenador().getIdEntrenador();
        if (cambioPersonas) {
            validarActivos(cliente, entrenador);
        }

        Estado nuevoEstado = estadoRepository.findById(Validaciones.idObligatorio(request.getIdEstado(), "idEstado"))
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado"));
        String nombreNuevo = nuevoEstado.getNombreEstado();
        if (!nombreNuevo.equals(estadoActual) && !TRANSICIONES.get(estadoActual).contains(nombreNuevo)) {
            throw new ReglaNegocioException(
                    "Una sesión " + estadoActual.toLowerCase() + " no puede pasar a " + nombreNuevo.toLowerCase());
        }
        if (!CANCELADA.equals(nombreNuevo)) {
            validarChoques(request, cliente, entrenador, sesion.getIdSesion());
        }

        aplicarDatos(sesion, request, cliente, entrenador, nuevoEstado);
        return convertirAResponse(sesionRepository.save(sesion));
    }

    @Override
    public boolean eliminar(Integer id) {
        if (!sesionRepository.existsById(id)) {
            return false;
        }
        sesionRepository.deleteById(id);
        return true;
    }

    private void validarHorario(SesionRequest request) {
        if (request.getFechaSesion() == null || request.getHoraInicio() == null || request.getHoraFin() == null) {
            throw new DatosInvalidosException("La fecha, la hora de inicio y la hora de fin son obligatorias");
        }
        if (!request.getHoraFin().isAfter(request.getHoraInicio())) {
            throw new DatosInvalidosException("La hora de fin debe ser posterior a la hora de inicio");
        }
    }

    private void validarActivos(Cliente cliente, Entrenador entrenador) {
        if (!ACTIVO.equals(cliente.getEstado().getNombreEstado())) {
            throw new ReglaNegocioException("El cliente no está activo");
        }
        if (!ACTIVO.equals(entrenador.getEstado().getNombreEstado())) {
            throw new ReglaNegocioException("El entrenador no está activo");
        }
    }

    private void validarChoques(SesionRequest request, Cliente cliente, Entrenador entrenador, int idExcluir) {
        List<Sesion> choques = sesionRepository.buscarChoques(request.getFechaSesion(), request.getHoraInicio(),
                request.getHoraFin(), entrenador.getIdEntrenador(), cliente.getIdCliente(), idExcluir);
        if (choques.isEmpty()) {
            return;
        }
        Sesion choque = choques.get(0);
        String quien = choque.getEntrenador().getIdEntrenador() == entrenador.getIdEntrenador()
                ? "El entrenador" : "El cliente";
        throw new ReglaNegocioException(quien + " ya tiene una sesión de " + choque.getHoraInicio()
                + " a " + choque.getHoraFin());
    }

    private Cliente buscarCliente(Integer idCliente) {
        return clienteRepository.findById(Validaciones.idObligatorio(idCliente, "idCliente"))
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
    }

    private Entrenador buscarEntrenadorBloqueado(Integer idEntrenador) {
        return entrenadorRepository.buscarParaActualizar(Validaciones.idObligatorio(idEntrenador, "idEntrenador"))
                .orElseThrow(() -> new RecursoNoEncontradoException("Entrenador no encontrado"));
    }

    private Estado buscarEstadoPorNombre(String nombre) {
        return estadoRepository.buscarPorNombre(nombre)
                .orElseThrow(() -> new RuntimeException("No existe el estado " + nombre));
    }

    private void aplicarDatos(Sesion sesion, SesionRequest request, Cliente cliente, Entrenador entrenador,
            Estado estado) {
        sesion.setFechaSesion(request.getFechaSesion());
        sesion.setHoraInicio(request.getHoraInicio());
        sesion.setHoraFin(request.getHoraFin());
        sesion.setCliente(cliente);
        sesion.setEntrenador(entrenador);
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
            response.setIdCliente(sesion.getCliente().getIdCliente());
            response.setNombreCliente(sesion.getCliente().getNombres() + " " + sesion.getCliente().getApellidos());
        }
        if (sesion.getEntrenador() != null) {
            response.setIdEntrenador(sesion.getEntrenador().getIdEntrenador());
            response.setNombreEntrenador(
                    sesion.getEntrenador().getNombres() + " " + sesion.getEntrenador().getApellidos());
        }
        if (sesion.getEstado() != null) {
            response.setNombreEstado(sesion.getEstado().getNombreEstado());
        }
        return response;
    }
}
