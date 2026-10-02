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
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Especialización de {@link CuentaBancaria} que habilita acuerdos de sobregiro (giro en descubierto).
 * <p>
 * Contempla costos fijos de mantenimiento periódico y margen de crédito operativo complementario.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see CuentaBancaria
 */
@Entity
@DiscriminatorValue("CUENTA_CORRIENTE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CuentaCorriente extends CuentaBancaria {

    /**
     * Límite de crédito disponible para operar con saldo negativo transitorio.
     */
    @NotNull(message = "El margen de descubierto es obligatorio para la Cuenta Corriente")
    @PositiveOrZero(message = "El margen de descubierto debe ser mayor o igual a cero")
    @Column(name = "margen_descubierto", precision = 15, scale = 2)
    private BigDecimal margenDescubierto;

    /**
     * Cargo fijo deducible por administración mensual del producto.
     */
    @NotNull(message = "El costo de mantenimiento es obligatorio para la Cuenta Corriente")
    @PositiveOrZero(message = "El costo de mantenimiento debe ser mayor o igual a cero")
    @Column(name = "costo_mantenimiento", precision = 10, scale = 2)
    private BigDecimal costoMantenimiento;
}