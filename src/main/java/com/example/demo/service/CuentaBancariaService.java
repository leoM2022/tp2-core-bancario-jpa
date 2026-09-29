package com.example.demo.service;

import com.example.demo.dto.CuentaBancariaRequestDto;
import com.example.demo.dto.CuentaBancariaResponseDto;
import com.example.demo.exception.CuentaInactivaException;
import com.example.demo.exception.OperacionInvalidaException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.SaldoInsuficienteException;
import com.example.demo.model.CuentaBancaria;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Interfaz de servicio principal para la gestión de la entidad {@link CuentaBancaria}.
 *
 * <p>Esta interfaz define el contrato de operaciones financieras, abarcando
 * tanto el comportamiento del dominio puro (depósitos y extracciones transaccionales)
 * como los contratos desacoplados de entrada/salida para la API REST (TP4) mediante DTOs.</p>
 *
 * @version 1.1.0
 * @author Dyevara23 & leoM2022
 * @since 2026-09-21
 * @see CuentaBancaria
 * @see CuentaBancariaRequestDto
 * @see CuentaBancariaResponseDto
 */
public interface CuentaBancariaService {

    /**
     * Da de alta y apertura una cuenta bancaria a partir de un DTO validado (TP4).
     *
     * @param requestDto DTO de entrada con CBU, alias y saldo operativo inicial.
     * @return {@link CuentaBancariaResponseDto} con los datos visibles de la cuenta creada.
     * @throws OperacionInvalidaException si el CBU o el Alias ya se encuentran registrados.
     */
    CuentaBancariaResponseDto aperturarCuentaDto(CuentaBancariaRequestDto requestDto);

    /**
     * Consulta el detalle de una cuenta bancaria por su CBU devolviendo un DTO desacoplado (TP4).
     *
     * @param cbu Clave Bancaria Uniforme de 22 dígitos.
     * @return {@link CuentaBancariaResponseDto} correspondiente a la cuenta localizada.
     * @throws RecursoNoEncontradoException si el CBU no existe en el sistema.
     */
    CuentaBancariaResponseDto consultarDetallePorCbuDto(String cbu);

    /**
     * Acredita fondos en una cuenta bancaria específica.
     *
     * @param idCuenta El identificador único (UUID) de la cuenta destino para el depósito.
     * @param monto El importe exacto a depositar.
     * @return La entidad {@link CuentaBancaria} actualizada reflejando el nuevo saldo.
     * @throws OperacionInvalidaException si el monto es nulo o menor/igual a cero.
     * @throws CuentaInactivaException si la cuenta no se encuentra activa.
     * @throws RecursoNoEncontradoException si la cuenta no existe.
     */
    CuentaBancaria depositar(UUID idCuenta, BigDecimal monto);

    /**
     * Debita fondos de una cuenta bancaria específica.
     *
     * @param idCuenta El identificador único (UUID) de la cuenta de origen.
     * @param monto El importe exacto a extraer.
     * @return La entidad {@link CuentaBancaria} actualizada reflejando el nuevo saldo tras la operación.
     * @throws OperacionInvalidaException si el monto es nulo o menor/igual a cero.
     * @throws CuentaInactivaException si la cuenta no se encuentra activa.
     * @throws SaldoInsuficienteException si los fondos disponibles (incluyendo descubierto) son insuficientes.
     * @throws RecursoNoEncontradoException si la cuenta no existe.
     */
    CuentaBancaria extraer(UUID idCuenta, BigDecimal monto);

    /**
     * Recupera una cuenta bancaria utilizando su Clave Bancaria Uniforme (CBU).
     *
     * @param cbu El CBU exacto de la cuenta a buscar, en formato de cadena de texto.
     * @return La entidad {@link CuentaBancaria} asociada al CBU proporcionado.
     * @throws RecursoNoEncontradoException si no existe ninguna cuenta asociada al CBU provisto.
     */
    CuentaBancaria obtenerPorCbu(String cbu);

    /**
     * Recupera una cuenta bancaria específica utilizando su identificador único (UUID).
     *
     * @param idCuenta El identificador único (UUID) de la cuenta bancaria a consultar.
     * @return La entidad {@link CuentaBancaria} correspondiente al ID especificado.
     * @throws RecursoNoEncontradoException si no existe ninguna cuenta asociada al ID provisto.
     */
    CuentaBancaria obtenerPorId(UUID idCuenta);
}