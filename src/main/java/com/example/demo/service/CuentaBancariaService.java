/**
 * Interfaz de servicio para las operaciones de la entidad CuentaBancaria.
 *
 * Define el contrato para las operaciones de cuentas bancarias, incluyendo
 * depósitos, extracciones y consultas de las cuentas.
 */
package com.example.demo.service;

import com.example.demo.model.CuentaBancaria;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Interfaz de servicio principal para la gestión de la entidad {@link CuentaBancaria}.
 *
 * <p>Esta interfaz define el contrato que deben implementar las clases de servicio
 * para manejar las operaciones financieras y consultas relacionadas con las cuentas bancarias,
 * tales como acreditación de fondos, débitos y búsquedas específicas.</p>
 *
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 * @since 2026-09-21
 */
public interface CuentaBancariaService {

    /**
     * Acredita fondos en una cuenta bancaria específica.
     *
     * @param idCuenta El identificador único (UUID) de la cuenta destino para el depósito.
     * @param monto El importe exacto a depositar, representado como un {@link BigDecimal} para garantizar precisión financiera.
     * @return La entidad {@link CuentaBancaria} actualizada reflejando el nuevo saldo.
     */
    CuentaBancaria depositar(UUID idCuenta, BigDecimal monto);

    /**
     * Debita fondos de una cuenta bancaria específica.
     *
     * @param idCuenta El identificador único (UUID) de la cuenta de origen.
     * @param monto El importe exacto a extraer, representado como un {@link BigDecimal}.
     * @return La entidad {@link CuentaBancaria} actualizada reflejando el nuevo saldo tras la operación.
     */
    CuentaBancaria extraer(UUID idCuenta, BigDecimal monto);

    /**
     * Recupera una cuenta bancaria utilizando su Clave Bancaria Uniforme (CBU).
     *
     * @param cbu El CBU exacto de la cuenta a buscar, en formato de cadena de texto.
     * @return La entidad {@link CuentaBancaria} asociada al CBU proporcionado.
     */
    CuentaBancaria obtenerPorCbu(String cbu);

    /**
     * Recupera una cuenta bancaria específica utilizando su identificador único (UUID).
     *
     * @param idCuenta El identificador único (UUID) de la cuenta bancaria a consultar.
     * @return La entidad {@link CuentaBancaria} correspondiente al ID especificado.
     */
    CuentaBancaria obtenerPorId(UUID idCuenta);
}