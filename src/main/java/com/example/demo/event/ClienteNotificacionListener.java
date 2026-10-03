package com.example.demo.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener asíncrono encargado de consumir los eventos de registro de clientes.
 * Se ejecuta en un hilo independiente para no retrasar la respuesta HTTP del cliente.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Slf4j
@Component
public class ClienteNotificacionListener {

    /**
     * Consume el evento de forma asíncrona tras la persistencia del cliente.
     *
     * @param event Datos del cliente registrado.
     */
    @Async
    @EventListener
    public void manejarRegistroCliente(ClienteRegistradoEvent event) {
        log.info("[ASYNC-EVENT] Capturado evento de registro para cliente: {} ({}) en el Thread: [{}]",
                event.getNombre(), event.getEmail(), Thread.currentThread().getName());

        // Aquí se conectará en el Issue #16 el envío de correo HTML
        log.info("[ASYNC-EVENT] Token recibido para activación: {}", event.getTokenActivacion());
    }
}