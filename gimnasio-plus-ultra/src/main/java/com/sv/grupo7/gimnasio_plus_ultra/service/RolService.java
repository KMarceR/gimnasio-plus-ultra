package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sv.grupo7.gimnasio_plus_ultra.dto.RolRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.RolResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Rol;
import com.sv.grupo7.gimnasio_plus_ultra.repository.RolRepository;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public RolResponse crear(RolRequest request) {
        Rol rol = new Rol();
        rol.setNombreRol(request.getNombreRol());
        Rol nuevoRol = rolRepository.save(rol);
        return convertirAResponse(nuevoRol);
    }

    public List<RolResponse> obtenerTodos() {
        return rolRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    public RolResponse obtenerPorId(Integer id) {
        return rolRepository.findById(id)
                .map(this::convertirAResponse)
                .orElse(null);
    }

    public RolResponse actualizar(Integer id, RolRequest request) {
        Rol rol = rolRepository.findById(id).orElse(null);
        if (rol == null) {
            return null;
        }
        rol.setNombreRol(request.getNombreRol());
        return convertirAResponse(rolRepository.save(rol));
    }

    public boolean eliminar(Integer id) {
        if (!rolRepository.existsById(id)) {
            return false;
        }
        rolRepository.deleteById(id);
        return true;
    }

    private RolResponse convertirAResponse(Rol rol) {
        RolResponse response = new RolResponse();
        response.setIdRol(rol.getIdRol());
        response.setNombreRol(rol.getNombreRol());
        response.setCreadoEn(rol.getCreadoEn());
        return response;
    }
}
