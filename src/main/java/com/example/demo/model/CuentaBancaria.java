package com.example.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Superclase abstracta que define el comportamiento y los atributos comunes
 * de todos los productos de cuenta del sistema financiero.
 * <p>
 * Implementa la estrategia de persistencia polimórfica {@link InheritanceType#SINGLE_TABLE},
 * concentrando las subclases concretas en la tabla {@code cuentas_bancarias} y discriminando
 * el tipo de producto mediante la columna {@code tipo_cuenta}.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see CajaAhorro
 * @see CuentaCorriente
 * @see Cliente
 * @see Transaccion
 */
@Entity
@Table(name = "cuentas_bancarias")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_cuenta", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class CuentaBancaria extends EntidadAuditable {

    /**
     * Clave técnica primaria unívoca generada mediante UUID v4.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idCuentaBancaria;

    /**
     * Clave Bancaria Uniforme (CBU) de 22 dígitos asignada por el estándar financiero.
     */
    @NotBlank(message = "El CBU es obligatorio")
    @Pattern(regexp = "^\\d{22}$", message = "El CBU debe contener exactamente 22 dígitos numéricos")
    @Column(name = "cbu", nullable = false, length = 22, unique = true, updatable = false)
    private String cbu;

    /**
     * Alias alfanumérico único para transferencias interbancarias inmediatas.
     */
    @NotBlank(message = "El alias es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9.-]{6,20}$", message = "El alias debe contener entre 6 y 20 caracteres (letras, números, puntos o guiones)")
    @Column(name = "alias", nullable = false, length = 20, unique = true)
    private String alias;

    /**
     * Saldo líquido disponible para operar en cuenta.
     */
    @NotNull(message = "El saldo operativo inicial es obligatorio")
    @Column(name = "saldo_operativo", nullable = false, precision = 20, scale = 2)
    private BigDecimal saldoOperativo;

    /**
     * Estado operativo de la cuenta en el sistema (ACTIVA, SUSPENDIDA, BLOQUEADA).
     */
    @NotNull(message = "El estado de la cuenta es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuenta", nullable = false)
    private EstadoCuenta estado;

    /**
     * Cliente titular propietario de la cuenta bancaria.
     */
    @NotNull(message = "La cuenta debe pertenecer a un cliente titular")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /**
     * Historial de operaciones y transacciones asociadas a la cuenta.
     */
    @OneToMany(mappedBy = "cuentaBancaria", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Transaccion> transacciones = new ArrayList<>();
}