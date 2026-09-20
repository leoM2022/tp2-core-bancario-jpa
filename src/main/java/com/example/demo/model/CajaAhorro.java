package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

/**
 * Entidad destinada a las cuentas bancarias con caja de ahorro, por ello esta entidad hereda de CuentaBancaria
 *
 * @see CuentaBancaria
 * @version 1.0.0
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
    @NotNull(message = "La tasa de interés anual es obligatoria para la Caja de Ahorro")
    @Column(name = "tasa_interes_anual", precision = 5, scale = 2)
    private BigDecimal tasaInteresAnual;

    /**
     * Cupo límite de extracciones mensuales sin costo
     */
    @NotNull(message = "El cupo límite es obligatorio para la Caja de Ahorro")
    @PositiveOrZero(message = "El cupo debe ser mayor o igual a 0")
    @Column(name = "cupo_limite", updatable = false)
    private int cupoEntero;

}