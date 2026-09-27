package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sv.grupo7.gimnasio_plus_ultra.dto.EntrenadorRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.EntrenadorResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Entrenador;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.Usuario;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EntrenadorRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.UsuarioRepository;

@Service
public class EntrenadorService {

    private final EntrenadorRepository entrenadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoRepository estadoRepository;

    public EntrenadorService(EntrenadorRepository entrenadorRepository, UsuarioRepository usuarioRepository, EstadoRepository estadoRepository) {
        this.entrenadorRepository = entrenadorRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoRepository = estadoRepository;
    }

    public EntrenadorResponse crear(EntrenadorRequest request) {
        Entrenador entrenador = new Entrenador();
        aplicarDatos(entrenador, request);

        Entrenador nuevoEntrenador = entrenadorRepository.save(entrenador);
        return convertirAResponse(nuevoEntrenador);
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

    public EntrenadorResponse actualizar(Integer id, EntrenadorRequest request) {
        Entrenador entrenador = entrenadorRepository.findById(id).orElse(null);
        if (entrenador == null) {
            return null;
        }
        aplicarDatos(entrenador, request);
        return convertirAResponse(entrenadorRepository.save(entrenador));
    }

    public boolean eliminar(Integer id) {
        if (!entrenadorRepository.existsById(id)) {
            return false;
        }
        entrenadorRepository.deleteById(id);
        return true;
    }

    private void aplicarDatos(Entrenador entrenador, EntrenadorRequest request) {
        entrenador.setNombres(request.getNombres());
        entrenador.setApellidos(request.getApellidos());
        entrenador.setEmail(request.getEmail());
        entrenador.setTelefono(request.getTelefono());
        entrenador.setDireccion(request.getDireccion());
        entrenador.setGenero(request.getGenero());
        entrenador.setDui(request.getDui());
        entrenador.setFechaNacimiento(request.getFechaNacimiento());

        entrenador.setEspecialidad(request.getEspecialidad());
        entrenador.setTarifaPorSesion(request.getTarifaPorSesion());

        Usuario usuario = usuarioRepository.findById(Integer.valueOf(request.getIdUsuario()))
                .orElseThrow(() -> new RuntimeException("Usuario del sistema no encontrado"));
        entrenador.setUsuario(usuario);

        Estado estado = estadoRepository.findById(Integer.valueOf(request.getIdEstado()))
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        entrenador.setEstado(estado);
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
