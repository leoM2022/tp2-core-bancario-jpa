package com.example.demo.dto;

import com.example.demo.model.RolCliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdherenteRequestDto {
    /**
     * Nombre y apellido completo o denominación física del cliente.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe contener entre 2 y 50 caracteres")
    private String nombre;

    /**
     * Código Único de Identificación Laboral o Tributaria con formato oficial (XX-XXXXXXXX-X).
     */
    @NotBlank(message = "El CUIL es obligatorio")
    @Pattern(regexp = "^(20|23|24|27|30|33)-\\d{8}-\\d$", message = "El CUIL debe respetar el formato oficial XX-XXXXXXXX-X")
    private String cuil;


    /**
     * Rol del Adherente dentro de la cuenta bancaria [CÓNYUGE, HIJO]
     */
    @NotNull(message = "El Rol es obligatorio")
    private RolCliente rolCliente;
}
