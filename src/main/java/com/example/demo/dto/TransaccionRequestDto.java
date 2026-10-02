package com.example.demo.dto;

import com.example.demo.model.TipoTransaccion;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) para la emisión y procesamiento de transacciones financieras.
 * <p>
 * Desacopla la capa de presentación de las entidades relacionales, recibiendo las Claves
 * Bancarias Uniformes (CBU) involucradas en lugar de entidades Hibernate anidadas.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see TipoTransaccion
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionRequestDto {

    /**
     * CBU de la cuenta bancaria ordenante o sobre la cual se realiza el débito.
     */
    @NotBlank(message = "El CBU de origen es obligatorio")
    @Pattern(regexp = "^\\d{22}$", message = "El CBU de origen debe contener exactamente 22 dígitos numéricos")
    private String cbuOrigen;

    /**
     * CBU de la cuenta bancaria receptora del crédito (opcional en caso de extracciones o depósitos puros).
     */
    @Pattern(regexp = "^\\d{22}$", message = "El CBU de destino debe contener exactamente 22 dígitos numéricos")
    private String cbuDestino;

    /**
     * Monto total líquido a operar.
     */
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser estrictamente superior a cero")
    @Digits(integer = 15, fraction = 2, message = "El monto admite hasta 15 dígitos enteros y 2 decimales")
    private BigDecimal monto;

    /**
     * Tipo operacional del movimiento a registrar.
     */
    @NotNull(message = "El tipo de transacción es obligatorio")
    private TipoTransaccion tipoTransaccion;
}