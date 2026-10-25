package com.sv.grupo7.gimnasio_plus_ultra.exception;

import org.springframework.http.HttpStatus;

public class RecursoDuplicadoException extends GimnasioException {

    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }

    @Override
    public HttpStatus getEstadoHttp() {
        return HttpStatus.CONFLICT;
    }
}