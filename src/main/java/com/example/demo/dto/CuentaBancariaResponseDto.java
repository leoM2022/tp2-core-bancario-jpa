package com.example.demo.dto;

import com.example.demo.model.EstadoCuenta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representativo de la respuesta tras consultar u operar una cuenta bancaria.
 * <p>
 * Evita ciclos de serialización infinita hacia la entidad {@code Cliente} o las colecciones de transacciones.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaBancariaResponseDto {

    /**
     * Identificador UUID canónico de la cuenta bancaria.
     */
    private UUID idCuentaBancaria;

    /**
     * Clave Bancaria Uniforme (CBU).
     */
    private String cbu;

    /**
     * Alias alfanumérico público.
     */
    private String alias;

    /**
     * Saldo líquido disponible tras la operación.
     */
    private BigDecimal saldoOperativo;

    /**
     * Estado operativo de la cuenta (ACTIVA, SUSPENDIDA, BLOQUEADA).
     */
    private EstadoCuenta estado;

    /**
     * Discriminador polimórfico del producto (CAJA_AHORRO o CUENTA_CORRIENTE).
     */
    private String tipoCuenta;

    /**
     * Identificador UUID del cliente titular de la cuenta.
     */
    private UUID clienteId;
}