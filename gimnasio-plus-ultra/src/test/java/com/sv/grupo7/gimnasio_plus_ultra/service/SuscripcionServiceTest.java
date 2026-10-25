package com.sv.grupo7.gimnasio_plus_ultra.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sv.grupo7.gimnasio_plus_ultra.dto.SuscripcionRequest;
import com.sv.grupo7.gimnasio_plus_ultra.dto.SuscripcionResponse;
import com.sv.grupo7.gimnasio_plus_ultra.exception.DatosInvalidosException;
import com.sv.grupo7.gimnasio_plus_ultra.exception.RecursoNoEncontradoException;
import com.sv.grupo7.gimnasio_plus_ultra.exception.ReglaNegocioException;
import com.sv.grupo7.gimnasio_plus_ultra.model.Cliente;
import com.sv.grupo7.gimnasio_plus_ultra.model.Estado;
import com.sv.grupo7.gimnasio_plus_ultra.model.PlanSuscripcion;
import com.sv.grupo7.gimnasio_plus_ultra.model.Suscripcion;
import com.sv.grupo7.gimnasio_plus_ultra.repository.ClienteRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.EstadoRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.PlanSuscripcionRepository;
import com.sv.grupo7.gimnasio_plus_ultra.repository.SuscripcionRepository;

@ExtendWith(MockitoExtension.class)
class SuscripcionServiceTest {

    @Mock
    private SuscripcionRepository suscripcionRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private PlanSuscripcionRepository planSuscripcionRepository;
    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private SuscripcionService suscripcionService;

    private Estado activo;
    private Cliente cliente;
    private PlanSuscripcion planMensual;

    @BeforeEach
    void preparar() {
        activo = crearEstado("Activo");

        cliente = new Cliente();
        cliente.setIdCliente(1);
        cliente.setEstado(activo);

        planMensual = new PlanSuscripcion();
        planMensual.setIdPlanSuscripcion(1);
        planMensual.setNombreSuscripcion("Membresía Plus Ultra Black");
        planMensual.setTipoPlan("MENSUAL");
        planMensual.setPrecio(45.0);
        planMensual.setEstado(activo);
    }

    //Casos exitosos

    @Test
    void venderSuscripcionNuevaEmpiezaHoyYDuraUnMes() {
        prepararVentaValida(null); // el cliente no tiene suscripciones previas

        SuscripcionResponse respuesta = suscripcionService.vender(crearVenta(1, 1));

        assertNotNull(respuesta);
        assertEquals(LocalDate.now(), respuesta.getFechaInicio());
        assertEquals(LocalDate.now().plusMonths(1).minusDays(1), respuesta.getFechaVencimiento());
        assertEquals(45.0, respuesta.getPrecioPagado());
        assertTrue(respuesta.isVigente());
        verify(suscripcionRepository).save(any(Suscripcion.class));
    }

    @Test
    void renovarSuscripcionEmpiezaAlDiaSiguienteDelUltimoVencimiento() {
        LocalDate ultimoVencimiento = LocalDate.now().plusDays(10);
        prepararVentaValida(ultimoVencimiento); // aun tiene 10 dias de su plan anterior

        SuscripcionResponse respuesta = suscripcionService.vender(crearVenta(1, 1));

        assertEquals(ultimoVencimiento.plusDays(1), respuesta.getFechaInicio());
        assertFalse(respuesta.isVigente()); // todavia no empieza
    }

    @Test
    void marcarVencidasCambiaElEstadoYDevuelveCuantasCambio() {
        Suscripcion primera = new Suscripcion();
        primera.setEstado(activo);
        Suscripcion segunda = new Suscripcion();
        segunda.setEstado(activo);
        when(suscripcionRepository.buscarVencidas(any(LocalDate.class))).thenReturn(List.of(primera, segunda));
        when(estadoRepository.buscarPorNombre("Vencida")).thenReturn(Optional.of(crearEstado("Vencida")));

        int total = suscripcionService.marcarVencidas();

        assertEquals(2, total);
        assertEquals("Vencida", primera.getEstado().getNombreEstado());
        assertEquals("Vencida", segunda.getEstado().getNombreEstado());
    }

    //Validaciones y casos de error

    @Test
    void venderSinClienteLanzaDatosInvalidos() {
        DatosInvalidosException error = assertThrows(DatosInvalidosException.class,
                () -> suscripcionService.vender(crearVenta(null, 1)));

        assertEquals("Debe indicar el cliente y el plan", error.getMessage());
    }

    @Test
    void venderAClienteInexistenteLanzaRecursoNoEncontrado() {
        when(clienteRepository.buscarParaActualizar(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> suscripcionService.vender(crearVenta(99, 1)));
    }

    @Test
    void venderAClienteInactivoLanzaReglaNegocioYNoGuarda() {
        cliente.setEstado(crearEstado("Inactivo"));
        when(clienteRepository.buscarParaActualizar(1)).thenReturn(Optional.of(cliente));

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> suscripcionService.vender(crearVenta(1, 1)));

        assertEquals("El cliente no está activo", error.getMessage());
        verify(suscripcionRepository, never()).save(any(Suscripcion.class));
    }

    //Metodos de apoyo

    private void prepararVentaValida(LocalDate ultimoVencimiento) {
        when(clienteRepository.buscarParaActualizar(1)).thenReturn(Optional.of(cliente));
        when(planSuscripcionRepository.findById(1)).thenReturn(Optional.of(planMensual));
        when(estadoRepository.buscarPorNombre("Activo")).thenReturn(Optional.of(activo));
        when(suscripcionRepository.ultimaFechaVencimiento(1)).thenReturn(ultimoVencimiento);
        // save devuelve la misma suscripcion que recibe, como haria la BD
        when(suscripcionRepository.save(any(Suscripcion.class))).thenAnswer(invocacion -> invocacion.getArgument(0));
    }

    private SuscripcionRequest crearVenta(Integer idCliente, Integer idPlan) {
        SuscripcionRequest venta = new SuscripcionRequest();
        venta.setIdCliente(idCliente);
        venta.setIdPlanSuscripcion(idPlan);
        return venta;
    }

    private Estado crearEstado(String nombre) {
        Estado estado = new Estado();
        estado.setNombreEstado(nombre);
        return estado;
    }
}
