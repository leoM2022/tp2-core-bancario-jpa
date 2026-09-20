package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidad correspondiente a las cuentas corrientes. Estas mismas heredan de CuentaBancaria.
 *
 * @see CuentaBancaria
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("CUENTA_CORRIENTE")
public class CuentaCorriente extends CuentaBancaria {

    /**
     * Margen o giro en descubierto correspondiente a la cuenta.
     */
    @NotNull(message = "El margen de descubierto es obligatorio para la Cuenta Corriente")
    @PositiveOrZero(message = "El margen debe ser positivo o cero")
    @Column(name = "margen_descubierto", precision = 15, scale = 2)
    private BigDecimal margenDescubierto;

    /**
     * Costo mensual del mantenimiento de la cuenta.
     */
    @NotNull(message = "El costo de mantenimiento es obligatorio para la Cuenta Corriente")
    @PositiveOrZero(message = "El costo de mantenimiento debe ser positivo o cero")
    @Column(name = "costo_mantenimiento", precision = 10, scale = 2)
    private BigDecimal costoMantenimiento;

}