package com.example.demo.controller;

import com.example.demo.dto.TransaccionRequestDto;
import com.example.demo.dto.TransaccionResponseDto;
import com.example.demo.service.TransaccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la orquestación de operaciones monetarias y transferencias.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    /**
     * Endpoint para ejecutar una transferencia monetaria entre dos cuentas.
     * Retorna 200 OK.
     */
    @PostMapping("/transferir")
    public ResponseEntity<TransaccionResponseDto> transferir(@Valid @RequestBody TransaccionRequestDto requestDto) {
        log.info("Petición REST recibida: Transferencia monetaria por monto {}", requestDto.getMonto());
        TransaccionResponseDto response = transaccionService.procesarTransferenciaDto(requestDto);
        return ResponseEntity.ok(response);
    }
}