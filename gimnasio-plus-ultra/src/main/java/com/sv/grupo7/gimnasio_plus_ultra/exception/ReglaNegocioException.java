package com.sv.grupo7.gimnasio_plus_ultra.exception;

import org.springframework.http.HttpStatus;

public class ReglaNegocioException extends GimnasioException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }

    @Override
    public HttpStatus getEstadoHttp() {
        return HttpStatus.CONFLICT;
    }
}
