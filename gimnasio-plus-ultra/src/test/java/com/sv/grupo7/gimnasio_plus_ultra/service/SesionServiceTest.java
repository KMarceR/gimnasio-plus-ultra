package com.sv.grupo7.gimnasio_plus_ultra.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sv.grupo7.gimnasio_plus_ultra.dto.SesionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.SesionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.exception.DatosInvalidosException;
import com.sv.grupo7.gimnasio_plus_ultra.exception.ReglaNegocioException;
import com.sv.grupo7.gimnasio_plus_ultra.model.Cliente;
import com.sv.grupo7.gimnasio_plus_ultra.model.Entrenador;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.Sesion;
import com.sv.grupo7.gimnasio_plus_ultra.repository.ClienteRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EntrenadorRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.SesionRepository;

@ExtendWith(MockitoExtension.class)
class SesionServiceTest {

    @Mock
    private SesionRepository sesionRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private EntrenadorRepository entrenadorRepository;
    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private SesionService sesionService;

    private Cliente cliente;
    private Entrenador entrenador;
    private final LocalDate manana = LocalDate.now().plusDays(1);

    @BeforeEach
    void preparar() {
        cliente = new Cliente();
        cliente.setIdCliente(1);
        cliente.setEstado(crearEstado("Activo"));

        entrenador = new Entrenador();
        entrenador.setIdEntrenador(1);
        entrenador.setEstado(crearEstado("Activo"));
    }

    @Test
    void crearSesionValidaQuedaProgramada() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(entrenadorRepository.buscarParaActualizar(1)).thenReturn(Optional.of(entrenador));
        when(sesionRepository.buscarChoques(manana, LocalTime.of(8, 0), LocalTime.of(9, 0), 1, 1, 0))
                .thenReturn(List.of());
        when(estadoRepository.buscarPorNombre("Programada")).thenReturn(Optional.of(crearEstado("Programada")));
        when(sesionRepository.save(any(Sesion.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        SesionResponse respuesta = sesionService.crear(crearRequest(manana, 8, 9));

        assertEquals("Programada", respuesta.getNombreEstado());
        assertEquals(1, respuesta.getIdEntrenador());
    }

    @Test
    void horaDeFinAntesDeLaDeInicioLanzaDatosInvalidos() {
        DatosInvalidosException error = assertThrows(DatosInvalidosException.class,
                () -> sesionService.crear(crearRequest(manana, 10, 9)));

        assertEquals("La hora de fin debe ser posterior a la hora de inicio", error.getMessage());
        verify(sesionRepository, never()).save(any(Sesion.class));
    }

    @Test
    void choqueDeHorarioDelEntrenadorLanzaReglaNegocio() {
        Sesion existente = crearSesion("Programada", 8, 9);
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(entrenadorRepository.buscarParaActualizar(1)).thenReturn(Optional.of(entrenador));
        when(sesionRepository.buscarChoques(manana, LocalTime.of(8, 30), LocalTime.of(9, 30), 1, 1, 0))
                .thenReturn(List.of(existente));

        SesionRequest request = crearRequest(manana, 8, 9);
        request.setHoraInicio(LocalTime.of(8, 30));
        request.setHoraFin(LocalTime.of(9, 30));

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> sesionService.crear(request));

        assertEquals("El entrenador ya tiene una sesión de 08:00 a 09:00", error.getMessage());
        verify(sesionRepository, never()).save(any(Sesion.class));
    }

    @Test
    void sesionCanceladaNoSePuedeModificar() {
        when(sesionRepository.findById(5)).thenReturn(Optional.of(crearSesion("Cancelada", 8, 9)));

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> sesionService.actualizar(5, crearRequest(manana, 8, 9)));

        assertEquals("No se puede modificar una sesión cancelada", error.getMessage());
    }

    @Test
    void sesionProgramadaNoPuedePasarDirectoACompletada() {
        when(sesionRepository.findById(5)).thenReturn(Optional.of(crearSesion("Programada", 8, 9)));
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(entrenadorRepository.buscarParaActualizar(1)).thenReturn(Optional.of(entrenador));
        when(estadoRepository.findById(5)).thenReturn(Optional.of(crearEstado("Completada")));

        SesionRequest request = crearRequest(manana, 8, 9);
        request.setIdEstado(5);

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> sesionService.actualizar(5, request));

        assertEquals("Una sesión programada no puede pasar a completada", error.getMessage());
    }

    // Metodos de apoyo

    private SesionRequest crearRequest(LocalDate fecha, int horaInicio, int horaFin) {
        SesionRequest request = new SesionRequest();
        request.setIdCliente(1);
        request.setIdEntrenador(1);
        request.setFechaSesion(fecha);
        request.setHoraInicio(LocalTime.of(horaInicio, 0));
        request.setHoraFin(LocalTime.of(horaFin, 0));
        return request;
    }

    private Sesion crearSesion(String estado, int horaInicio, int horaFin) {
        Sesion sesion = new Sesion();
        sesion.setIdSesion(5);
        sesion.setCliente(cliente);
        sesion.setEntrenador(entrenador);
        sesion.setEstado(crearEstado(estado));
        sesion.setFechaSesion(manana);
        sesion.setHoraInicio(LocalTime.of(horaInicio, 0));
        sesion.setHoraFin(LocalTime.of(horaFin, 0));
        return sesion;
    }

    private Estado crearEstado(String nombre) {
        Estado estado = new Estado();
        estado.setNombreEstado(nombre);
        return estado;
    }
}
