package com.sv.grupo7.gimnasio_plus_ultra.exception;

import org.springframework.http.HttpStatus;

public class RecursoNoEncontradoException extends GimnasioException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    @Override
    public HttpStatus getEstadoHttp() {
        return HttpStatus.NOT_FOUND;
    }
}