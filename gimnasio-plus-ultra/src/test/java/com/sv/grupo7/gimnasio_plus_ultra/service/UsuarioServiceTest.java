package com.sv.grupo7.gimnasio_plus_ultra.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sv.grupo7.gimnasio_plus_ultra.dto.UsuarioRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.UsuarioResponse;
import com.sv.grupo7.gimnasio_plus_ultra.exception.ReglaNegocioException;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.Rol;
import com.sv.grupo7.gimnasio_plus_ultra.model.Usuario;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.PersonaRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.RolRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private EstadoRepository estadoRepository;
    @Mock
    private PersonaRepository personaRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void crearConRolEntrenadorSeRechaza() {
        when(rolRepository.findById(3)).thenReturn(Optional.of(crearRol(3, "Entrenador")));

        assertThrows(ReglaNegocioException.class, () -> usuarioService.crear(crearRequest(3)));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void crearRecepcionistaGuardaElUsuario() {
        when(rolRepository.findById(2)).thenReturn(Optional.of(crearRol(2, "Recepcionista")));
        when(estadoRepository.findById(1)).thenReturn(Optional.of(crearEstado()));
        when(passwordEncoder.encode("Clave123")).thenReturn("hash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        UsuarioResponse response = usuarioService.crear(crearRequest(2));

        assertEquals("Recepcionista", response.getNombreRol());
        assertEquals("01234567-8", response.getNombreUsuario());
    }

    @Test
    void convertirUsuarioEnEntrenadorSeRechaza() {
        when(usuarioRepository.findById(10)).thenReturn(Optional.of(crearUsuario(crearRol(2, "Recepcionista"))));
        when(rolRepository.findById(3)).thenReturn(Optional.of(crearRol(3, "Entrenador")));

        assertThrows(ReglaNegocioException.class, () -> usuarioService.actualizar(10, crearRequest(3)));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void quitarRolEntrenadorSeRechaza() {
        when(usuarioRepository.findById(10)).thenReturn(Optional.of(crearUsuario(crearRol(3, "Entrenador"))));
        when(rolRepository.findById(1)).thenReturn(Optional.of(crearRol(1, "Administrador")));

        assertThrows(ReglaNegocioException.class, () -> usuarioService.actualizar(10, crearRequest(1)));
        verify(usuarioRepository, never()).save(any());
    }

    private UsuarioRequest crearRequest(Integer idRol) {
        UsuarioRequest request = new UsuarioRequest();
        request.setNombres("Ana");
        request.setApellidos("López");
        request.setDui("01234567-8");
        request.setEmail("ana@correo.com");
        request.setTelefono("7777-8888");
        request.setContrasena("Clave123");
        request.setIdRol(idRol);
        request.setIdEstado(1);
        return request;
    }

    private Usuario crearUsuario(Rol rol) {
        Usuario usuario = new Usuario();
        usuario.setDui("01234567-8");
        usuario.setEmail("ana@correo.com");
        usuario.setRol(rol);
        return usuario;
    }

    private Rol crearRol(int id, String nombre) {
        Rol rol = new Rol();
        rol.setIdRol(id);
        rol.setNombreRol(nombre);
        return rol;
    }

    private Estado crearEstado() {
        Estado estado = new Estado();
        estado.setNombreEstado("Activo");
        return estado;
    }
}
