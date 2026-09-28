package com.example.demo.dto;

import com.example.demo.model.CuentaBancaria;
import com.example.demo.model.TipoTransaccion;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Clase RequestDto de la Entidad {@link com.example.demo.model.Transaccion} con las validaciones correspondientes
 * para cada uno de sus atributos.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransaccionRequestDto {
    /**
     * Tipo de transacción: DEPOSITO, EXTRACCIÓN, TRANSFERENCIA_ENVIADA,
     * TRANSFERENCIA_RECIBIDA
     */
    @NotBlank(message = "El campo no puede estar vacío.")
    private TipoTransaccion tipoTransaccion;

    /**
     * Cuenta Origen de la transacción.
     */
    @NotBlank(message = "El campo no puede estar vacío.")
    private CuentaBancaria cuentaOrigen;

    /**
     * Cuenta Destino de la transacción.
     */
    @NotBlank(message = "El campo no puede estar vacío.")
    private CuentaBancaria cuentaDestino;

    /**
     * Monto total de la transacción con un máximo de 9 enteros y 2 decimales.
     */
    @NotBlank(message = "El campo no puede estar vacío.")
    @Digits(integer = 9, fraction = 2, message = "El monto debe tener máximo 9 enteros y 2 decimales.")
    private BigDecimal monto;

    /**
     * Fecha y Hora de la transacción.
     */
    @NotBlank(message = "El campo no puede estar vacío.")
    private LocalDateTime fechaHora;
}