package com.example.demo.event;

import com.example.demo.service.NotificacionEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener asíncrono encargado de consumir los eventos de registro de clientes.
 * Se ejecuta en un hilo secundario sin retrasar la respuesta HTTP del cliente.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClienteNotificacionListener {

    private final NotificacionEmailService notificacionEmailService;

    /**
     * Consume el evento de forma asíncrona tras la persistencia del cliente
     * y despacha el correo HTML con el enlace de activación.
     *
     * @param event Datos del cliente registrado.
     */
    @Async
    @EventListener
    public void manejarRegistroCliente(ClienteRegistradoEvent event) {
        log.info("[ASYNC-EVENT] Procesando notificación en Thread: [{}] para: {}",
                Thread.currentThread().getName(), event.getEmail());

        notificacionEmailService.enviarCorreoActivacion(
                event.getEmail(),
                event.getNombre(),
                event.getTokenActivacion()
        );
    }
}