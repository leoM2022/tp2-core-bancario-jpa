package com.example.demo.mapper;

import com.example.demo.dto.CuentaBancariaRequestDto;
import com.example.demo.dto.CuentaBancariaResponseDto;
import com.example.demo.model.CajaAhorro;
import com.example.demo.model.CuentaBancaria;
import com.example.demo.model.CuentaCorriente;
import com.example.demo.model.EstadoCuenta;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Mapper utilitario responsable de la transformación bidireccional entre la jerarquía
 * polimórfica {@link CuentaBancaria} y sus correspondientes contratos DTO.
 * <p>
 * Implementa el patrón Data Mapper para aislar las reglas de persistencia relacional
 * de la capa de transporte REST, resolviendo el tipo discriminador y evitando la
 * serialización cíclica con {@link com.example.demo.model.Cliente}.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see CuentaBancaria
 * @see CajaAhorro
 * @see CuentaCorriente
 * @see CuentaBancariaRequestDto
 * @see CuentaBancariaResponseDto
 */
public final class CuentaBancariaMapper {

    /**
     * Constructor privado que previene la instanciación de una clase utilitaria.
     */
    private CuentaBancariaMapper() {
        throw new UnsupportedOperationException("CuentaBancariaMapper es una clase utilitaria estática y no debe ser instanciada.");
    }

    /**
     * Transforma un {@link CuentaBancariaRequestDto} en una instancia persistible de {@link CajaAhorro}
     * inicializada con los valores mandatorios para evitar infracciones de integridad.
     *
     * @param dto Contrato de entrada con CBU, Alias y saldo inicial.
     * @return Entidad {@link CajaAhorro} lista para asociar al titular o null si el DTO es nulo.
     */
    public static CajaAhorro toEntity(CuentaBancariaRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return CajaAhorro.builder()
                .cbu(dto.getCbu().trim())
                .alias(dto.getAlias().trim())
                .saldoOperativo(dto.getSaldoOperativo())
                .estado(EstadoCuenta.ACTIVA)
                .transacciones(new ArrayList<>())
                .tasaInteresAnual(new BigDecimal("0.00"))
                .cupoEntero(5)
                .build();
    }

    /**
     * Convierte una instancia concreta de la jerarquía {@link CuentaBancaria} en su correspondiente DTO de respuesta.
     *
     * @param entidad Instancia de {@link CuentaBancaria} persistida en base de datos.
     * @return {@link CuentaBancariaResponseDto} enriquecido con estado, discriminador e ID del titular.
     */
    public static CuentaBancariaResponseDto toResponseDto(CuentaBancaria entidad) {
        if (entidad == null) {
            return null;
        }

        String tipo = "CUENTA_BANCARIA";
        if (entidad instanceof CajaAhorro) {
            tipo = "CAJA_AHORRO";
        } else if (entidad instanceof CuentaCorriente) {
            tipo = "CUENTA_CORRIENTE";
        }

        return CuentaBancariaResponseDto.builder()
                .idCuentaBancaria(entidad.getIdCuentaBancaria())
                .cbu(entidad.getCbu())
                .alias(entidad.getAlias())
                .saldoOperativo(entidad.getSaldoOperativo())
                .estado(entidad.getEstado())
                .tipoCuenta(tipo)
                .clienteId(entidad.getCliente() != null ? entidad.getCliente().getId() : null)
                .build();
    }
}