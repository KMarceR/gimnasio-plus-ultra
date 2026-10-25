package com.sv.grupo7.gimnasio_plus_ultra.exception;

import org.springframework.http.HttpStatus;

public class DatosInvalidosException extends GimnasioException {

    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }

    @Override
    public HttpStatus getEstadoHttp() {
        return HttpStatus.BAD_REQUEST;
    }
}
