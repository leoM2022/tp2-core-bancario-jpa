package com.example.demo.mapper;

import com.example.demo.dto.AdherenteResponseDto;
import com.example.demo.dto.ClienteRequestDto;
import com.example.demo.dto.ClienteResponseDto;
import com.example.demo.model.Cliente;

import java.util.ArrayList;

/**
 * Clase utilitaria encargada de la transformación bidireccional entre el modelo de persistencia
 * {@link Cliente} y los objetos de transferencia de datos {@link ClienteRequestDto} y {@link ClienteResponseDto}.
 * <p>
 * Implementa el patrón Data Mapper para aislar el dominio relacional de las capas externas de transporte (REST/JSON),
 * evitando fugas de abstracción y problemas de recursión infinita en serializaciones bidireccionales.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.1.0
 * @see Cliente
 * @see ClienteRequestDto
 * @see ClienteResponseDto
 */
public final class ClienteMapper {

    /**
     * Constructor privado para prevenir la instanciación de una clase utilitaria puramente estática.
     */
    private ClienteMapper() {
        throw novelUnsupportedOperationException();
    }

    private static UnsupportedOperationException novelUnsupportedOperationException() {
        return new UnsupportedOperationException("ClienteMapper es una clase utilitaria estática y no debe ser instanciada.");
    }

    /**
     * Transforma un {@link ClienteRequestDto} proveniente del controlador REST en una entidad {@link Cliente} lista para el dominio.
     *
     * @param dto Objeto de transferencia con los datos validados del cliente.
     * @return Instancia de la entidad {@link Cliente} o null si el DTO es nulo.
     */
    public static Cliente toEntity(ClienteRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return Cliente.builder()
                .nombre(dto.getNombre().trim())
                .email(dto.getEmail().trim().toLowerCase())
                .cuil(dto.getCuil().trim())
                .telefono(dto.getTelefono().trim())
                .razonSocial(dto.getRazonSocial().trim())
                .direccion(dto.getDireccion().trim())
                .rolCliente(dto.getRolCliente())
                .cuentas(new ArrayList<>())
                .cotitulares(new ArrayList<>())
                .build();
    }

    /**
     * Convierte una entidad de dominio persistida {@link Cliente} en su correspondiente {@link ClienteResponseDto}.
     *
     * @param cliente Entidad de dominio que representa al cliente en la base de datos.
     * @return DTO representativo para serialización JSON pública o null si la entidad es nula.
     */
    public static ClienteResponseDto toResponseDto(Cliente cliente) {
        if (cliente == null) return null;
        return ClienteResponseDto.builder()
                .id(cliente.getId())
                .cuil(cliente.getCuil())
                .nombre(cliente.getNombre())
                .razonSocial(cliente.getRazonSocial())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .email(cliente.getEmail())
                .rolCliente(cliente.getRolCliente())
                .estado(cliente.getEstado())
                .build();
    }
    public static AdherenteResponseDto toAdherenteResponseDto(Cliente adherente){
        if (adherente == null) return null;
        return AdherenteResponseDto.builder()
                .id(adherente.getId())
                .nombre(adherente.getNombre())
                .cuil(adherente.getCuil()).
                telefono(adherente.getTelefono())
                .rol(adherente.getRolCliente())
                .direccion(adherente.getDireccion())
                .fechaCreacion(adherente.getFechaCreacion())
                .estado(adherente.getEstado()).
                build();
    }
}