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
import com.sv.grupo7.gimnasio_plus_ultra.dto.VistaCancelacionResponse;
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
        primera.setCliente(cliente);
        Suscripcion segunda = new Suscripcion();
        segunda.setEstado(activo);
        segunda.setCliente(cliente);
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

    // ---------- Cancelacion ----------

    @Test
    void cancelarPlanVigenteAdelantaElSiguienteParaQueEmpieceHoy() {
        LocalDate hoy = LocalDate.now();
        Suscripcion actual = crearSuscripcion(10, planMensual, hoy.minusDays(10), hoy.plusDays(19));
        Suscripcion siguiente = crearSuscripcion(11, crearPlanAnual(), hoy.plusDays(20),
                hoy.plusDays(20).plusMonths(12).minusDays(1));
        when(suscripcionRepository.findById(10)).thenReturn(Optional.of(actual));
        when(suscripcionRepository.buscarActivasPosteriores(1, actual.getFechaInicio())).thenReturn(List.of(siguiente));
        when(estadoRepository.buscarPorNombre("Cancelada")).thenReturn(Optional.of(crearEstado("Cancelada")));
        when(suscripcionRepository.save(any(Suscripcion.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        suscripcionService.cancelar(10);

        assertEquals("Cancelada", actual.getEstado().getNombreEstado());
        assertEquals(hoy, siguiente.getFechaInicio());
        assertEquals(hoy.plusMonths(12).minusDays(1), siguiente.getFechaVencimiento());
    }

    @Test
    void vistaPreviaDePlanFuturoAdelantaElSiguienteALaFechaDelCanceladoSinGuardar() {
        LocalDate inicioB = LocalDate.now().plusDays(10);
        LocalDate inicioC = inicioB.plusMonths(1);
        Suscripcion planB = crearSuscripcion(20, planMensual, inicioB, inicioC.minusDays(1));
        Suscripcion planC = crearSuscripcion(21, planMensual, inicioC, inicioC.plusMonths(1).minusDays(1));
        when(suscripcionRepository.findById(20)).thenReturn(Optional.of(planB));
        when(suscripcionRepository.buscarActivasPosteriores(1, inicioB)).thenReturn(List.of(planC));

        VistaCancelacionResponse vista = suscripcionService.vistaCancelacion(20);

        assertEquals(1, vista.getCambios().size());
        assertEquals(inicioB, vista.getCambios().get(0).getNuevaFechaInicio());
        assertFalse(vista.isQuedaSinPlanVigente());
        assertEquals(inicioC, planC.getFechaInicio());
        verify(suscripcionRepository, never()).save(any(Suscripcion.class));
    }

    @Test
    void vistaPreviaDelUnicoPlanVigenteAvisaQueQuedaSinPlan() {
        LocalDate hoy = LocalDate.now();
        Suscripcion actual = crearSuscripcion(30, planMensual, hoy.minusDays(5), hoy.plusDays(24));
        when(suscripcionRepository.findById(30)).thenReturn(Optional.of(actual));
        when(suscripcionRepository.buscarActivasPosteriores(1, actual.getFechaInicio())).thenReturn(List.of());

        VistaCancelacionResponse vista = suscripcionService.vistaCancelacion(30);

        assertTrue(vista.getCambios().isEmpty());
        assertTrue(vista.isQuedaSinPlanVigente());
    }

    @Test
    void cancelarSuscripcionYaCanceladaLanzaReglaNegocio() {
        Suscripcion yaCancelada = crearSuscripcion(40, planMensual, LocalDate.now(), LocalDate.now().plusDays(29));
        yaCancelada.setEstado(crearEstado("Cancelada"));
        when(suscripcionRepository.findById(40)).thenReturn(Optional.of(yaCancelada));

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> suscripcionService.cancelar(40));

        assertEquals("Solo se pueden cancelar suscripciones activas", error.getMessage());
        verify(suscripcionRepository, never()).save(any(Suscripcion.class));
    }

    //Metodos de apoyo

    private Suscripcion crearSuscripcion(int id, PlanSuscripcion plan, LocalDate inicio, LocalDate vencimiento) {
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setIdSuscripcion(id);
        suscripcion.setCliente(cliente);
        suscripcion.setPlanSuscripcion(plan);
        suscripcion.setEstado(activo);
        suscripcion.setFechaInicio(inicio);
        suscripcion.setFechaVencimiento(vencimiento);
        return suscripcion;
    }

    private PlanSuscripcion crearPlanAnual() {
        PlanSuscripcion plan = new PlanSuscripcion();
        plan.setIdPlanSuscripcion(3);
        plan.setNombreSuscripcion("Membresía Anual Plus Ultra");
        plan.setTipoPlan("ANUAL");
        plan.setPrecio(450.0);
        plan.setEstado(activo);
        return plan;
    }

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
