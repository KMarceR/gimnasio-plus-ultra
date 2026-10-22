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

import jakarta.transaction.Transactional;

@Service
public class EntrenadorService {

    private static final String ROL_ENTRENADOR = "Entrenador";
    private static final String ESTADO_ACTIVO = "Activo";
    private static final String FORMATO_DUI = "\\d{8}-\\d"; // 8 dígitos, guion, 1 dígito

    private final EntrenadorRepository entrenadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final EstadoRepository estadoRepository;
    private final PasswordEncoder passwordEncoder;

    public EntrenadorService(EntrenadorRepository entrenadorRepository,
            UsuarioRepository usuarioRepository,
            PersonaRepository personaRepository,
            RolRepository rolRepository,
            EstadoRepository estadoRepository,
            PasswordEncoder passwordEncoder) {
        this.entrenadorRepository = entrenadorRepository;
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.rolRepository = rolRepository;
        this.estadoRepository = estadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public EntrenadorResponse crear(EntrenadorRequest request) {
        validarDatos(request, true);

        if (personaRepository.existsByDui(request.getDui())) {
            throw new RuntimeException("Ya existe una persona registrada con ese DUI");
        }
        if (personaRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe una persona registrada con ese email");
        }

        Rol rolEntrenador = rolRepository.buscarPorNombre(ROL_ENTRENADOR)
                .orElseThrow(() -> new RuntimeException("No existe el rol Entrenador"));
        Estado estado = obtenerEstado(request.getIdEstado());

        Usuario usuario = new Usuario();
        copiarDatosPersona(usuario, request);
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

    public List<EntrenadorResponse> obtenerTodos() {
        return entrenadorRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    public EntrenadorResponse obtenerPorId(Integer id) {
        return entrenadorRepository.findById(id)
                .map(this::convertirAResponse)
                .orElse(null);
    }

    @Transactional
    public EntrenadorResponse actualizar(Integer id, EntrenadorRequest request) {
        Entrenador entrenador = entrenadorRepository.findById(id).orElse(null);
        if (entrenador == null) {
            return null;
        }
        validarDatos(request, false);

        Usuario usuario = entrenador.getUsuario();
        if (!usuario.getDui().equals(request.getDui()) && personaRepository.existsByDui(request.getDui())) {
            throw new RuntimeException("Ya existe una persona registrada con ese DUI");
        }
        if (!usuario.getEmail().equals(request.getEmail()) && personaRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe una persona registrada con ese email");
        }
        Estado estado = obtenerEstado(request.getIdEstado());

        copiarDatosPersona(usuario, request);
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

    private void copiarDatosPersona(Usuario usuario, EntrenadorRequest request) {
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setGenero(request.getGenero());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setDireccion(request.getDireccion());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setDui(request.getDui());
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
