package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) para la exposición pública y serialización JSON de la entidad Cliente.
 * <p>
 * Oculta las colecciones bidireccionales y relaciones reflexivas de persistencia interna,
 * evitando ciclos infinitos de serialización en Jackson.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDto {

    /**
     * Identificador técnico canónico UUID v4 (RFC 4122).
     */
    private UUID id;

    /**
     * Nombre y apellido del titular registrado.
     */
    private String nombre;

    /**
     * Correo electrónico de contacto.
     */
    private String email;

    /**
     * Clave fiscal de negocio del cliente (CUIL/CUIT).
     */
    private String cuil;

    /**
     * Razón social asociada.
     */
    private String razonSocial;

    /**
     * Teléfono de contacto registrado.
     */
    private String telefono;

    /**
     * Domicilio físico registrado.
     */
    private String direccion;

    /**
     * Marca de tiempo de persistencia generada por EntidadAuditable.
     */
    private LocalDateTime fechaCreacion;
}