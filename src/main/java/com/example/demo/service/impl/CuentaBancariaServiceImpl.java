package com.example.demo.service.impl;

import com.example.demo.dto.CuentaBancariaRequestDto;
import com.example.demo.dto.CuentaBancariaResponseDto;
import com.example.demo.exception.CuentaInactivaException;
import com.example.demo.exception.OperacionInvalidaException;
import com.example.demo.exception.RecursoDuplicadoException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.SaldoInsuficienteException;
import com.example.demo.mapper.CuentaBancariaMapper;
import com.example.demo.model.CajaAhorro;
import com.example.demo.model.Cliente;
import com.example.demo.model.CuentaBancaria;
import com.example.demo.model.CuentaCorriente;
import com.example.demo.model.EstadoCuenta;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.CuentaBancariaRepository;
import com.example.demo.service.CuentaBancariaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Implementación del servicio de aplicación {@link CuentaBancariaService}.
 * <p>
 * Gestiona el ciclo de vida de los productos financieros, asegurando atomicidad transaccional ACID,
 * reglas de evaluación polimórfica para giros en descubierto y sincronización contra MySQL vía repositorios JPA.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see CuentaBancariaService
 * @see CuentaBancariaRepository
 * @see ClienteRepository
 * @see CuentaBancariaMapper
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CuentaBancariaServiceImpl implements CuentaBancariaService {

    private final CuentaBancariaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CuentaBancariaResponseDto aperturarCuentaDto(CuentaBancariaRequestDto requestDto) {
        log.info("Iniciando solicitud de apertura de cuenta con CBU: {} y Alias: {}",
                requestDto.getCbu(), requestDto.getAlias());

        String cbuSanitizado = requestDto.getCbu().trim();
        String aliasSanitizado = requestDto.getAlias().trim();

        if (cuentaRepository.existsByCbu(cbuSanitizado)) {
            log.error("Violación de integridad: El CBU {} ya se encuentra registrado.", cbuSanitizado);
            throw new RecursoDuplicadoException("El CBU " + cbuSanitizado + " ya se encuentra registrado en el sistema.");
        }

        if (cuentaRepository.existsByAlias(aliasSanitizado)) {
            log.error("Violación de integridad: El Alias {} ya se encuentra registrado.", aliasSanitizado);
            throw new RecursoDuplicadoException("El Alias " + aliasSanitizado + " ya se encuentra en uso.");
        }

        Cliente clienteTitular;
        if (requestDto.getClienteId() != null) {
            clienteTitular = clienteRepository.findById(requestDto.getClienteId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe el cliente con ID: " + requestDto.getClienteId()));
        } else {
            clienteTitular = clienteRepository.findAll().stream()
                    .findFirst()
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existen clientes registrados para asociar a la cuenta bancaria."));
        }

        CajaAhorro nuevaCuenta = CuentaBancariaMapper.toEntity(requestDto);
        nuevaCuenta.setCliente(clienteTitular);

        CuentaBancaria guardada = cuentaRepository.saveAndFlush(nuevaCuenta);
        log.info("Cuenta bancaria aperturada exitosamente con UUID: {}", guardada.getIdCuentaBancaria());

        return CuentaBancariaMapper.toResponseDto(guardada);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CuentaBancariaResponseDto consultarDetallePorCbuDto(String cbu) {
        log.debug("Consultando detalle de cuenta vía DTO para CBU: {}", cbu);
        CuentaBancaria cuenta = obtenerPorCbu(cbu);
        return CuentaBancariaMapper.toResponseDto(cuenta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CuentaBancaria depositar(UUID idCuenta, BigDecimal monto) {
        log.info("Iniciando depósito en cuenta ID: {} por monto: {}", idCuenta, monto);

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Operación rechazada: Monto inválido para depósito ({})", monto);
            throw new OperacionInvalidaException("El monto a depositar debe ser superior a cero.");
        }

        CuentaBancaria cuenta = obtenerPorId(idCuenta);

        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            log.error("Operación rechazada: La cuenta {} se encuentra en estado {}", idCuenta, cuenta.getEstado());
            throw new CuentaInactivaException("La cuenta no se encuentra activa para operar.");
        }

        cuenta.setSaldoOperativo(cuenta.getSaldoOperativo().add(monto));
        CuentaBancaria actualizada = cuentaRepository.saveAndFlush(cuenta);
        log.info("Depósito completado exitosamente. Nuevo saldo: {}", actualizada.getSaldoOperativo());

        return actualizada;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CuentaBancaria extraer(UUID idCuenta, BigDecimal monto) {
        log.info("Iniciando extracción en cuenta ID: {} por monto: {}", idCuenta, monto);

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Operación rechazada: Monto inválido para extracción ({})", monto);
            throw new OperacionInvalidaException("El monto a extraer debe ser superior a cero.");
        }

        CuentaBancaria cuenta = obtenerPorId(idCuenta);

        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            log.error("Operación rechazada: La cuenta {} se encuentra en estado {}", idCuenta, cuenta.getEstado());
            throw new CuentaInactivaException("La cuenta no se encuentra activa para operar.");
        }

        BigDecimal fondosDisponibles = cuenta.getSaldoOperativo();

        if (cuenta instanceof CuentaCorriente cc) {
            if (cc.getMargenDescubierto() != null) {
                fondosDisponibles = fondosDisponibles.add(cc.getMargenDescubierto());
            }
        }

        if (fondosDisponibles.compareTo(monto) < 0) {
            log.error("Fondos insuficientes en cuenta {}. Disponibles: {}, Requeridos: {}", idCuenta, fondosDisponibles, monto);
            throw new SaldoInsuficienteException("Fondos insuficientes para efectuar la extracción solicitada.");
        }

        cuenta.setSaldoOperativo(cuenta.getSaldoOperativo().subtract(monto));
        CuentaBancaria actualizada = cuentaRepository.saveAndFlush(cuenta);
        log.info("Extracción completada exitosamente. Saldo remanente: {}", actualizada.getSaldoOperativo());

        return actualizada;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CuentaBancaria obtenerPorCbu(String cbu) {
        String cbuSanitizado = cbu.trim();
        log.debug("Ejecutando búsqueda de cuenta bancaria por CBU: {}", cbuSanitizado);
        return cuentaRepository.findByCbu(cbuSanitizado)
                .orElseThrow(() -> {
                    log.warn("Búsqueda infructuosa: No existe cuenta con CBU: {}", cbuSanitizado);
                    return new RecursoNoEncontradoException("Cuenta no encontrada con CBU: " + cbuSanitizado);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CuentaBancaria obtenerPorId(UUID idCuenta) {
        log.debug("Ejecutando búsqueda de cuenta bancaria por UUID: {}", idCuenta);
        return cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> {
                    log.warn("Búsqueda infructuosa: No existe cuenta con ID: {}", idCuenta);
                    return new RecursoNoEncontradoException("Cuenta no encontrada con ID: " + idCuenta);
                });
    }
}