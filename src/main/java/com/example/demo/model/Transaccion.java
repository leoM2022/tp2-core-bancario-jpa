package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad destinada a representar cada transaccion realizada por X cuenta bancaria
 * <p>
 *     Tiene las siguientes relaciones con otras Entidades:
 *     <ul>
 *         <li>N:1 con CuentaBancaria: Las multiples transacciones realizadas por una cuenta bancaria.</li>
 *     </ul>
 * </p>
 *
 * @see CuentaBancaria
 * @author Dyevara23 & leoM2022
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transacciones")
public class Transaccion extends EntidadAuditable{

    /**
     * Identificador unico de la transaccion
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idTransaccion;

    /**
     * Fecha y Hora de la transaccion
     */
    @Column(name = "fecha_hora",nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    /**
     * Monto total de la transaccion
     */
    @Column(name = "monto", nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    /**
     * Tipo de transaccion: DEPOSITO, EXTRACCION, TRANSFERENCIA_ENVIADA,
     * TRANSFERENCIA_RECIBIDA
     */
    @Enumerated(EnumType.STRING)

    private TipoTransaccion tipoTransaccion;

    /**
     * Estado de la transaccion: PENDIENTE,
     * COMPLETADA, RECHAZADA, REVERTIDA
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_transaccion", nullable = false)
    private EstadoTransaccion estadoTransaccion;

    /**
     * Cuenta Bancaria sobre la cual se realiza la transaccion.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuenta_bancaria_id", nullable = false)
    private CuentaBancaria cuentaBancaria;

}