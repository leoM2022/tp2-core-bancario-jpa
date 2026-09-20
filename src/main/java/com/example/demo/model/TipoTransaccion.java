package com.example.demo.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Enumeracion que representa los estados posibles de una transaccion.
 *
 * @see Transaccion
 * @author Dyevara23 & leoM2022
 */
public enum TipoTransaccion {
    @Enumerated(EnumType.STRING)
    DEPOSITO,
    EXTRACCION,
    TRANSFERENCIA_ENVIADA,
    TRANSFERENCIA_RECIBIDA
}

