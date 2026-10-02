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
 * Especialización de {@link CuentaBancaria} orientada al ahorro y generación de rendimientos pasivos.
 * <p>
 * Incorpora reglas de negocio sobre cupos de extracción bonificados y tasa nominal anual regulada.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see CuentaBancaria
 */
@Entity
@DiscriminatorValue("CAJA_AHORRO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CajaAhorro extends CuentaBancaria {

    /**
     * Tasa Nominal Anual (TNA) expresada en porcentaje decimal.
     */
    @NotNull(message = "La tasa de interés anual es obligatoria para la Caja de Ahorro")
    @PositiveOrZero(message = "La tasa de interés no puede ser negativa")
    @Column(name = "tasa_interes_anual", precision = 5, scale = 2)
    private BigDecimal tasaInteresAnual;

    /**
     * Cupo máximo mensual de extracciones sin cargo operativo.
     */
    @NotNull(message = "El cupo límite es obligatorio para la Caja de Ahorro")
    @PositiveOrZero(message = "El cupo debe ser mayor o igual a 0")
    @Column(name = "cupo_limite")
    private Integer cupoEntero;
}