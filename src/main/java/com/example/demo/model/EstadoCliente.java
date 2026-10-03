package com.example.demo.model;
/**
 * Representa los estados del ciclo de vida de un cliente en el sistema financiero.
 *
 * @see Cliente
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 */
public enum EstadoCliente {
    /**
     * El cliente fue registrado pero aún no confirmó el enlace/token enviado a su correo electrónico.
     */
    PENDIENTE_ACTIVACION,

    /**
     * El cliente validó su identidad mediante el token de activación y puede abrir cuentas u operar.
     */
    ACTIVO,

    /**
     * El cliente se encuentra suspendido por el sistema financiero.
     */
    BLOQUEADO
}
