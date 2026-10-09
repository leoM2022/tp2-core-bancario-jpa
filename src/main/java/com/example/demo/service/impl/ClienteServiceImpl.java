package com.example.demo.service.impl;

import com.example.demo.dto.AdherenteRequestDto;
import com.example.demo.dto.AdherenteResponseDto;
import com.example.demo.dto.ClienteRequestDto;
import com.example.demo.dto.ClienteResponseDto;
import com.example.demo.event.ClienteRegistradoEvent;
import com.example.demo.exception.OperacionNoPermitidaException;
import com.example.demo.exception.RecursoDuplicadoException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.TokenInvalidoException;
import com.example.demo.mapper.ClienteMapper;
import com.example.demo.model.Cliente;
import com.example.demo.model.EstadoCliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementación transaccional del servicio de aplicación {@link ClienteService}.
 * <p>
 * Centraliza la orquestación de operaciones de negocio para la gestión de clientes,
 * garantizando integridad transaccional ACID, inmutabilidad de componentes y auditoría vía logs.
 * En el marco del TP5, administra el ciclo de vida inicial de los usuarios mediante
 * estados pendientes y tokens de activación con caducidad temporal de 24 horas.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.3.0
 * @see ClienteService
 * @see ClienteRepository
 * @see ClienteMapper
 * @see EstadoCliente
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ApplicationEventPublisher eventPublisher;
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
        nuevoCliente.setCuil(cuilSanitizado);
        nuevoCliente.setEmail(emailSanitizado);

        // Reglas de negocio del TP5: Estado inicial y Token con vigencia de 24 horas
        nuevoCliente.setEstado(EstadoCliente.PENDIENTE_ACTIVACION);
        nuevoCliente.setTokenActivacion(UUID.randomUUID().toString());
        nuevoCliente.setFechaExpiracionToken(LocalDateTime.now().plusHours(24));
        //Publicacion del evento asinrono desacoplado
        Cliente clienteGuardado = clienteRepository.saveAndFlush(nuevoCliente);
        eventPublisher.publishEvent(new ClienteRegistradoEvent(
                clienteGuardado.getId(),
                clienteGuardado.getNombre(),
                clienteGuardado.getEmail(),
                clienteGuardado.getRolCliente(),
                clienteGuardado.getTokenActivacion()
        ));

        log.info("Cliente registrado exitosamente vía DTO en estado PENDIENTE_ACTIVACION. UUID: {}, Token: {}",
                clienteGuardado.getId(), clienteGuardado.getTokenActivacion());
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

        // Reglas de negocio del TP5: Estado inicial y Token con vigencia de 24 horas
        cliente.setEstado(EstadoCliente.PENDIENTE_ACTIVACION);
        cliente.setTokenActivacion(UUID.randomUUID().toString());
        cliente.setFechaExpiracionToken(LocalDateTime.now().plusHours(24));

        Cliente guardado = clienteRepository.saveAndFlush(cliente);
        eventPublisher.publishEvent(new ClienteRegistradoEvent(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getEmail(),
                guardado.getRolCliente(),
                guardado.getTokenActivacion()
        ));
        log.info("Entidad Cliente persistida exitosamente con UUID: {} y Token: {}", guardado.getId(), guardado.getTokenActivacion());
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

    @Override
    @Transactional
    public AdherenteResponseDto registrarAdherente(String cuilTitular, AdherenteRequestDto requestDto) {
        log.info("Procesando registro de adherente vía DTO con CUIL: {}", requestDto.getCuil());
        //Buscar el titular por su CUIL. En caso de no encontrarlo, lanzar excepción correspondiente.
        Cliente titular = clienteRepository.findByCuil(cuilTitular)
                .orElseThrow(() -> new RecursoNoEncontradoException("Titular no encontrado con el CUIL: " + cuilTitular));
        //Un adherente no debería tener sus propios adherentes.
        if(titular.getTitular() != null){
            throw new OperacionNoPermitidaException("Un cliente adherente no puede tener otros adherentes a cargo");
        }
        //Verificación de existencia del cuil del adherente.
        if (clienteRepository.existsByCuil(requestDto.getCuil())) {
            throw new RecursoDuplicadoException("Ya existe un cliente o adherente registrado con el CUIL: " + requestDto.getCuil());
        }
        // Armar un correo único basado en el titular y el CUIL del adherente
        String emailAdherente = "adh." + requestDto.getCuil().replaceAll("\\D", "") + "@banco.local";

        Cliente adherente = new Cliente();
        adherente.setCuil(requestDto.getCuil());
        adherente.setNombre(requestDto.getNombre());
        adherente.setRolCliente(requestDto.getRolCliente());
        adherente.setEstado(titular.getEstado());
        adherente.setTelefono(titular.getTelefono());
        adherente.setEmail(emailAdherente);
        adherente.setDireccion(titular.getDireccion());
        adherente.setRazonSocial(titular.getRazonSocial());

        //El adherente asigna como titular al titular y este lo agrega a su lista de adherentes.
        adherente.setTitular(titular);
        titular.getAdherentes().add(adherente);
        Cliente adherenteGuardado = clienteRepository.save(adherente);

        return ClienteMapper.toAdherenteResponseDto(adherenteGuardado);
    }


    @Override
    @Transactional
    public void desvincularAdherente(String cuilTitular, String cuilAdherente) {
        //Buscar el titular por su CUIL. En caso de no encontrarlo, lanzar excepción correspondiente.
        Cliente titular = clienteRepository.findByCuil(cuilTitular)
                .orElseThrow(() -> new RecursoNoEncontradoException("Titular no encontrado con el CUIL: " + cuilTitular));
        Cliente adherente = clienteRepository.findByCuil(cuilAdherente)
                .orElseThrow(() -> new RecursoNoEncontradoException("Adherente no encontrado con el CUIL: " + cuilAdherente));
        if (adherente.getTitular() == null || !adherente.getTitular().getCuil().equals(cuilTitular)){
            throw new OperacionNoPermitidaException("El adherente no se encuentra asignado al titular con el CUIL: " + cuilTitular);
        }
        adherente.setTitular(null);
        titular.getAdherentes().remove(adherente);
        clienteRepository.save(adherente);
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
    public void eliminarCliente(String cuilCliente) {
        log.info("Iniciando baja física del cliente con CUIL: {}", cuilCliente);
        Cliente cliente = obtenerClientePorCuil(cuilCliente);
        clienteRepository.delete(cliente);
        log.info("Cliente con CUIL {} eliminado exitosamente del repositorio", cuilCliente);
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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ClienteResponseDto activarClientePorToken(String token) {
        log.info("Iniciando proceso de activación con token: {}", token);

        if (token == null || token.trim().isEmpty()) {
            throw new TokenInvalidoException("El token de activación no puede ser nulo o vacío.");
        }

        Cliente cliente = clienteRepository.findByTokenActivacion(token.trim())
                .orElseThrow(() -> {
                    log.warn("Activación fallida: No existe cliente asociado al token provisto: {}", token);
                    return new TokenInvalidoException("El token de activación no existe o es inválido.");
                });

        // 1. Validar si ya está activo
        if (cliente.getEstado() == EstadoCliente.ACTIVO) {
            log.warn("El cliente con CUIL {} ya se encontraba activo previamente", cliente.getCuil());
            return ClienteMapper.toResponseDto(cliente);
        }

        // 2. Validar ventana temporal de 24 horas
        if (cliente.getFechaExpiracionToken() == null ||
                LocalDateTime.now().isAfter(cliente.getFechaExpiracionToken())) {
            log.error("El token de activación ha expirado para el cliente ID: {}", cliente.getId());
            throw new TokenInvalidoException("El token de activación ha expirado. Su validez máxima es de 24 horas.");
        }

        // 3. Pasar a ACTIVO e invalidar el token consumido
        cliente.setEstado(EstadoCliente.ACTIVO);
        cliente.setTokenActivacion(null);
        cliente.setFechaExpiracionToken(null);

        Cliente clienteActualizado = clienteRepository.saveAndFlush(cliente);
        log.info("Cliente con UUID {} activado exitosamente.", clienteActualizado.getId());

        return ClienteMapper.toResponseDto(clienteActualizado);
    }


}