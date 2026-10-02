package com.example.demo.controller;

import com.example.demo.dto.ClienteRequestDto;
import com.example.demo.dto.ClienteResponseDto;
import com.example.demo.mapper.ClienteMapper;
import com.example.demo.model.Cliente;
import com.example.demo.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador REST para la exposición pública y administración de la entidad {@link Cliente}.
 * <p>
 * Gestiona el ciclo de vida de peticiones HTTP, validaciones iniciales de contratos de entrada (DTOs)
 * y despacho de códigos semánticos de respuesta HTTP hacia herramientas cliente (Bruno, Postman, Web Apps).
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see ClienteService
 * @see ClienteRequestDto
 * @see ClienteResponseDto
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Da de alta un nuevo cliente titular en el sistema bancario.
     *
     * @param requestDto Payload JSON validado mediante Bean Validation.
     * @return {@link ResponseEntity} con estado 201 Created y el {@link ClienteResponseDto} generado.
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDto> registrarCliente(@Valid @RequestBody ClienteRequestDto requestDto) {
        log.info("Petición REST recibida: Alta de cliente con CUIL {}", requestDto.getCuil());
        ClienteResponseDto response = clienteService.registrarClienteDto(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Recupera un cliente a partir de su identificador canónico universal (UUID).
     *
     * @param id UUID único del cliente.
     * @return {@link ResponseEntity} 200 OK con el DTO público del cliente.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> obtenerPorId(@PathVariable UUID id) {
        log.info("Petición REST recibida: Búsqueda de cliente por UUID {}", id);
        Cliente cliente = clienteService.obtenerClientePorId(id);
        return ResponseEntity.ok(ClienteMapper.toResponseDto(cliente));
    }

    /**
     * Recupera un cliente mediante su Clave Fiscal (CUIL).
     *
     * @param cuil CUIL formal con formato XX-XXXXXXXX-X.
     * @return {@link ResponseEntity} 200 OK con el DTO público del cliente.
     */
    @GetMapping("/cuil/{cuil}")
    public ResponseEntity<ClienteResponseDto> obtenerPorCuil(@PathVariable String cuil) {
        log.info("Petición REST recibida: Búsqueda de cliente por CUIL {}", cuil);
        Cliente cliente = clienteService.obtenerClientePorCuil(cuil);
        return ResponseEntity.ok(ClienteMapper.toResponseDto(cliente));
    }

    /**
     * Obtiene una lista paginada de clientes para optimizar el consumo de memoria.
     *
     * @param pageable Configuración de paginación inyectada por Spring Web (página, tamaño, orden).
     * @return {@link ResponseEntity} 200 OK con la página de clientes mapeada a DTOs.
     */
    @GetMapping
    public ResponseEntity<Page<ClienteResponseDto>> listarClientes(@PageableDefault(size = 10) Pageable pageable) {
        log.info("Petición REST recibida: Listado paginado de clientes (Página: {}, Tamaño: {})",
                pageable.getPageNumber(), pageable.getPageSize());
        Page<ClienteResponseDto> paginaResponse = clienteService.listarPaginado(pageable)
                .map(ClienteMapper::toResponseDto);
        return ResponseEntity.ok(paginaResponse);
    }

    /**
     * Elimina físicamente un cliente registrado a partir de su identificador UUID.
     *
     * @param id UUID del cliente a dar de baja.
     * @return {@link ResponseEntity} 204 No Content tras la baja exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable UUID id) {
        log.info("Petición REST recibida: Baja de cliente con UUID {}", id);
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}