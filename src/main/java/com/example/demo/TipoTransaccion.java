package com.example.demo;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum TipoTransaccion {
    @Enumerated(EnumType.STRING)
    DEPOSITO,
    EXTRACCION,
    TRANSFERENCIA_ENVIADA,
    TRANSFERENCIA_RECIBIDA
}

