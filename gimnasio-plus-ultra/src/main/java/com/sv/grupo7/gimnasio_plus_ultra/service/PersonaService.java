package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.List;

import com.sv.grupo7.gimnasio_plus_ultra.dto.PersonaRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.PersonaResponse;
import com.sv.grupo7.gimnasio_plus_ultra.model.Persona;
import com.sv.grupo7.gimnasio_plus_ultra.repository.PersonaRepository;

public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    // Al ser Persona abstracta, creamos una subclase anónima temporal para permitir
    // el mapeo base
    public PersonaResponse crear(PersonaRequest personaRequest) {
        Persona persona = new Persona() {
        };

        persona.setNombres(personaRequest.getNombres());
        persona.setApellidos(personaRequest.getApellidos());
        persona.setEmail(personaRequest.getEmail());
        persona.setTelefono(personaRequest.getTelefono());
        persona.setDireccion(personaRequest.getDireccion());
        persona.setGenero(personaRequest.getGenero());
        persona.setDui(personaRequest.getDui());
        persona.setFechaNacimiento(personaRequest.getFechaNacimiento());

        // Se usa personaRepository.save() sobre la entidad Persona, no sobre el repositorio mismo
        Persona nuevaPersona = personaRepository.save(persona);
        return convertirAResponse(nuevaPersona);
    }

    public List<PersonaResponse> obtenerTodos() {
        return personaRepository.findAll().stream()
                .map(this::convertirAResponse).toList();
    }

    public PersonaResponse obtenerPorId(Integer id) {
        return personaRepository.findById(id)
                .map(this::convertirAResponse)
                .orElse(null);
    }

    public PersonaResponse actualizar(Integer id, PersonaRequest personaRequest) {
        Persona personaExistente = personaRepository.findById(id).orElse(null);

        if (personaExistente == null) {
            return null;
        }

        personaExistente.setNombres(personaRequest.getNombres());
        personaExistente.setApellidos(personaRequest.getApellidos());
        personaExistente.setEmail(personaRequest.getEmail());
        personaExistente.setTelefono(personaRequest.getTelefono());
        personaExistente.setDireccion(personaRequest.getDireccion());
        personaExistente.setGenero(personaRequest.getGenero());
        personaExistente.setDui(personaRequest.getDui());
        personaExistente.setFechaNacimiento(personaRequest.getFechaNacimiento());

        Persona personaActualizada = personaRepository.save(personaExistente);
        return convertirAResponse(personaActualizada);
    }

    public boolean eliminar(Integer id) {
        if (!personaRepository.existsById(id)) {
            return false;
        }
        personaRepository.deleteById(id);
        return true;
    }

    // Mapeador auxiliar encapsulado
    private PersonaResponse convertirAResponse(Persona persona) {
        PersonaResponse response = new PersonaResponse();
        response.setIdPersona(persona.getIdPersona());
        response.setNombres(persona.getNombres());
        response.setApellidos(persona.getApellidos());
        response.setEmail(persona.getEmail());
        response.setTelefono(persona.getTelefono());
        response.setDireccion(persona.getDireccion());
        response.setGenero(persona.getGenero());
        response.setDui(persona.getDui());
        response.setFechaNacimiento(persona.getFechaNacimiento());
        response.setCreadoEn(persona.getCreadoEn());
        return response;
    }
}
