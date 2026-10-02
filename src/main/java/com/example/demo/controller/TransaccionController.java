package com.example.demo.controller;

import com.example.demo.dto.TransaccionRequestDto;
import com.example.demo.dto.TransaccionResponseDto;
import com.example.demo.mapper.TransaccionMapper;
import com.example.demo.service.TransaccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la orquestación, procesamiento y consulta de transacciones financieras.
 * <p>
 * Expone endpoints seguros para transferencias monetarias interbancarias y auditoría
 * contable de movimientos sobre las cuentas del sistema.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see TransaccionService
 * @see TransaccionRequestDto
 * @see TransaccionResponseDto
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    /**
     * Procesa y asienta una transferencia monetaria entre dos cuentas bancarias activas.
     *
     * @param requestDto DTO validado con CBU origen, CBU destino y monto.
     * @return {@link ResponseEntity} 200 OK con el comprobante de la transacción procesada.
     */
    @PostMapping("/transferir")
    public ResponseEntity<TransaccionResponseDto> transferir(@Valid @RequestBody TransaccionRequestDto requestDto) {
        log.info("Petición REST recibida: Transferencia monetaria por monto {} desde CBU {} hacia CBU {}",
                requestDto.getMonto(), requestDto.getCbuOrigen(), requestDto.getCbuDestino());
        TransaccionResponseDto response = transaccionService.procesarTransferenciaDto(requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Recupera el registro histórico de todas las transacciones asentadas en el sistema financiero.
     *
     * @return {@link ResponseEntity} 200 OK con la lista de comprobantes auditados.
     */
    @GetMapping
    public ResponseEntity<List<TransaccionResponseDto>> listarTodas() {
        log.info("Petición REST recibida: Consulta del registro histórico de transacciones");
        List<TransaccionResponseDto> response = transaccionService.listarTodas().stream()
                .map(TransaccionMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(response);
    }
}