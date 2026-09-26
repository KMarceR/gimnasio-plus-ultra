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

    private RolResponse convertirAResponse(Rol rol) {
        RolResponse response = new RolResponse();
        response.setIdRol(rol.getIdRol());
        response.setNombreRol(rol.getNombreRol());
        response.setCreadoEn(rol.getCreadoEn());
        return response;
    }
}