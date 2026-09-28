package com.example.demo.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Trasnfer Object para la exposición pública de la entidad {@link com.example.demo.model.Cliente}.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDto {
    /**
     * Identificador único del cliente.
     */
    private UUID id;

    /**
     * Nombre y apellido completo del cliente.
     */
    private String nombre;

    /**
     * Correo electrónico del cliente.
     */
    private String email;

    /**
     * Código Único de Identificación Laboral del cliente.
     */
    private String cuil;

    /**
     * Fecha de creación del cliente.
     */
    private LocalDateTime fechaCreacion;

}