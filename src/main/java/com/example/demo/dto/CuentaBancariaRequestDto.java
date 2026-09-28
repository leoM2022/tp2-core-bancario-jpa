package com.example.demo.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;

/**
 * Clase RequestDto de la Entidad {@link com.example.demo.model.CuentaBancaria} con las validaciones correspondientes
 * para cada uno de sus atributos.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuentaBancariaRequestDto {
    /**
     * Clave Bancaria Uniforme correspondiente a la cuenta. Solo puede
     * contener números y tiene longitud de 22 caracteres.
     */
    @Pattern(regexp = "\\d{22}", message = "El CBU solo puede contener números")
    private String cbu;

    /**
     * Alias correspondiente a la cuenta bancaria. Debe tener entre 6 y 20 caracteres y
     * solo puede contener letras, números, guion/es y punto/s.
     */
    @Size(min = 6, max = 20)
    @Pattern(
            regexp = "^[a-zA-Z0-9.-]{6,20}$",
            message = "El alias debe tener entre 6 y 20 caracteres y solo puede contener letras, números, puntos y guiones"
    )
    private String alias;

    /**
     * Saldo operativo de la cuenta bancaria.
     */
    @Digits(integer = 20, fraction = 2, message = "El saldo operativo puede tener un máximo de 20 enteros y 2 decimales.")
    private BigDecimal saldoOperativo;
}
