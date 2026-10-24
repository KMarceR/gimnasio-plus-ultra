package com.sv.grupo7.gimnasio_plus_ultra.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TareasProgramadas implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(TareasProgramadas.class);
    private static final long RETRASO_INICIAL_SEGUNDOS = 10; // primera ejecucion poco despues de arrancar
    private static final long INTERVALO_MINUTOS = 60; // luego cada hora

    private final AsistenciaService asistenciaService;
    private final SuscripcionService suscripcionService;
    private final ScheduledExecutorService planificador = Executors.newScheduledThreadPool(2); // un hilo por tarea

    public TareasProgramadas(AsistenciaService asistenciaService, SuscripcionService suscripcionService) {
        this.asistenciaService = asistenciaService;
        this.suscripcionService = suscripcionService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void iniciar() {
        planificador.scheduleWithFixedDelay(this::cerrarEntradasSinSalida,
                RETRASO_INICIAL_SEGUNDOS, INTERVALO_MINUTOS * 60, TimeUnit.SECONDS);
        planificador.scheduleWithFixedDelay(this::marcarSuscripcionesVencidas,
                RETRASO_INICIAL_SEGUNDOS, INTERVALO_MINUTOS * 60, TimeUnit.SECONDS);
        log.info("Tareas programadas iniciadas (cada {} min)", INTERVALO_MINUTOS);
    }

    private void cerrarEntradasSinSalida() {
        try {
            int total = asistenciaService.cerrarEntradasSinSalida();
            log.info("[{}] Cierre de entradas sin salida: {} registro(s) actualizado(s)",
                    Thread.currentThread().getName(), total);
        } catch (Exception e) {
            log.error("Error en el cierre de entradas sin salida", e);
        }
    }

    private void marcarSuscripcionesVencidas() {
        try {
            int total = suscripcionService.marcarVencidas();
            log.info("[{}] Suscripciones vencidas: {} registro(s) actualizado(s)",
                    Thread.currentThread().getName(), total);
        } catch (Exception e) {
            log.error("Error al marcar suscripciones vencidas", e);
        }
    }

    @Override
    public void destroy() {
        planificador.shutdown();
        try {
            if (!planificador.awaitTermination(10, TimeUnit.SECONDS)) {
                planificador.shutdownNow();
            }
        } catch (InterruptedException e) {
            planificador.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
