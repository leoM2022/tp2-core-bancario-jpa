package com.example.demo.service.impl;

import com.example.demo.dto.ClienteRequestDto;
import com.example.demo.dto.ClienteResponseDto;
import com.example.demo.exception.RecursoDuplicadoException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.mapper.ClienteMapper;
import com.example.demo.model.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementación transaccional del servicio de aplicación {@link ClienteService}.
 * <p>
 * Centraliza la orquestación de operaciones de negocio para la gestión de clientes,
 * garantizando integridad transaccional ACID, inmutabilidad de componentes y auditoría vía logs.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see ClienteService
 * @see ClienteRepository
 * @see ClienteMapper
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ClienteResponseDto registrarClienteDto(ClienteRequestDto requestDto) {
        log.info("Procesando registro de cliente vía DTO con CUIL: {}", requestDto.getCuil());

        String cuilSanitizado = requestDto.getCuil().trim();
        String emailSanitizado = requestDto.getEmail().trim().toLowerCase();

        validarUnicidad(cuilSanitizado, emailSanitizado);

        Cliente nuevoCliente = ClienteMapper.toEntity(requestDto);
        Cliente clienteGuardado = clienteRepository.saveAndFlush(nuevoCliente);

        log.info("Cliente registrado exitosamente vía DTO con UUID: {}", clienteGuardado.getId());
        return ClienteMapper.toResponseDto(clienteGuardado);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Cliente crearCliente(Cliente cliente) {
        log.info("Validando alta directa de entidad Cliente con CUIL: {}", cliente.getCuil());

        String cuilSanitizado = cliente.getCuil().trim();
        String emailSanitizado = cliente.getEmail().trim().toLowerCase();

        validarUnicidad(cuilSanitizado, emailSanitizado);

        cliente.setCuil(cuilSanitizado);
        cliente.setEmail(emailSanitizado);

        Cliente guardado = clienteRepository.saveAndFlush(cliente);
        log.info("Entidad Cliente persistida exitosamente con UUID: {}", guardado.getId());
        return guardado;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Cliente obtenerClientePorId(UUID id) {
        log.debug("Ejecutando consulta de cliente por UUID: {}", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Búsqueda infructuosa: No existe cliente con UUID: {}", id);
                    return new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Cliente obtenerClientePorCuil(String cuil) {
        String cuilSanitizado = cuil.trim();
        log.debug("Ejecutando consulta de cliente por CUIL: {}", cuilSanitizado);
        return clienteRepository.findByCuil(cuilSanitizado)
                .orElseThrow(() -> {
                    log.warn("Búsqueda infructuosa: No existe cliente con CUIL: {}", cuilSanitizado);
                    return new RecursoNoEncontradoException("Cliente no encontrado con CUIL: " + cuilSanitizado);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> listarPaginado(Pageable pageable) {
        log.debug("Consultando listado paginado de clientes. Página: {}, Tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        return clienteRepository.findAll(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void eliminarCliente(UUID id) {
        log.info("Iniciando baja física del cliente con UUID: {}", id);
        Cliente cliente = obtenerClientePorId(id);
        clienteRepository.delete(cliente);
        log.info("Cliente con UUID {} eliminado exitosamente del repositorio", id);
    }

    /**
     * Comprueba las invariantes de negocio de unicidad fiscal y de correo de contacto.
     *
     * @param cuil CUIL fiscal a validar.
     * @param email Correo electrónico a validar.
     * @throws RecursoDuplicadoException Si alguno de los datos ya existe en la base relacional.
     */
    private void validarUnicidad(String cuil, String email) {
        if (clienteRepository.existsByCuil(cuil)) {
            log.error("Regla de negocio infringida: El CUIL {} ya se encuentra registrado", cuil);
            throw new RecursoDuplicadoException("El CUIL " + cuil + " ya se encuentra registrado en el sistema.");
        }

        if (clienteRepository.existsByEmail(email)) {
            log.error("Regla de negocio infringida: El correo {} ya se encuentra registrado", email);
            throw new RecursoDuplicadoException("El correo electrónico " + email + " ya está en uso.");
        }
    }
}