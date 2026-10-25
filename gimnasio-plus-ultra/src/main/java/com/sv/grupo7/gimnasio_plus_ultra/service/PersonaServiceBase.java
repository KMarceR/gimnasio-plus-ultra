package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.Objects;

import com.sv.grupo7.gimnasio_plus_ultra.dto.PersonaRequest;
import com.sv.grupo7.gimnasio_plus_ultra.exception.RecursoDuplicadoException;
import com.sv.grupo7.gimnasio_plus_ultra.model.Persona;
import com.sv.grupo7.gimnasio_plus_ultra.repository.PersonaRepository;

public abstract class PersonaServiceBase {

    protected final PersonaRepository personaRepository;

    protected PersonaServiceBase(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    protected void copiarDatosPersona(Persona persona, PersonaRequest request) {
        persona.setNombres(request.getNombres());
        persona.setApellidos(request.getApellidos());
        persona.setEmail(request.getEmail());
        persona.setTelefono(request.getTelefono());
        persona.setDireccion(request.getDireccion());
        persona.setGenero(request.getGenero());
        persona.setDui(request.getDui());
        persona.setFechaNacimiento(request.getFechaNacimiento());
    }

    protected void validarPersonaNueva(PersonaRequest request) {
        if (personaRepository.existsByDui(request.getDui())) {
            throw new RecursoDuplicadoException("Ya existe una persona registrada con ese DUI");
        }
        if (personaRepository.existsByEmail(request.getEmail())) {
            throw new RecursoDuplicadoException("Ya existe una persona registrada con ese email");
        }
    }

    protected void validarPersonaEditada(Persona actual, PersonaRequest request) {
        boolean cambioDui = !Objects.equals(actual.getDui(), request.getDui());
        boolean cambioEmail = !Objects.equals(actual.getEmail(), request.getEmail());
        if (cambioDui && personaRepository.existsByDui(request.getDui())) {
            throw new RecursoDuplicadoException("Ya existe una persona registrada con ese DUI");
        }
        if (cambioEmail && personaRepository.existsByEmail(request.getEmail())) {
            throw new RecursoDuplicadoException("Ya existe una persona registrada con ese email");
        }
    }
}
