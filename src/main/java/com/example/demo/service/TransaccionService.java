package com.example.demo.service;

import com.example.demo.dto.TransaccionRequestDto;
import com.example.demo.dto.TransaccionResponseDto;
import com.example.demo.exception.CuentaInactivaException;
import com.example.demo.exception.OperacionInvalidaException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.SaldoInsuficienteException;
import com.example.demo.model.Transaccion;

import java.math.BigDecimal;
import java.util.List;

/**
 * Contrato de operaciones de negocio para la gestión transaccional, transferencias
 * y registro inmutable de auditoría.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.1.0
 * @since 2026-09-29
 * @see Transaccion
 * @see TransaccionRequestDto
 * @see TransaccionResponseDto
 */
public interface TransaccionService {

    /**
     * Orquesta y ejecuta una transferencia interbancaria a partir de un DTO validado (TP4).
     *
     * <p>Desacopla la capa de exposición web retornando un comprobante inmutable sin entidades JPA.</p>
     *
     * @param requestDto DTO con datos de la transacción (cuentas y monto).
     * @return {@link TransaccionResponseDto} con el comprobante de la transacción asentada.
     * @throws OperacionInvalidaException si los datos requeridos son nulos o inválidos.
     * @throws RecursoNoEncontradoException si alguna de las cuentas no existe.
     * @throws CuentaInactivaException si alguna de las cuentas no se encuentra activa.
     * @throws SaldoInsuficienteException si el emisor no dispone de fondos ni descubierto.
     */
    TransaccionResponseDto procesarTransferenciaDto(TransaccionRequestDto requestDto);

    /**
     * Realiza una transferencia de fondos entre dos cuentas bancarias por CBU directo.
     *
     * @param cbuOrigen CBU de la cuenta emisora.
     * @param cbuDestino CBU de la cuenta receptora.
     * @param monto Importe monetario a transferir.
     * @param concepto Motivo o detalle de la operación.
     * @return Registro de la entidad {@link Transaccion} auditada y persistida.
     * @throws OperacionInvalidaException si montos o CBUs son inválidos.
     * @throws RecursoNoEncontradoException si una cuenta no existe.
     * @throws CuentaInactivaException si una cuenta no está activa.
     * @throws SaldoInsuficienteException si no alcanzan los fondos.
     */
    Transaccion transferir(String cbuOrigen, String cbuDestino, BigDecimal monto, String concepto);

    /**
     * Obtiene el listado completo de transacciones auditadas registradas en el sistema.
     *
     * @return Lista de entidades {@link Transaccion}.
     */
    List<Transaccion> listarTodas();
}