package com.sv.grupo7.gimnasio_plus_ultra.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sv.grupo7.gimnasio_plus_ultra.dto.ClienteResponse;
import com.sv.grupo7.gimnasio_plus_ultra.service.ClienteService;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    void obtenerClienteExistenteDevuelve200ConSusDatos() {
        ClienteResponse cliente = new ClienteResponse();
        cliente.setIdCliente(1);
        cliente.setNombres("Roberto Antonio");
        when(clienteService.obtenerPorId(1)).thenReturn(cliente);

        ResponseEntity<ClienteResponse> respuesta = clienteController.obtenerPorId(1);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals("Roberto Antonio", respuesta.getBody().getNombres());
    }

    @Test
    void obtenerClienteInexistenteDevuelve404SinCuerpo() {
        when(clienteService.obtenerPorId(99)).thenReturn(null);

        ResponseEntity<ClienteResponse> respuesta = clienteController.obtenerPorId(99);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertFalse(respuesta.hasBody());
    }

    @Test
    void eliminarClienteExistenteDevuelve204() {
        when(clienteService.eliminar(1)).thenReturn(true);

        assertEquals(HttpStatus.NO_CONTENT, clienteController.eliminar(1).getStatusCode());
    }

    @Test
    void eliminarClienteInexistenteDevuelve404() {
        when(clienteService.eliminar(99)).thenReturn(false);

        assertEquals(HttpStatus.NOT_FOUND, clienteController.eliminar(99).getStatusCode());
    }
}
