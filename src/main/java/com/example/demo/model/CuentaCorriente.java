package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidad correspondiente a las cuentas corrientes. Estas mismas heredan de CuentaBancaria.
 *
 * @see CuentaBancaria
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
    @Column(name = "margen_descubierto", nullable = false, precision = 15, scale = 2)
    private BigDecimal margenDescubierto;

    /**
     * Costo mensual del mantenimiento de la cuenta.
     */
    @Column(name = "costo_mantenimiento", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoMantenimiento;

}