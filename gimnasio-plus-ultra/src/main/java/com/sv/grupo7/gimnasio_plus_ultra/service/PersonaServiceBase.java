package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.Objects;

import com.sv.grupo7.gimnasio_plus_ultra.dto.PersonaRequest;
import com.sv.grupo7.gimnasio_plus_ultra.exception.DatosInvalidosException;
import com.sv.grupo7.gimnasio_plus_ultra.exception.RecursoDuplicadoException;
import com.sv.grupo7.gimnasio_plus_ultra.model.Persona;
import com.sv.grupo7.gimnasio_plus_ultra.repository.PersonaRepository;

public abstract class PersonaServiceBase {

    private static final String FORMATO_DUI = "\\d{8}-\\d";
    private static final String FORMATO_TELEFONO = "\\d{4}-\\d{4}";
    private static final String FORMATO_EMAIL = "[^@\\s]+@[^@\\s]+\\.[^@\\s]+";

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

    protected void validarFormato(PersonaRequest request) {
        if (request.getDui() == null || !request.getDui().matches(FORMATO_DUI)) {
            throw new DatosInvalidosException("El DUI debe tener el formato 00000000-0");
        }
        if (request.getTelefono() != null && !request.getTelefono().isBlank()
                && !request.getTelefono().matches(FORMATO_TELEFONO)) {
            throw new DatosInvalidosException("El teléfono debe tener el formato 0000-0000");
        }
        if (request.getEmail() == null || !request.getEmail().matches(FORMATO_EMAIL)) {
            throw new DatosInvalidosException("El email no tiene un formato válido");
        }
    }

    protected void validarPersonaNueva(PersonaRequest request) {
        validarFormato(request);
        if (personaRepository.existsByDui(request.getDui())) {
            throw new RecursoDuplicadoException("Ya existe una persona registrada con ese DUI");
        }
        if (personaRepository.existsByEmail(request.getEmail())) {
            throw new RecursoDuplicadoException("Ya existe una persona registrada con ese email");
        }
    }

    protected void validarPersonaEditada(Persona actual, PersonaRequest request) {
        validarFormato(request);
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
