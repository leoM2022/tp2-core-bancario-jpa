package com.example.demo.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Enumeración que representa los estados posibles de una transacción.
 *
 * @see Transaccion
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 */
public enum TipoTransaccion {
    @Enumerated(EnumType.STRING)
    DEPOSITO,
    EXTRACCION,
    TRANSFERENCIA_ENVIADA,
    TRANSFERENCIA_RECIBIDA
}

