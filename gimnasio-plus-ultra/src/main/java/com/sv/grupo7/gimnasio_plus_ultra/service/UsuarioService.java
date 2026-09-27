package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sv.grupo7.gimnasio_plus_ultra.dto.UsuarioRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.UsuarioResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.Rol;
import com.sv.grupo7.gimnasio_plus_ultra.model.Usuario;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.RolRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EstadoRepository estadoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            EstadoRepository estadoRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.estadoRepository = estadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse crear(UsuarioRequest request) {
        Usuario usuario = new Usuario();

        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setGenero(request.getGenero());
        usuario.setDui(request.getDui());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setNombreUsuario(request.getNombreUsuario());

        String hashContrasena = passwordEncoder.encode(request.getContrasena());
        usuario.setContrasena(hashContrasena);

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rol);

        Estado estado = estadoRepository.findById(request.getIdEstado())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        usuario.setEstado(estado);

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return convertirAResponse(nuevoUsuario);
    }

    public List<UsuarioResponse> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    public UsuarioResponse obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .map(this::convertirAResponse)
                .orElse(null);
    }

    public UsuarioResponse actualizar(Integer id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return null;
        }

        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setGenero(request.getGenero());
        usuario.setDui(request.getDui());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setNombreUsuario(request.getNombreUsuario());

        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rol);

        Estado estado = estadoRepository.findById(request.getIdEstado())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        usuario.setEstado(estado);

        return convertirAResponse(usuarioRepository.save(usuario));
    }

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
}
