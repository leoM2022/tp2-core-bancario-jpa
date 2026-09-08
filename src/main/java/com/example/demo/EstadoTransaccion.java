package com.example.demo;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum EstadoTransaccion {
    @Enumerated(EnumType.STRING)
    PENDIENTE,
    COMPLETADA,
    RECHAZADA,
    REVERTIDA
}
