package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sv.grupo7.gimnasio_plus_ultra.dto.UsuarioRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.UsuarioResponse;
import com.sv.grupo7.gimnasio_plus_ultra.exception.RecursoNoEncontradoException;
import com.sv.grupo7.gimnasio_plus_ultra.exception.ReglaNegocioException;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.Rol;
import com.sv.grupo7.gimnasio_plus_ultra.model.Usuario;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.RolRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.UsuarioRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.PersonaRepository;
import com.sv.grupo7.gimnasio_plus_ultra.util.Validaciones;

@Service
public class UsuarioService extends PersonaServiceBase implements CrudService<UsuarioRequest, UsuarioResponse> {

    // Un entrenador necesita especialidad y tarifa: solo se crea y cambia desde EntrenadorService
    private static final String ROL_ENTRENADOR = "Entrenador";
    private static final String MENSAJE_ENTRENADOR =
            "Los entrenadores se registran y modifican desde la pantalla Entrenadores";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EstadoRepository estadoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            EstadoRepository estadoRepository,
            PersonaRepository personaRepository,
            PasswordEncoder passwordEncoder) {
        super(personaRepository);
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.estadoRepository = estadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioResponse crear(UsuarioRequest request) {
        validarPersonaNueva(request);
        Rol rol = buscarRol(request.getIdRol());
        if (ROL_ENTRENADOR.equals(rol.getNombreRol())) {
            throw new ReglaNegocioException(MENSAJE_ENTRENADOR);
        }

        Usuario usuario = new Usuario();

        copiarDatosPersona(usuario.getPersona(), request);
        usuario.setNombreUsuario(request.getDui());

        String hashContrasena = passwordEncoder.encode(request.getContrasena());
        usuario.setContrasena(hashContrasena);
        usuario.setRol(rol);

        Estado estado = estadoRepository.findById(Validaciones.idObligatorio(request.getIdEstado(), "idEstado"))
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado"));
        usuario.setEstado(estado);

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return convertirAResponse(nuevoUsuario);
    }

    @Override
    public List<UsuarioResponse> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    @Override
    public UsuarioResponse obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .map(this::convertirAResponse)
                .orElse(null);
    }

    @Override
    public UsuarioResponse actualizar(Integer id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return null;
        }
        validarPersonaEditada(usuario.getPersona(), request);
        Rol rol = buscarRol(request.getIdRol());
        boolean eraEntrenador = ROL_ENTRENADOR.equals(usuario.getRol().getNombreRol());
        boolean seraEntrenador = ROL_ENTRENADOR.equals(rol.getNombreRol());
        if (eraEntrenador != seraEntrenador) {
            throw new ReglaNegocioException(MENSAJE_ENTRENADOR);
        }
        copiarDatosPersona(usuario.getPersona(), request);
        usuario.setNombreUsuario(request.getDui());

        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }
        usuario.setRol(rol);

        Estado estado = estadoRepository.findById(Validaciones.idObligatorio(request.getIdEstado(), "idEstado"))
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado"));
        usuario.setEstado(estado);

        return convertirAResponse(usuarioRepository.save(usuario));
    }

    @Override
    public boolean eliminar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            return false;
        }
        usuarioRepository.deleteById(id);
        return true;
    }

    private UsuarioResponse convertirAResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();

        response.setIdPersona(usuario.getIdPersona());
        response.setNombres(usuario.getNombres());
        response.setApellidos(usuario.getApellidos());
        response.setEmail(usuario.getEmail());
        response.setTelefono(usuario.getTelefono());
        response.setDireccion(usuario.getDireccion());
        response.setGenero(usuario.getGenero());
        response.setDui(usuario.getDui());
        response.setFechaNacimiento(usuario.getFechaNacimiento());

        response.setIdUsuario(usuario.getIdUsuario());
        response.setNombreUsuario(usuario.getNombreUsuario());

        if (usuario.getRol() != null) {
            response.setNombreRol(usuario.getRol().getNombreRol());
        }

        if (usuario.getEstado() != null) {
            response.setNombreEstado(usuario.getEstado().getNombreEstado());
        }

        response.setCreadoEn(usuario.getCreadoEn());

        return response;
    }

    private Rol buscarRol(Integer idRol) {
        return rolRepository.findById(Validaciones.idObligatorio(idRol, "idRol"))
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado"));
    }

    public UsuarioResponse login(String nombreUsuario, String contrasena) {
        return usuarioRepository.buscarPorNombreUsuario(nombreUsuario)
                .filter(usuario -> passwordEncoder.matches(contrasena, usuario.getContrasena()))
                .filter(usuario -> "Activo".equals(usuario.getEstado().getNombreEstado()))
                .map(this::convertirAResponse)
                .orElse(null);
    }
}
