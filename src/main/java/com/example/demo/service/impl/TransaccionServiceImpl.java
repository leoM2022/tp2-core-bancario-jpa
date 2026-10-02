package com.example.demo.service.impl;

import com.example.demo.dto.TransaccionRequestDto;
import com.example.demo.dto.TransaccionResponseDto;
import com.example.demo.exception.CuentaInactivaException;
import com.example.demo.exception.OperacionInvalidaException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.SaldoInsuficienteException;
import com.example.demo.mapper.TransaccionMapper;
import com.example.demo.model.CuentaBancaria;
import com.example.demo.model.CuentaCorriente;
import com.example.demo.model.EstadoCuenta;
import com.example.demo.model.EstadoTransaccion;
import com.example.demo.model.TipoTransaccion;
import com.example.demo.model.Transaccion;
import com.example.demo.repository.CuentaBancariaRepository;
import com.example.demo.repository.TransaccionRepository;
import com.example.demo.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación transaccional del servicio {@link TransaccionService}.
 * <p>
 * Gestiona transferencias atómicas entre cuentas bancarias, asegurando débitos,
 * créditos correlativos y registro inmutable de movimientos en la tabla de transacciones.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see TransaccionService
 * @see CuentaBancariaRepository
 * @see TransaccionRepository
 * @see TransaccionMapper
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransaccionServiceImpl implements TransaccionService {

    private final CuentaBancariaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TransaccionResponseDto procesarTransferenciaDto(TransaccionRequestDto requestDto) {
        log.info("Procesando transferencia interbancaria recibida vía DTO");

        if (requestDto == null || requestDto.getCbuOrigen() == null || requestDto.getCbuDestino() == null) {
            log.error("Solicitud inválida: Se requieren los CBUs de origen y destino");
            throw new OperacionInvalidaException("Debe especificar el CBU de origen y de destino.");
        }

        String cbuOrigen = requestDto.getCbuOrigen().trim();
        String cbuDestino = requestDto.getCbuDestino().trim();
        BigDecimal monto = requestDto.getMonto();

        Transaccion transaccion = transferir(cbuOrigen, cbuDestino, monto, "Transferencia inmediata vía API REST");
        return TransaccionMapper.toResponseDto(transaccion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Transaccion transferir(String cbuOrigen, String cbuDestino, BigDecimal monto, String concepto) {
        log.info("Iniciando transferencia de ${} desde CBU {} hacia CBU {}", monto, cbuOrigen, cbuDestino);

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Monto inválido detectado para la transferencia: {}", monto);
            throw new OperacionInvalidaException("El monto a transferir debe ser estrictamente mayor a cero.");
        }

        if (cbuOrigen == null || cbuDestino == null || cbuOrigen.trim().equalsIgnoreCase(cbuDestino.trim())) {
            log.error("Conflicto de CBUs: origen y destino coinciden o son nulos (Origen: {}, Destino: {})", cbuOrigen, cbuDestino);
            throw new OperacionInvalidaException("El CBU de origen y destino no pueden ser iguales ni nulos.");
        }

        CuentaBancaria origen = cuentaRepository.findByCbu(cbuOrigen.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta de origen no encontrada con CBU: " + cbuOrigen));

        CuentaBancaria destino = cuentaRepository.findByCbu(cbuDestino.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta de destino no encontrada con CBU: " + cbuDestino));

        if (origen.getEstado() != EstadoCuenta.ACTIVA) {
            log.error("Cuenta origen {} rechazada: Estado actual {}", cbuOrigen, origen.getEstado());
            throw new CuentaInactivaException("La cuenta de origen no se encuentra activa para operar.");
        }

        if (destino.getEstado() != EstadoCuenta.ACTIVA) {
            log.error("Cuenta destino {} rechazada: Estado actual {}", cbuDestino, destino.getEstado());
            throw new CuentaInactivaException("La cuenta de destino no se encuentra activa para operar.");
        }

        BigDecimal fondosDisponibles = origen.getSaldoOperativo();
        if (origen instanceof CuentaCorriente cc && cc.getMargenDescubierto() != null) {
            fondosDisponibles = fondosDisponibles.add(cc.getMargenDescubierto());
        }

        if (fondosDisponibles.compareTo(monto) < 0) {
            log.error("Saldo insuficiente en origen {}. Disponible: {}, Requerido: {}", cbuOrigen, fondosDisponibles, monto);
            throw new SaldoInsuficienteException("Fondos insuficientes para efectuar la transferencia solicitada.");
        }

        origen.setSaldoOperativo(origen.getSaldoOperativo().subtract(monto));
        destino.setSaldoOperativo(destino.getSaldoOperativo().add(monto));

        cuentaRepository.saveAndFlush(origen);
        cuentaRepository.saveAndFlush(destino);

        Transaccion txCompletada = Transaccion.builder()
                .fechaHora(LocalDateTime.now())
                .monto(monto)
                .tipoTransaccion(TipoTransaccion.TRANSFERENCIA_ENVIADA)
                .estadoTransaccion(EstadoTransaccion.COMPLETADA)
                .cuentaOrigen(origen)
                .cuentaDestino(destino)
                .cuentaBancaria(origen)
                .build();

        Transaccion txGuardada = transaccionRepository.saveAndFlush(txCompletada);
        log.info("Transferencia asentada exitosamente con UUID: {}", txGuardada.getIdTransaccion());

        return txGuardada;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Transaccion> listarTodas() {
        log.debug("Recuperando el registro histórico completo de transacciones");
        return transaccionRepository.findAll();
    }
}