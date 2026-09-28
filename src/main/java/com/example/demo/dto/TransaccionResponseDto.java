package com.example.demo.dto;

import com.example.demo.model.CuentaBancaria;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Trasnfer Object para la exposición pública de la entidad {@link com.example.demo.model.Transaccion}.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransaccionResponseDto {
    /**
     * Identificador Único de la transacción.
     */
    private UUID idTransaccion;

    /**
     * Cuenta Bancaria que emite la transacción
     */
    private CuentaBancaria cuentaOrigen;

    /**
     * Cuenta Bancaria que recibe la transacción.
     */
    private CuentaBancaria cuentaDestino;

    /**
     * Monto de la transacción en BigDecimal.
     */
    private BigDecimal monto;

    /**
     * Fecha de emisión de la transacción.
     */
    private LocalDateTime fechaCreacion;
}
