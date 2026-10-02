package com.example.demo.dto;

import com.example.demo.model.EstadoTransaccion;
import com.example.demo.model.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) para la respuesta de operaciones y auditoría de transacciones.
 * <p>
 * Representación inmutable y segura que serializa los CBUs asociados y el estado final
 * de la operación sin exponer el grafo de entidades de la base de datos.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see TipoTransaccion
 * @see EstadoTransaccion
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionResponseDto {

    /**
     * Identificador único universal (UUID) de la transacción generada.
     */
    private UUID idTransaccion;

    /**
     * CBU de la cuenta de origen.
     */
    private String cbuOrigen;

    /**
     * CBU de la cuenta de destino.
     */
    private String cbuDestino;

    /**
     * Monto asentado en la operación contable.
     */
    private BigDecimal monto;

    /**
     * Naturaleza funcional de la transacción procesada.
     */
    private TipoTransaccion tipoTransaccion;

    /**
     * Estado final del procesamiento transaccional (EXITOSA, COMPLETADA, RECHAZADA).
     */
    private EstadoTransaccion estadoTransaccion;

    /**
     * Marca de tiempo exacta del registro en base de datos.
     */
    private LocalDateTime fechaHora;
}