package com.sv.grupo7.gimnasio_plus_ultra.exception;

import org.springframework.http.HttpStatus;

public abstract class GimnasioException extends RuntimeException {

    protected GimnasioException(String mensaje) {
        super(mensaje);
    }

    public abstract HttpStatus getEstadoHttp();
}
