package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Clase RequestDto de la Entidad {@link com.example.demo.model.Cliente} con las validaciones correspondientes
 * para cada uno de sus atributos.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ClienteRequestDto {
    /**
     * Nombre del cliente. Este no debe estar en blanco, tiene un tamaño máximo de 100 caracteres y solo
     * admite letras.
     */
    @NotBlank(message = "El campo no puede estar en blanco.")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres.")
    @Pattern(regexp = "^[A-Z][a-z]$", message = "El nombre solo puede contener letras.")
    private String nombre;

    /**
     * Correo electrónico del cliente validado con {@link Email}
     */
    @NotBlank(message = "El campo no puede estar en blanco.")
    @Email
    private String email;

    /**
     * Clave Única de Identificación Laboral/Tributaria del cliente validado con expresiones regulares
     * de {@link Pattern} en donde se verifica que el valor ingresado tenga el formato XX-XXXXXXXX-X
     */
    @Size(max = 13)
    @NotBlank(message = "El campo no puede estar en blanco.")
    @Pattern(regexp = "^(20|27|23|24)-\\d{8}-\\d$", message = "El cuil debe estar en formato: XX-XXXXXXXX-X")
    private String cuil;

    /**
     * Número telefónico del cliente validado con expresiones regulares de {@link Pattern}
     */
    @Size(max = 15, message = "El número de teléfono no debe superar los 15 caracteres.")
    @NotBlank(message = "El campo no puede estar en blanco.")
    @Pattern(regexp = "^[+54-][1-9]{1,4}-[0-9]", message = "Formato de numero telefónico invalido")
    private String telefono;

    /**
     * Razón social del cliente, la misma no puede superar los 150 caracteres.
     */
    @Size(max = 150, message = "La razón social no puede superar los 150 caracteres.")
    @NotBlank(message = "El campo no puede estar en blanco.")
    private String razonSocial;

    /**
     * Dirección del cliente, la misma no puede superar los 100 caracteres.
     */
    @Size(max = 100, message = "La dirección no puede superar los 100 caracteres.")
    private String direccion;
}
