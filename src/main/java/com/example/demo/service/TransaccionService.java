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
 * Contrato de operaciones transaccionales y auditoría inmutable para transferencias bancarias.
 * <p>
 * Implementa el patrón Application Service coordinando la mutación de saldos
 * entre cuentas origen y destino bajo consistencia ACID.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see Transaccion
 * @see TransaccionRequestDto
 * @see TransaccionResponseDto
 */
public interface TransaccionService {

    /**
     * Orquesta y ejecuta una transferencia de fondos a partir del DTO recibido desde la capa REST.
     *
     * @param requestDto DTO validado con CBUs de origen, destino y monto.
     * @return {@link TransaccionResponseDto} Comprobante inmutable de la operación procesada.
     * @throws OperacionInvalidaException Si los CBUs son idénticos o los datos son inválidos.
     * @throws RecursoNoEncontradoException Si alguna de las cuentas no existe.
     * @throws CuentaInactivaException Si alguna de las cuentas no se encuentra activa.
     * @throws SaldoInsuficienteException Si el saldo más el descubierto no cubren el monto.
     */
    TransaccionResponseDto procesarTransferenciaDto(TransaccionRequestDto requestDto);

    /**
     * Realiza la transferencia de fondos entre dos cuentas identificadas por CBU.
     *
     * @param cbuOrigen CBU de la cuenta que transfiere los fondos.
     * @param cbuDestino CBU de la cuenta que recibe la acreditación.
     * @param monto Importe líquido a transferir.
     * @param concepto Detalle o descripción de la transferencia.
     * @return Instancia persistida de {@link Transaccion} auditada en MySQL.
     * @throws OperacionInvalidaException Si los parámetros monetarios o CBUs no son válidos.
     * @throws RecursoNoEncontradoException Si no se encuentra alguna de las cuentas bancarias.
     * @throws CuentaInactivaException Si alguna de las cuentas está inactiva o bloqueada.
     * @throws SaldoInsuficienteException Si los fondos disponibles son inferiores al monto solicitado.
     */
    Transaccion transferir(String cbuOrigen, String cbuDestino, BigDecimal monto, String concepto);

    /**
     * Obtiene el listado completo de transacciones registradas para auditoría.
     *
     * @return Lista de entidades {@link Transaccion}.
     */
    List<Transaccion> listarTodas();
}