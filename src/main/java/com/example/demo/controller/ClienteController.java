package com.example.demo.controller;

import com.example.demo.dto.ClienteRequestDto;
import com.example.demo.dto.ClienteResponseDto;
import com.example.demo.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la exposición y gestión de clientes.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Endpoint para registrar un nuevo cliente en el sistema.
     * Retorna 201 CREATED junto con el DTO de salida desacoplado.
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDto> registrarCliente(@Valid @RequestBody ClienteRequestDto requestDto) {
        log.info("Petición REST recibida: Alta de cliente con CUIL {}", requestDto.getCuil());
        ClienteResponseDto response = clienteService.registrarClienteDto(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}