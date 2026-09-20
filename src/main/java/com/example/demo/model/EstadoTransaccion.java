package com.example.demo.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Enumeración que representa a los estados posibles que pasa a lo largo de su ciclo
 * de vida una Transaccion.
 *
 * @see Transaccion
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 */
public enum EstadoTransaccion {
    @Enumerated(EnumType.STRING)
    PENDIENTE,
    COMPLETADA,
    RECHAZADA,
    REVERTIDA
}
