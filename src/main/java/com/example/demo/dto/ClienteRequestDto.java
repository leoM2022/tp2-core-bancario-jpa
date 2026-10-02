package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) para la captura y sanitización de datos en el alta de un Cliente.
 * <p>
 * Aplica Bean Validation (JSR-380) para verificar las entradas enviadas desde clientes REST (Bruno/Postman)
 * antes de interactuar con la lógica del negocio o persistir en la base relacional.
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
public class ClienteRequestDto {

    /**
     * Nombre y apellido completo o denominación física del cliente.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe contener entre 2 y 50 caracteres")
    private String nombre;

    /**
     * Correo electrónico de contacto validado bajo formato estándar de internet.
     */
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico es inválido")
    @Size(max = 60, message = "El correo no puede exceder los 60 caracteres")
    private String email;

    /**
     * Código Único de Identificación Laboral o Tributaria con formato oficial (XX-XXXXXXXX-X).
     */
    @NotBlank(message = "El CUIL es obligatorio")
    @Pattern(regexp = "^(20|23|24|27|30|33)-\\d{8}-\\d$", message = "El CUIL debe respetar el formato oficial XX-XXXXXXXX-X")
    private String cuil;

    /**
     * Línea telefónica con código de área estándar.
     */
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{1,3}[- ]?[0-9]{1,4}[- ]?[0-9]{4,8}$", message = "Formato telefónico inválido. Ejemplo aceptado: +54-388-1234567")
    private String telefono;

    /**
     * Razón social para personas jurídicas o denominación complementaria.
     */
    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 150, message = "La razón social no puede superar los 150 caracteres")
    private String razonSocial;

    /**
     * Domicilio físico o postal declarado por el titular.
     */
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 100, message = "La dirección no puede superar los 100 caracteres")
    private String direccion;
}