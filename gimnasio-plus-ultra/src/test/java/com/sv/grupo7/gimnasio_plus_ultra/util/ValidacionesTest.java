package com.sv.grupo7.gimnasio_plus_ultra.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.sv.grupo7.gimnasio_plus_ultra.exception.DatosInvalidosException;

class ValidacionesTest {

    @Test
    void idPresenteSeDevuelveSinCambios() {
        assertEquals(5, Validaciones.idObligatorio(5, "idEstado"));
    }

    @Test
    void idNuloLanzaDatosInvalidosConElNombreDelCampo() {
        DatosInvalidosException error = assertThrows(DatosInvalidosException.class,
                () -> Validaciones.idObligatorio(null, "idEstado"));

        assertEquals("El campo idEstado es obligatorio", error.getMessage());
    }
}
