package com.example.demo.mapper;

import com.example.demo.dto.TransaccionResponseDto;
import com.example.demo.model.Transaccion;

/**
 * Mapper utilitario para la transformación de transacciones auditadas a DTO de comprobante.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
public final class TransaccionMapper {

    private TransaccionMapper() {
    }

    /**
     * Mapea la entidad Transaccion a TransaccionResponseDto para la capa REST.
     */
    public static TransaccionResponseDto toResponseDto(Transaccion entidad) {
        if (entidad == null) {
            return null;
        }

        return TransaccionResponseDto.builder()
                .idTransaccion(entidad.getIdTransaccion())
                .cuentaOrigen(entidad.getCuentaOrigen())
                .cuentaDestino(entidad.getCuentaDestino())
                .monto(entidad.getMonto())
                .fechaCreacion(entidad.getFechaHora())
                .build();
    }
}