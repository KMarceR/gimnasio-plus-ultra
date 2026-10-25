package com.sv.grupo7.gimnasio_plus_ultra.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import com.sv.grupo7.gimnasio_plus_ultra.dto.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    private static final Logger log = LoggerFactory.getLogger(ManejadorGlobalExcepciones.class);

    @ExceptionHandler(GimnasioException.class)
    public ResponseEntity<ErrorResponse> manejarGimnasio(GimnasioException ex, HttpServletRequest request) {
        return construir(ex.getEstadoHttp(), ex.getMessage(), request);
    }

    // Restriccion de la BD: 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> manejarIntegridad(DataIntegrityViolationException ex,
            HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT,
                "No se puede completar la operación: el registro está relacionado con otros datos o ya existe",
                request);
    }

    // MySQL apagado o pool sin conexiones libres: 503
    @ExceptionHandler({ CannotCreateTransactionException.class, DataAccessResourceFailureException.class })
    public ResponseEntity<ErrorResponse> manejarBaseDatos(Exception ex, HttpServletRequest request) {
        log.error("Base de datos no disponible", ex);
        return construir(HttpStatus.SERVICE_UNAVAILABLE,
                "La base de datos no está disponible. Intente de nuevo en unos momentos", request);
    }

    // JSON mal escrito o con tipos incorrectos: 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido(HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no es un JSON válido o tiene tipos de dato incorrectos", request);
    }

    // /api/clientes/abc en lugar de un numero: 400
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarParametro(MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST, "El parámetro '" + ex.getName() + "' tiene un formato inválido",
                request);
    }

    // Ruta que no existe: 404
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> manejarRuta(NoResourceFoundException ex, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, "La ruta solicitada no existe", request);
    }

    // Metodo HTTP equivocado (ej. DELETE donde solo hay GET): 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> manejarMetodo(HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        return construir(HttpStatus.METHOD_NOT_ALLOWED,
                "El método " + ex.getMethod() + " no está permitido en esta ruta", request);
    }

    // Cualquier otro error: 500 sin mostrar detalles internos al cliente
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGeneral(Exception ex, HttpServletRequest request) {
        log.error("Error no controlado en {}", request.getRequestURI(), ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno. Revise la consola del servidor", request);
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus estado, String mensaje, HttpServletRequest request) {
        ErrorResponse cuerpo = new ErrorResponse(LocalDateTime.now(), estado.value(),
                estado.getReasonPhrase(), mensaje, request.getRequestURI());
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
