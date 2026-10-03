package com.example.demo.service;

/**
 * Contrato de operaciones para la generación y emisión de notificaciones
 * por correo electrónico del sistema bancario.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
public interface NotificacionEmailService {

    /**
     * Genera y envía el correo electrónico con plantilla HTML y enlace de activación.
     *
     * @param destinatario Correo electrónico del cliente.
     * @param nombreCliente Nombre del titular registrado.
     * @param tokenActivacion Token criptográfico generado para la confirmación.
     */
    void enviarCorreoActivacion(String destinatario, String nombreCliente, String tokenActivacion);
}