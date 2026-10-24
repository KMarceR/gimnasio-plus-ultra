package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sv.grupo7.gimnasio_plus_ultra.dto.EntrenadorRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.EntrenadorResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Entrenador;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.Rol;
import com.sv.grupo7.gimnasio_plus_ultra.model.Usuario;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EntrenadorRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.PersonaRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.RolRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.UsuarioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class EntrenadorService extends PersonaServiceBase implements CrudService<EntrenadorRequest, EntrenadorResponse> {

    private static final String ROL_ENTRENADOR = "Entrenador";
    private static final String ESTADO_ACTIVO = "Activo";
    private static final String FORMATO_DUI = "\\d{8}-\\d"; // 8 dígitos, guion, 1 dígito

    private final EntrenadorRepository entrenadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EstadoRepository estadoRepository;
    private final PasswordEncoder passwordEncoder;

    public EntrenadorService(EntrenadorRepository entrenadorRepository,
            UsuarioRepository usuarioRepository,
            PersonaRepository personaRepository,
            RolRepository rolRepository,
            EstadoRepository estadoRepository,
            PasswordEncoder passwordEncoder) {
        super(personaRepository);
        this.entrenadorRepository = entrenadorRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.estadoRepository = estadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public EntrenadorResponse crear(EntrenadorRequest request) {
        validarDatos(request, true);
        validarPersonaNueva(request);

        Rol rolEntrenador = rolRepository.buscarPorNombre(ROL_ENTRENADOR)
                .orElseThrow(() -> new RuntimeException("No existe el rol Entrenador"));
        Estado estado = obtenerEstado(request.getIdEstado());

        Usuario usuario = new Usuario();
        copiarDatosPersona(usuario.getPersona(), request);
        usuario.setNombreUsuario(request.getDui());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setRol(rolEntrenador);
        usuario.setEstado(estado);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Entrenador entrenador = new Entrenador();
        entrenador.setUsuario(usuarioGuardado);
        entrenador.setPersona(usuarioGuardado.getPersona());
        entrenador.setEspecialidad(request.getEspecialidad());
        entrenador.setTarifaPorSesion(request.getTarifaPorSesion());
        entrenador.setEstado(estado);

        return convertirAResponse(entrenadorRepository.save(entrenador));
    }

    @Override
    public List<EntrenadorResponse> obtenerTodos() {
        return entrenadorRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    @Override
    public EntrenadorResponse obtenerPorId(Integer id) {
        return entrenadorRepository.findById(id)
                .map(this::convertirAResponse)
                .orElse(null);
    }

    @Transactional
    @Override
    public EntrenadorResponse actualizar(Integer id, EntrenadorRequest request) {
        Entrenador entrenador = entrenadorRepository.findById(id).orElse(null);
        if (entrenador == null) {
            return null;
        }
        validarDatos(request, false);

        Usuario usuario = entrenador.getUsuario();
        validarPersonaEditada(usuario.getPersona(), request);
        Estado estado = obtenerEstado(request.getIdEstado());

        copiarDatosPersona(usuario.getPersona(), request);
        usuario.setNombreUsuario(request.getDui());
        if (!esVacio(request.getContrasena())) { // contraseña solo si viene una nueva
            usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }
        usuario.setEstado(estado);
        usuarioRepository.save(usuario);

        entrenador.setEspecialidad(request.getEspecialidad());
        entrenador.setTarifaPorSesion(request.getTarifaPorSesion());
        entrenador.setEstado(estado);

        return convertirAResponse(entrenadorRepository.save(entrenador));
    }

    @Transactional
    @Override
    public boolean eliminar(Integer id) {
        Entrenador entrenador = entrenadorRepository.findById(id).orElse(null);
        if (entrenador == null) {
            return false;
        }
        Usuario usuario = entrenador.getUsuario();
        entrenadorRepository.delete(entrenador);
        entrenadorRepository.flush();
        usuarioRepository.delete(usuario);
        return true;
    }

    private void validarDatos(EntrenadorRequest request, boolean esNuevo) {
        if (esVacio(request.getNombres()) || esVacio(request.getApellidos()) || esVacio(request.getGenero())
                || esVacio(request.getDui()) || esVacio(request.getEmail()) || request.getFechaNacimiento() == null) {
            throw new RuntimeException("Faltan datos obligatorios del entrenador");
        }
        if (!request.getDui().matches(FORMATO_DUI)) {
            throw new RuntimeException("El DUI debe tener el formato 00000000-0");
        }
        if (esNuevo && esVacio(request.getContrasena())) {
            throw new RuntimeException("La contraseña es obligatoria");
        }
        if (request.getTarifaPorSesion() == null || request.getTarifaPorSesion() < 0) {
            throw new RuntimeException("La tarifa por sesión es obligatoria y no puede ser negativa");
        }
    }

    private Estado obtenerEstado(Integer idEstado) {
        if (idEstado == null || idEstado <= 0) {
            return estadoRepository.buscarPorNombre(ESTADO_ACTIVO)
                    .orElseThrow(() -> new RuntimeException("No existe el estado Activo"));
        }
        return estadoRepository.findById(idEstado)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }

    private EntrenadorResponse convertirAResponse(Entrenador entrenador) {
        EntrenadorResponse response = new EntrenadorResponse();

        response.setIdPersona(entrenador.getIdPersona());
        response.setNombres(entrenador.getNombres());
        response.setApellidos(entrenador.getApellidos());
        response.setEmail(entrenador.getEmail());
        response.setTelefono(entrenador.getTelefono());
        response.setDireccion(entrenador.getDireccion());
        response.setGenero(entrenador.getGenero());
        response.setDui(entrenador.getDui());
        response.setFechaNacimiento(entrenador.getFechaNacimiento());

        response.setIdEntrenador(entrenador.getIdEntrenador());
        response.setEspecialidad(entrenador.getEspecialidad());
        response.setTarifaPorSesion(entrenador.getTarifaPorSesion());
        response.setCreadoEn(entrenador.getCreadoEn());

        if (entrenador.getUsuario() != null) {
            response.setNombreUsuario(entrenador.getUsuario().getNombreUsuario());
        }
        if (entrenador.getEstado() != null) {
            response.setNombreEstado(entrenador.getEstado().getNombreEstado());
        }
        return response;
    }
}
