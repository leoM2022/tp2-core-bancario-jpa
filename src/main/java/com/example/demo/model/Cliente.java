package com.example.demo.model;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad de dominio que modela al cliente titular o cotitular de cuentas bancarias.
 * <p>
 * Representa el nodo central del módulo de gestión de clientes (CRM bancario).
 * Administra las relaciones de persistencia y reglas de negocio
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.3.0
 * @see EntidadAuditable
 * @see CuentaBancaria
 * @see EstadoCliente
 */
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class Cliente extends EntidadAuditable {

    /**
     * Clave primaria técnica autogenerada mediante identificador universal único (UUID).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Clave Única de Identificación Tributaria/Laboral (CUIL/CUIT).
     * Formato requerido por AFIP/ANSES: XX-XXXXXXXX-X.
     */
    @NotBlank(message = "El CUIL es obligatorio")
    @Pattern(regexp = "^(20|23|24|27)-\\d{8}-\\d$", message = "El CUIL debe respetar el formato oficial XX-XXXXXXXX-X")
    @Column(name = "cuil", nullable = false, unique = true, length = 13)
    private String cuil;

    /**
     * Nombre y apellido completo o denominación física del titular.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe contener entre 2 y 50 caracteres")
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /**
     * Razón social aplicable a cuentas comerciales o denominación legal complementaria.
     */
    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 150, message = "La razón social no puede exceder los 150 caracteres")
    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    /**
     * Domicilio real o legal declarado por el cliente.
     */
    @NotBlank(message = "La dirección postal es obligatoria")
    @Size(max = 100, message = "La dirección no puede superar los 100 caracteres")
    @Column(name = "direccion", nullable = false, length = 100)
    private String direccion;

    /**
     * Línea telefónica de contacto en formato interurbano/móvil estándar (+54...).
     */
    @NotBlank(message = "El teléfono de contacto es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{1,3}[- ]?[0-9]{1,4}[- ]?[0-9]{4,8}$", message = "Formato telefónico inválido. Ejemplo aceptado: +54-388-1234567")
    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    /**
     * Dirección de correo electrónico validada bajo estructura estándar de internet.
     */
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El correo electrónico debe ser una dirección válida")
    @Column(name = "email", nullable = false, unique = true, length = 60)
    private String email;

    /**
     * Estado operativo y de habilitación del cliente dentro del sistema bancario.
     * Los clientes inician en estado {@link EstadoCliente#PENDIENTE_ACTIVACION}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoCliente estado;

    /**
     * Token criptográfico de activación generado aleatoriamente (formato UUID)
     * para el enlace de confirmación por correo electrónico.
     */
    @Column(name = "token_activacion", unique = true, length = 36)
    private String tokenActivacion;

    /**
     * Marca temporal que fija el límite de vigencia de 24 horas del token de activación.
     */
    @Column(name = "fecha_expiracion_token")
    private LocalDateTime fechaExpiracionToken;

    /**
     * Cuentas bancarias de las cuales este cliente es titular principal.
     * Mapeo bidireccional con cascada total y remoción de registros huérfanos.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CuentaBancaria> cuentas = new ArrayList<>();

    /**
     * Referencia al tutor legal o titular representante en cuentas compartidas.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tutor")
    private Cliente clienteTutor;

    /**
     * Lista de cotitulares asociados a este cliente en su rol de tutor o apoderado.
     */
    @OneToMany(mappedBy = "clienteTutor", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Cliente> cotitulares = new ArrayList<>();
}