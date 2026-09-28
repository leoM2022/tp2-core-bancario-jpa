package com.example.demo.dto;

import lombok.*;

import java.util.UUID;

/**
 * Data Trasnfer Object para la exposición pública de la entidad {@link com.example.demo.model.CuentaBancaria}.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CuentaBancariaResponseDto {
    /**
     * Identificador Único de la cuenta bancaria.
     */
    private UUID idCuentaBancaria;

    /**
     * Clave Bancaria Uniforme de la cuenta.
     */
    private String cbu;

    /**
     * Alias de la cuenta bancaria.
     */
    private String alias;
}