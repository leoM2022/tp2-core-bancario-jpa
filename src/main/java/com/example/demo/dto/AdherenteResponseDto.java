package com.example.demo.dto;

import com.example.demo.model.EstadoCliente;
import com.example.demo.model.RolCliente;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) para la exposición pública y serialización JSON de los adherentes de una cuenta.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdherenteResponseDto {

    /**
     * Identificador técnico canónico UUID v4
     */
    private UUID id;

    /**
     * Nombre completo del Adherente.
     */
    private String nombre;

    /**
     * Código Único de Identificación Laboral del Adherente.
     */
    private String cuil;

    /**
     * Teléfono de contacto registrado.
     */
    private String telefono;

    /**
     * Rol del Adherente dentro de la cuenta bancaria [CÓNYUGE, HIJO]
     */
    private RolCliente rol;

    /**
     * Domicilio físico registrado.
     */
    private String direccion;

    /**
     * Marca de tiempo de persistencia generada por EntidadAuditable
     */
    private LocalDateTime fechaCreacion;

    /**
     * Estado del cliente.
     */
    private EstadoCliente estado;
}
