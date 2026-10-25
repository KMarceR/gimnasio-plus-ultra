package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sv.grupo7.gimnasio_plus_ultra.dto.UsuarioRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.UsuarioResponse;
import com.sv.grupo7.gimnasio_plus_ultra.exception.RecursoNoEncontradoException;
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

        Usuario usuario = new Usuario();

        copiarDatosPersona(usuario.getPersona(), request);
        usuario.setNombreUsuario(request.getDui());

        String hashContrasena = passwordEncoder.encode(request.getContrasena());
        usuario.setContrasena(hashContrasena);

        Rol rol = rolRepository.findById(Validaciones.idObligatorio(request.getIdRol(), "idRol"))
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado"));
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
        copiarDatosPersona(usuario.getPersona(), request);
        usuario.setNombreUsuario(request.getDui());

        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }

        Rol rol = rolRepository.findById(Validaciones.idObligatorio(request.getIdRol(), "idRol"))
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado"));
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

    public UsuarioResponse login(String nombreUsuario, String contrasena) {
        return usuarioRepository.buscarPorNombreUsuario(nombreUsuario)
                .filter(usuario -> passwordEncoder.matches(contrasena, usuario.getContrasena()))
                .filter(usuario -> "Activo".equals(usuario.getEstado().getNombreEstado()))
                .map(this::convertirAResponse)
                .orElse(null);
    }
}
