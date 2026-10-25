package com.sv.grupo7.gimnasio_plus_ultra.util;

import com.sv.grupo7.gimnasio_plus_ultra.exception.DatosInvalidosException;

public final class Validaciones {

    private Validaciones() {
    }

    // Devuelve el id si viene en la peticion; si no, responde 400 con el nombre del campo
    public static Integer idObligatorio(Integer id, String campo) {
        if (id == null) {
            throw new DatosInvalidosException("El campo " + campo + " es obligatorio");
        }
        return id;
    }
}
