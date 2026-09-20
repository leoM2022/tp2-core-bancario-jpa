package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa a un cliente titular de una cuenta bancaria en el sistema bancario
 * <p>
 *      Esta misma mantiene las siguientes relaciones:
 *      <ul>
 *          <li>1:N con cuentas bancarias: un cliente puede tener muchas cuentas.</li>
 *          <li>M:1 con otros clientes: un grupo de clientes comparten una cuenta bancaria y uno de ellos es el tutor.</li>
 *          <li>1:M con otros clientes: un cliente es tutor y representa a un grupo de cotitulares que comparten la misma cuenta.</li>
 *      </ul>
 * </p>
 *
 * @see EntidadAuditable
 * @see CuentaBancaria
 * @author Dyevara23 & leoM2022
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clientes")
@Builder
public class Cliente extends EntidadAuditable{

    /**
     * Identificador unico del cliente
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * CUIL unico del cliente en formato: XX-XXXXXXXX-X
     */
    @Column(nullable = false, unique = true, length = 11)
    @Pattern(regexp = "^(20|27|23|24)-\\d{8}-\\d$", message = "El cuil debe estar en formato: XX-XXXXXXXX-X")
    private String cuil;

    /**
     * Nombre y apellido del cliente
     */
    @Column(nullable = false, length = 50)
    private String nombre;

    /**
     * Razon social del cliente
     */
    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    /**
     * Direccion del cliente (formato: CALLE, ALTURA, BARRIO, LOCALIDAD, PROVINCIA, PAIS)
     */
    @Column(nullable = false, length = 100)
    private String direccion;

    /**
     * Numero telefonico del cliente
     */
    @Column(nullable = false, length = 15)
    @Pattern(regexp = "^[+54-][1-9]{1,4}-[0-9]", message = "Formato de numero telefonico invalido")
    private String telefono;

    /**
     * Correo electronico del cliente
     */
    @Column(nullable = false, length = 30)
    @Pattern(regexp = "^[a-bA-B][a-bA-B0-9.](@gmail.com|@hotmail.com|@outlook.com|@yahoo.com)$", message = "Formato de email invalido")
    private String email;

    /**
     * Multiples cuentas asociadas a un cliente
     * Mapeo bidireccional y eliminacion en cascada y de huerfanos
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CuentaBancaria> cuentas = new ArrayList<>();

    /**
     * RELACION REFLEXIVA: Un grupo de clientes comparten una cuenta bancaria
     * de los cuales uno es el tutor.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tutor")
    private Cliente clienteTutor;

    /**
     * Un grupo de cotitulares son representados por un cliente con el rol de tutor.
     */
    @OneToMany(mappedBy = "clienteTutor", fetch = FetchType.LAZY)
    private List<Cliente> cotitulares = new ArrayList<>();

}