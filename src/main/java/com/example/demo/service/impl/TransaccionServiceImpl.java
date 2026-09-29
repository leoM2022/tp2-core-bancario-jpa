package com.example.demo.service.impl;

import com.example.demo.dto.TransaccionRequestDto;
import com.example.demo.dto.TransaccionResponseDto;
import com.example.demo.exception.CuentaInactivaException;
import com.example.demo.exception.OperacionInvalidaException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.SaldoInsuficienteException;
import com.example.demo.mapper.TransaccionMapper;
import com.example.demo.model.*;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class TransaccionServiceImpl implements TransaccionService {

    private final CuentaBancariaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    @Override
    @Transactional
    public TransaccionResponseDto procesarTransferenciaDto(TransaccionRequestDto requestDto) {
        log.info("Procesando transferencia interbancaria recibida via DTO");

        if (requestDto == null || requestDto.getCuentaOrigen() == null || requestDto.getCuentaDestino() == null) {
            throw new OperacionInvalidaException("Debe especificar la cuenta de origen y de destino.");
        }

        String cbuOrigen = requestDto.getCuentaOrigen().getCbu();
        String cbuDestino = requestDto.getCuentaDestino().getCbu();
        BigDecimal monto = requestDto.getMonto();

        Transaccion transaccion = transferir(cbuOrigen, cbuDestino, monto, "Transferencia interbancaria");
        return TransaccionMapper.toResponseDto(transaccion);
    }

    @Override
    @Transactional
    public Transaccion transferir(String cbuOrigen, String cbuDestino, BigDecimal monto, String concepto) {
        log.info("Iniciando transferencia de {} desde CBU {} hacia CBU {}", monto, cbuOrigen, cbuDestino);

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OperacionInvalidaException("El monto a transferir debe ser estrictamente mayor a cero.");
        }

        if (cbuOrigen == null || cbuDestino == null || cbuOrigen.trim().equalsIgnoreCase(cbuDestino.trim())) {
            throw new OperacionInvalidaException("El CBU de origen y destino no pueden ser iguales ni nulos.");
        }

        CuentaBancaria origen = cuentaRepository.findByCbu(cbuOrigen.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta de origen no encontrada con CBU: " + cbuOrigen));

        CuentaBancaria destino = cuentaRepository.findByCbu(cbuDestino.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta de destino no encontrada con CBU: " + cbuDestino));

        if (origen.getEstado() != EstadoCuenta.ACTIVA) {
            throw new CuentaInactivaException("La cuenta de origen no se encuentra activa para operar.");
        }
        if (destino.getEstado() != EstadoCuenta.ACTIVA) {
            throw new CuentaInactivaException("La cuenta de destino no se encuentra activa para operar.");
        }

        BigDecimal fondosDisponibles = origen.getSaldoOperativo();
        if (origen instanceof CuentaCorriente cc && cc.getMargenDescubierto() != null) {
            fondosDisponibles = fondosDisponibles.add(cc.getMargenDescubierto());
        }

        if (fondosDisponibles.compareTo(monto) < 0) {
            log.error("Saldo insuficiente en origen {}. Disponible: {}, Requerido: {}", cbuOrigen, fondosDisponibles, monto);

            Transaccion txRechazada = new Transaccion();
            txRechazada.setFechaHora(LocalDateTime.now());
            txRechazada.setMonto(monto);
            txRechazada.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA_ENVIADA);
            txRechazada.setEstadoTransaccion(EstadoTransaccion.RECHAZADA);
            txRechazada.setCuentaOrigen(origen);
            txRechazada.setCuentaDestino(destino);
            txRechazada.setCuentaBancaria(origen);

            transaccionRepository.saveAndFlush(txRechazada);

            throw new SaldoInsuficienteException("Fondos insuficientes para efectuar la transferencia solicitada.");
        }

        origen.setSaldoOperativo(origen.getSaldoOperativo().subtract(monto));
        destino.setSaldoOperativo(destino.getSaldoOperativo().add(monto));

        cuentaRepository.saveAndFlush(origen);
        cuentaRepository.saveAndFlush(destino);

        Transaccion txCompletada = new Transaccion();
        txCompletada.setFechaHora(LocalDateTime.now());
        txCompletada.setMonto(monto);
        txCompletada.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA_ENVIADA);
        txCompletada.setEstadoTransaccion(EstadoTransaccion.COMPLETADA);
        txCompletada.setCuentaOrigen(origen);
        txCompletada.setCuentaDestino(destino);
        txCompletada.setCuentaBancaria(origen);

        return transaccionRepository.saveAndFlush(txCompletada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaccion> listarTodas() {
        return transaccionRepository.findAll();
    }
}