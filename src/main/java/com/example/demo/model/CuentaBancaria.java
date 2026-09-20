package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa a una cuenta bancaria dentro del sistema
 * <p>
 *     Esta misma entidad tiene las siguientes relaciones:
 *     <ul>
 *         <li>N:1 con Cliente: un cliente puede ser titular de mas de una cuenta bancaria.</li>
 *         <li>1:M con Transaccion: Una cuenta bancaria esta asociada a multiples transacciones</li>
 *     </ul>
 * </p>
 *
 * @see Cliente
 * @see Transaccion
 * @author Dyevara23 & leoM2022
 */
@Builder
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_cuenta", discriminatorType = DiscriminatorType.STRING)
@Table(name = "cuentas_bancarias")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class CuentaBancaria extends EntidadAuditable{

    /**
     * Identificador unico de la cuenta bancaria.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idCuentaBancaria;

    /**
     * Clave Bancaria Uniforme correspondiente a la cuenta. Solo puede
     * contener numeros y tiene longitud de 22 caracteres.
     */
    @Column(nullable = false, length = 22, unique = true, updatable = false)
    @Pattern(regexp = "\\d{22}", message = "El CBU solo puede contener números")
    private String cbu;

    /**
     * Alias correspondiente a la cuenta bancaria. Debe tener entre 6 y 20 caracteres y
     * solo puede contener letras, numeros, guion/es y punto/s.
     */
    @Column(nullable = false, length = 20, unique = true)
    @Pattern(
            regexp = "^[a-zA-Z0-9.-]{6,20}$",
            message = "El alias debe tener entre 6 y 20 caracteres y solo puede contener letras, números, puntos y guiones"
    )
    private String alias;

    /**
     * Saldo operativo de la cuenta bancaria.
     */
    @Column(name = "saldo_operativo", nullable = false, precision = 20, scale = 2)
    private BigDecimal saldoOperativo;

    /**
     * Estado actual de la cuenta: ACTIVA, SUSPENDIDA, BLOQUEADA.
     */
    @Column(name = "estado_cuenta", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCuenta estado;

    /**
     * Cliente titular de la cuenta.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /**
     * Historial de las transacciones emitidas por la cuenta bancaria.
     */
    @OneToMany(mappedBy = "cuentaBancaria", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Transaccion> transacciones = new ArrayList<>();

}