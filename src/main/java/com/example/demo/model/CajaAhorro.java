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
 * Entidad destinada a las cuentas bancarias con caja de ahorro, por ello esta entidad hereda de CuentaBancaria
 *
 * @see CuentaBancaria
 * @author Dyevara23 & leoM2022
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DiscriminatorValue("CAJA_AHORRO")
public class CajaAhorro extends CuentaBancaria {

    /**
     * Tasa nominal Anual expresada en porcentaje
     */
    @Column(name = "tasa_interes_anual", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaInteresAnual;

    /**
     * Cupo límite de extracciones mensuales sin costo
     */
    @Column(name = "cupo_limite", nullable = false, updatable = false)
    private int cupoEntero;

}