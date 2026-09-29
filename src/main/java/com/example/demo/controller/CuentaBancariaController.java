package com.example.demo.controller;

import com.example.demo.dto.CuentaBancariaRequestDto;
import com.example.demo.dto.CuentaBancariaResponseDto;
import com.example.demo.service.CuentaBancariaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la administración y consulta de cuentas bancarias.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/cuentas")
@RequiredArgsConstructor
public class CuentaBancariaController {

    private final CuentaBancariaService cuentaBancariaService;

    /**
     * Endpoint para aperturar una cuenta bancaria asociada.
     * Retorna 201 CREATED.
     */
    @PostMapping
    public ResponseEntity<CuentaBancariaResponseDto> aperturarCuenta(@Valid @RequestBody CuentaBancariaRequestDto requestDto) {
        log.info("Petición REST recibida: Apertura de cuenta con CBU {}", requestDto.getCbu());
        CuentaBancariaResponseDto response = cuentaBancariaService.aperturarCuentaDto(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para consultar el detalle de una cuenta bancaria y su saldo actual por CBU.
     * Retorna 200 OK.
     */
    @GetMapping("/{cbu}")
    public ResponseEntity<CuentaBancariaResponseDto> consultarPorCbu(@PathVariable String cbu) {
        log.info("Petición REST recibida: Consulta de cuenta por CBU {}", cbu);
        CuentaBancariaResponseDto response = cuentaBancariaService.consultarDetallePorCbuDto(cbu);
        return ResponseEntity.ok(response);
    }
}