package com.example.demo.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) para la solicitud de apertura de cuenta bancaria.
 * <p>
 * Centraliza las validaciones de formato financiero requeridas por el BCRA
 * (CBU numérico de 22 posiciones y Alias alfanumérico estandarizado).
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
public class CuentaBancariaRequestDto {

    /**
     * Clave Bancaria Uniforme (CBU) de 22 dígitos numéricos.
     */
    @NotBlank(message = "El CBU es obligatorio")
    @Pattern(regexp = "^\\d{22}$", message = "El CBU debe contener exactamente 22 dígitos numéricos")
    private String cbu;

    /**
     * Alias unívoco asignado para la cuenta en el sistema financiero.
     */
    @NotBlank(message = "El alias es obligatorio")
    @Size(min = 6, max = 20, message = "El alias debe contener entre 6 y 20 caracteres")
    @Pattern(
            regexp = "^[a-zA-Z0-9.-]{6,20}$",
            message = "El alias solo puede contener caracteres alfanuméricos, puntos y guiones"
    )
    private String alias;

    /**
     * Monto con el cual se realiza la apertura inicial de la cuenta.
     */
    @NotNull(message = "El saldo operativo inicial es obligatorio")
    @PositiveOrZero(message = "El saldo inicial debe ser mayor o igual a cero")
    @Digits(integer = 18, fraction = 2, message = "El saldo admite hasta 18 enteros y 2 decimales")
    private BigDecimal saldoOperativo;

    /**
     * Identificador UUID opcional del cliente titular (si no se envía, el servicio asigna el titular por defecto).
     */
    private UUID clienteId;
}