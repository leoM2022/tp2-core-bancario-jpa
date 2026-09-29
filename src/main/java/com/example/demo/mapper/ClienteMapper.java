package com.example.demo.mapper;

import com.example.demo.dto.ClienteRequestDto;
import com.example.demo.dto.ClienteResponseDto;
import com.example.demo.model.Cliente;

import java.time.LocalDateTime;
import java.util.ArrayList;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static Cliente toEntity(ClienteRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        cliente.setCuil(dto.getCuil());
        cliente.setTelefono(dto.getTelefono());
        cliente.setRazonSocial(dto.getRazonSocial());
        cliente.setDireccion(dto.getDireccion());
        cliente.setCuentas(new ArrayList<>());
        cliente.setCotitulares(new ArrayList<>());

        return cliente;
    }

    public static ClienteResponseDto toResponseDto(Cliente entidad) {
        if (entidad == null) {
            return null;
        }

        return ClienteResponseDto.builder()
                .id(entidad.getId())
                .nombre(entidad.getNombre())
                .email(entidad.getEmail())
                .cuil(entidad.getCuil())
                .fechaCreacion(entidad.getFechaCreacion() != null ? entidad.getFechaCreacion() : LocalDateTime.now())
                .build();
    }
}