package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad de dominio que representa un evento u operación financiera inmutable en el sistema.
 * <p>
 * Registra débitos, créditos y transferencias vinculando las cuentas involucradas,
 * el importe liquidado y la marca temporal precisa de ejecución.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see CuentaBancaria
 * @see TipoTransaccion
 * @see EstadoTransaccion
 * @see EntidadAuditable
 */
@Entity
@Table(name = "transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Transaccion extends EntidadAuditable {

    /**
     * Identificador canónico universal de la transacción generado por UUID v4.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idTransaccion;

    /**
     * Marca temporal exacta de cuando se procesó la operación contable.
     */
    @NotNull(message = "La fecha y hora de la transacción es obligatoria")
    @Column(name = "fecha_hora", nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    /**
     * Importe monetario operado. Siempre debe ser estrictamente positivo.
     */
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto de la transacción debe ser mayor a cero")
    @Column(name = "monto", nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    /**
     * Naturaleza funcional del movimiento persistida como VARCHAR representativo.
     */
    @NotNull(message = "El tipo de transacción es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transaccion", nullable = false, length = 30)
    private TipoTransaccion tipoTransaccion;

    /**
     * Estado del ciclo de vida contable de la operación.
     */
    @NotNull(message = "El estado de la transacción es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_transaccion", nullable = false, length = 20)
    private EstadoTransaccion estadoTransaccion;

    /**
     * Cuenta bancaria sobre la cual se asienta el registro en el historial.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuenta_bancaria_id", nullable = false)
    private CuentaBancaria cuentaBancaria;

    /**
     * Cuenta ordenante de la transferencia (opcional si la operación fue un depósito directo).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_origen_id")
    private CuentaBancaria cuentaOrigen;

    /**
     * Cuenta beneficiaria de la transferencia (opcional si la operación fue una extracción directa).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_destino_id")
    private CuentaBancaria cuentaDestino;
}