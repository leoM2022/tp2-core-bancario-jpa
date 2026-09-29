package com.example.demo.service.impl;

import com.example.demo.dto.CuentaBancariaRequestDto;
import com.example.demo.dto.CuentaBancariaResponseDto;
import com.example.demo.exception.*;
import com.example.demo.mapper.CuentaBancariaMapper;
import com.example.demo.model.*;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.CuentaBancariaRepository;
import com.example.demo.service.CuentaBancariaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuentaBancariaServiceImpl implements CuentaBancariaService {

    private final CuentaBancariaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public CuentaBancaria depositar(UUID idCuenta, BigDecimal monto) {
        log.info("Iniciando deposito en cuenta ID: {} por monto: {}", idCuenta, monto);

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Operacion rechazada: Monto invalido ({})", monto);
            throw new OperacionInvalidaException("El monto a depositar debe ser superior a cero.");
        }

        CuentaBancaria cuenta = obtenerPorId(idCuenta);

        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            log.error("Operacion rechazada: La cuenta {} se encuentra en estado {}", idCuenta, cuenta.getEstado());
            throw new CuentaInactivaException("La cuenta no se encuentra activa para operar.");
        }

        cuenta.setSaldoOperativo(cuenta.getSaldoOperativo().add(monto));
        return cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional
    public CuentaBancaria extraer(UUID idCuenta, BigDecimal monto) {
        log.info("Iniciando extraccion en cuenta ID: {} por monto: {}", idCuenta, monto);

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Operacion rechazada: Monto invalido ({})", monto);
            throw new OperacionInvalidaException("El monto a extraer debe ser superior a cero.");
        }

        CuentaBancaria cuenta = obtenerPorId(idCuenta);

        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            log.error("Operacion rechazada: La cuenta {} se encuentra en estado {}", idCuenta, cuenta.getEstado());
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
            throw new SaldoInsuficienteException("Fondos insuficientes para efectuar la extraccion.");
        }

        cuenta.setSaldoOperativo(cuenta.getSaldoOperativo().subtract(monto));
        return cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaBancaria obtenerPorCbu(String cbu) {
        return cuentaRepository.findByCbu(cbu)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con CBU: " + cbu));
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaBancaria obtenerPorId(UUID idCuenta) {
        return cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con ID: " + idCuenta));
    }

    @Override
    @Transactional
    public CuentaBancariaResponseDto aperturarCuentaDto(CuentaBancariaRequestDto requestDto) {
        // ... (validaciones de CBU y Alias iguales) ...

        Cliente clienteTitular = clienteRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay clientes registrados en la base de datos."));

        CajaAhorro nuevaCuenta = new CajaAhorro();
        nuevaCuenta.setCbu(requestDto.getCbu().trim());
        nuevaCuenta.setAlias(requestDto.getAlias().trim());
        nuevaCuenta.setSaldoOperativo(requestDto.getSaldoOperativo());
        nuevaCuenta.setEstado(EstadoCuenta.ACTIVA);
        nuevaCuenta.setCliente(clienteTitular);
        nuevaCuenta.setTransacciones(new ArrayList<>());

        nuevaCuenta.setTasaInteresAnual(new BigDecimal("0.00"));
        nuevaCuenta.setCupoEntero(5);

        CuentaBancaria guardada = cuentaRepository.saveAndFlush(nuevaCuenta);

        return CuentaBancariaMapper.toResponseDto(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaBancariaResponseDto consultarDetallePorCbuDto(String cbu) {
        log.info("Consultando cuenta via DTO para CBU: {}", cbu);
        CuentaBancaria cuenta = obtenerPorCbu(cbu);
        return CuentaBancariaMapper.toResponseDto(cuenta);
    }
}