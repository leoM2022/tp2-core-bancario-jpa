package com.example.demo.model;

/**
 * Estados del ciclo de vida operacional y contable de una transacción financiera.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see Transaccion
 */
public enum EstadoTransaccion {
    /**
     * Transacción registrada pendiente de liquidación o compensación.
     */
    PENDIENTE,

    /**
     * Transacción procesada y asentada exitosamente en el libro mayor de MySQL.
     */
    COMPLETADA,

    /**
     * Operación abortada por insuficiencia de fondos o cuenta inactiva.
     */
    RECHAZADA,

    /**
     * Estado complementario para conciliación exitosa.
     */
    EXITOSA,

    /**
     * Operación compensada o revertida por contracargo administrativo.
     */
    REVERTIDA
}