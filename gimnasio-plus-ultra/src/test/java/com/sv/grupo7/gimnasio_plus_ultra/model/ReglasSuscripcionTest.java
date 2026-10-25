package com.sv.grupo7.gimnasio_plus_ultra.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class ReglasSuscripcionTest {

    @Test
    void planMensualVenceUnDiaAntesDelMismoDiaDelMesSiguiente() {
        assertEquals(1, TipoPlan.MENSUAL.getMeses());
        assertEquals(LocalDate.of(2026, 10, 21),
                TipoPlan.MENSUAL.calcularVencimiento(LocalDate.of(2026, 9, 22)));
    }

    @Test
    void planAnualDuraDoceMeses() {
        assertEquals(12, TipoPlan.ANUAL.getMeses());
        assertEquals(LocalDate.of(2027, 10, 21),
                TipoPlan.ANUAL.calcularVencimiento(LocalDate.of(2026, 10, 22)));
    }

    @Test
    void tipoDePlanInexistenteLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> TipoPlan.valueOf("SEMANAL"));
    }

    @Test
    void suscripcionActivaEsVigenteSoloDentroDeSusFechas() {
        Suscripcion suscripcion = crearSuscripcion("Activo");

        assertTrue(suscripcion.estaVigente(LocalDate.of(2026, 10, 1)));   // primer dia
        assertTrue(suscripcion.estaVigente(LocalDate.of(2026, 10, 31)));  // ultimo dia
        assertFalse(suscripcion.estaVigente(LocalDate.of(2026, 9, 30)));  // antes de empezar
        assertFalse(suscripcion.estaVigente(LocalDate.of(2026, 11, 1)));  // ya vencio
    }

    @Test
    void suscripcionCanceladaNoEsVigenteAunqueEsteDentroDeSusFechas() {
        Suscripcion suscripcion = crearSuscripcion("Cancelada");

        assertFalse(suscripcion.estaVigente(LocalDate.of(2026, 10, 15)));
    }

    private Suscripcion crearSuscripcion(String nombreEstado) {
        Estado estado = new Estado();
        estado.setNombreEstado(nombreEstado);

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setEstado(estado);
        suscripcion.setFechaInicio(LocalDate.of(2026, 10, 1));
        suscripcion.setFechaVencimiento(LocalDate.of(2026, 10, 31));
        return suscripcion;
    }
}