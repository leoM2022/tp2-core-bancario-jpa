package com.example.demo.controller;

import com.example.demo.dto.CuentaBancariaRequestDto;
import com.example.demo.dto.CuentaBancariaResponseDto;
import com.example.demo.mapper.CuentaBancariaMapper;
import com.example.demo.model.CuentaBancaria;
import com.example.demo.service.CuentaBancariaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Controlador REST para la administración y operaciones sobre la jerarquía {@link CuentaBancaria}.
 * <p>
 * Expone endpoints HTTP semánticos para la apertura de cuentas, consultas de saldo,
 * y operaciones transaccionales directas (depósitos y extracciones).
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see CuentaBancariaService
 * @see CuentaBancariaRequestDto
 * @see CuentaBancariaResponseDto
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/cuentas")
@RequiredArgsConstructor
public class CuentaBancariaController {

    private final CuentaBancariaService cuentaBancariaService;

    /**
     * Endpoint para aperturar una nueva cuenta bancaria en el sistema.
     *
     * @param requestDto DTO validado con CBU, alias, saldo inicial y titular opcional.
     * @return {@link ResponseEntity} 201 Created con el DTO público de la cuenta generada.
     */
    @PostMapping
    public ResponseEntity<CuentaBancariaResponseDto> aperturarCuenta(@Valid @RequestBody CuentaBancariaRequestDto requestDto) {
        log.info("Petición REST recibida: Apertura de cuenta con CBU {}", requestDto.getCbu());
        CuentaBancariaResponseDto response = cuentaBancariaService.aperturarCuentaDto(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para consultar el detalle de una cuenta bancaria y su saldo actual a través de su CBU.
     *
     * @param cbu Clave Bancaria Uniforme de 22 dígitos.
     * @return {@link ResponseEntity} 200 OK con el DTO de respuesta.
     */
    @GetMapping("/{cbu}")
    public ResponseEntity<CuentaBancariaResponseDto> consultarPorCbu(@PathVariable String cbu) {
        log.info("Petición REST recibida: Consulta de cuenta por CBU {}", cbu);
        CuentaBancariaResponseDto response = cuentaBancariaService.consultarDetallePorCbuDto(cbu);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para consultar los datos de una cuenta mediante su identificador técnico universal (UUID).
     *
     * @param id UUID único de la cuenta.
     * @return {@link ResponseEntity} 200 OK con los datos mapeados a DTO.
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<CuentaBancariaResponseDto> consultarPorId(@PathVariable UUID id) {
        log.info("Petición REST recibida: Consulta de cuenta por UUID {}", id);
        CuentaBancaria cuenta = cuentaBancariaService.obtenerPorId(id);
        return ResponseEntity.ok(CuentaBancariaMapper.toResponseDto(cuenta));
    }

    /**
     * Endpoint operativo para realizar un depósito monetario en una cuenta activa.
     *
     * @param id Identificador UUID de la cuenta receptora.
     * @param monto Importe líquido a depositar.
     * @return {@link ResponseEntity} 200 OK con el estado y saldo actualizado de la cuenta.
     */
    @PostMapping("/{id}/depositar")
    public ResponseEntity<CuentaBancariaResponseDto> depositar(@PathVariable UUID id, @RequestParam BigDecimal monto) {
        log.info("Petición REST recibida: Depósito en cuenta UUID {} por monto: {}", id, monto);
        CuentaBancaria cuentaActualizada = cuentaBancariaService.depositar(id, monto);
        return ResponseEntity.ok(CuentaBancariaMapper.toResponseDto(cuentaActualizada));
    }

    /**
     * Endpoint operativo para realizar una extracción monetaria con validación de saldo y descubierto.
     *
     * @param id Identificador UUID de la cuenta emisora.
     * @param monto Importe a debitar.
     * @return {@link ResponseEntity} 200 OK con el balance actualizado de la cuenta.
     */
    @PostMapping("/{id}/extraer")
    public ResponseEntity<CuentaBancariaResponseDto> extraer(@PathVariable UUID id, @RequestParam BigDecimal monto) {
        log.info("Petición REST recibida: Extracción en cuenta UUID {} por monto: {}", id, monto);
        CuentaBancaria cuentaActualizada = cuentaBancariaService.extraer(id, monto);
        return ResponseEntity.ok(CuentaBancariaMapper.toResponseDto(cuentaActualizada));
    }
}