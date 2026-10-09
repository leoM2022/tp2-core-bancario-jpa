package com.example.demo.service;

import com.example.demo.dto.CuentaBancariaRequestDto;
import com.example.demo.dto.CuentaBancariaResponseDto;
import com.example.demo.exception.CuentaInactivaException;
import com.example.demo.exception.OperacionInvalidaException;
import com.example.demo.exception.RecursoDuplicadoException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.SaldoInsuficienteException;
import com.example.demo.model.ConfiguracionTope;
import com.example.demo.model.CuentaBancaria;
import com.example.demo.model.RolCliente;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Contrato de operaciones transaccionales para la administración de productos de cuenta bancaria.
 * <p>
 * Implementa el Patrón Application Service coordinando operaciones de mutación de saldos
 * bajo control transaccional ACID y desacoplando contratos para la API REST mediante DTOs.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.3.0
 * @see CuentaBancaria
 * @see CuentaBancariaRequestDto
 * @see CuentaBancariaResponseDto
 */
public interface CuentaBancariaService {

    /**
     * Da de alta y apertura una cuenta bancaria a partir de un DTO validado.
     *
     * @param requestDto DTO de entrada con CBU, alias, saldo inicial y titular opcional.
     * @return {@link CuentaBancariaResponseDto} con los datos visibles y el UUID generado.
     * @throws RecursoDuplicadoException Si el CBU o el Alias ya se encuentran registrados en MySQL.
     * @throws RecursoNoEncontradoException Si el cliente titular especificado no existe.
     */
    CuentaBancariaResponseDto aperturarCuentaDto(CuentaBancariaRequestDto requestDto);

    /**
     * Consulta el detalle de una cuenta bancaria por su CBU devolviendo un DTO desacoplado.
     *
     * @param cbu Clave Bancaria Uniforme de 22 dígitos.
     * @return {@link CuentaBancariaResponseDto} con la información pública de la cuenta.
     * @throws RecursoNoEncontradoException Si el CBU provisto no existe en el sistema.
     */
    CuentaBancariaResponseDto consultarDetallePorCbuDto(String cbu);

    /**
     * Acredita fondos en una cuenta bancaria activa.
     *
     * @param idCuenta Identificador UUID de la cuenta destino.
     * @param cuilCliente CUIL del cliente que realiza la transacción.
     * @param monto Importe monetario a acreditar.
     * @return Instancia de {@link CuentaBancaria} actualizada con el nuevo saldo.
     * @throws OperacionInvalidaException Si el monto es nulo o menor/igual a cero.
     * @throws CuentaInactivaException Si la cuenta receptora no se encuentra en estado ACTIVA.
     * @throws RecursoNoEncontradoException Si la cuenta no existe en el repositorio.
     */
    CuentaBancaria depositar(UUID idCuenta, String cuilCliente, BigDecimal monto);

    /**
     * Debita fondos de una cuenta bancaria activa considerando descubierto en Cuentas Corrientes.
     *
     * @param idCuenta Identificador UUID de la cuenta de origen.
     * @param cuilCliente CUIL del cliente que realiza la transacción.
     * @param monto Importe monetario a debitar.
     * @return Instancia de {@link CuentaBancaria} actualizada con el saldo remanente.
     * @throws OperacionInvalidaException Si el monto es nulo o menor/igual a cero.
     * @throws CuentaInactivaException Si la cuenta no se encuentra en estado ACTIVA.
     * @throws SaldoInsuficienteException Si los fondos disponibles (incluyendo descubierto) son insuficientes.
     * @throws RecursoNoEncontradoException Si la cuenta no existe en la base de datos.
     */
    CuentaBancaria extraer(UUID idCuenta, String cuilCliente, BigDecimal monto);

    /**
     * Recupera una cuenta bancaria por su Clave Bancaria Uniforme (CBU).
     *
     * @param cbu CBU de 22 dígitos numéricos.
     * @return Entidad {@link CuentaBancaria} correspondiente.
     * @throws RecursoNoEncontradoException Si no existe cuenta vinculada al CBU provisto.
     */
    CuentaBancaria obtenerPorCbu(String cbu);

    /**
     * Recupera una cuenta bancaria a través de su identificador UUID.
     *
     * @param idCuenta Identificador UUID de la cuenta.
     * @return Entidad {@link CuentaBancaria} correspondiente.
     * @throws RecursoNoEncontradoException Si no existe registro asociado al UUID provisto.
     */
    CuentaBancaria obtenerPorId(UUID idCuenta);

    /**
     * Recupera una ConfiguracionTope a traves de su rolCliente
     * @param rolCliente rol del cliente [TITULAR, ADHERENTE].
     * @return Entidad {@link ConfiguracionTope} correspondiente.
     * @throws RecursoNoEncontradoException Si no existe el registro.
     */
    ConfiguracionTope obtenerTopePorRol(RolCliente rolCliente);
}