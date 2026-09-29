package com.example.demo.mapper;

import com.example.demo.dto.CuentaBancariaRequestDto;
import com.example.demo.dto.CuentaBancariaResponseDto;
import com.example.demo.model.CajaAhorro;
import com.example.demo.model.CuentaBancaria;
import com.example.demo.model.EstadoCuenta;

import java.util.ArrayList;

/**
 * Mapper utilitario para la transformación de cuentas bancarias hacia sus DTOs.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.1
 */
public final class CuentaBancariaMapper {

    private CuentaBancariaMapper() {
    }

    /**
     * Convierte el request DTO a una instancia inicial de CuentaBancaria (por defecto CajaAhorro activa).
     */
    public static CuentaBancaria toEntity(CuentaBancariaRequestDto dto) {
        if (dto == null) {
            return null;
        }

        CajaAhorro cuenta = new CajaAhorro();
        cuenta.setCbu(dto.getCbu());
        cuenta.setAlias(dto.getAlias());
        cuenta.setSaldoOperativo(dto.getSaldoOperativo());
        cuenta.setEstado(EstadoCuenta.ACTIVA); // Obligatorio para satisfacer nullable = false
        cuenta.setTransacciones(new ArrayList<>());
        return cuenta;
    }

    /**
     * Mapea la entidad persistida hacia el contrato de respuesta exacto de tu compañero.
     */
    public static CuentaBancariaResponseDto toResponseDto(CuentaBancaria entidad) {
        if (entidad == null) {
            return null;
        }

        return CuentaBancariaResponseDto.builder()
                .idCuentaBancaria(entidad.getIdCuentaBancaria())
                .cbu(entidad.getCbu())
                .alias(entidad.getAlias())
                .build();
    }
}