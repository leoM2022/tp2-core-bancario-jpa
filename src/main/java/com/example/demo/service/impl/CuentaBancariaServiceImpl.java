/**
 * Implementación de la interfaz {@link CuentaBancariaService}.
 *
 * <p>Este servicio gestiona toda la lógica de negocio centralizada para las operaciones
 * de cuentas bancarias, abarcando depósitos, extracciones y consultas. Garantiza la
 * integridad de las transacciones mediante la validación rigurosa del estado de la cuenta
 * y la disponibilidad de fondos previos a cualquier movimiento financiero.</p>
 *
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 * @since 2026-09-21
 */
package com.example.demo.service.impl;

import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.model.CuentaBancaria;
import com.example.demo.model.CuentaCorriente;
import com.example.demo.model.EstadoCuenta;
import com.example.demo.repository.CuentaBancariaRepository;
import com.example.demo.service.CuentaBancariaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuentaBancariaServiceImpl implements CuentaBancariaService {

    private final CuentaBancariaRepository cuentaRepository;

    /**
     * Acredita fondos en una cuenta bancaria específica.
     *
     * <p>Aplica reglas de validación para asegurar que el importe a depositar sea positivo
     * y que la cuenta destino se encuentre en estado {@link EstadoCuenta#ACTIVA} antes
     * de incrementar el saldo operativo.</p>
     *
     * @param idCuenta El identificador único (UUID) de la cuenta destino.
     * @param monto El importe a depositar (debe ser estrictamente mayor a cero).
     * @return La entidad {@link CuentaBancaria} actualizada con el nuevo saldo.
     * @throws IllegalArgumentException si el monto a depositar es nulo o igual/menor a cero.
     * @throws IllegalStateException si la cuenta no se encuentra activa para operar.
     */
    @Override
    @Transactional
    public CuentaBancaria depositar(UUID idCuenta, BigDecimal monto) {
        log.info("Iniciando deposito en cuenta ID: {} por monto: {}", idCuenta, monto);

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser superior a cero.");
        }

        CuentaBancaria cuenta = obtenerPorId(idCuenta);

        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            log.error("Operacion rechazada: La cuenta {} se encuentra en estado {}", idCuenta, cuenta.getEstado());
            throw new IllegalStateException("La cuenta no se encuentra activa para operar.");
        }

        cuenta.setSaldoOperativo(cuenta.getSaldoOperativo().add(monto));
        return cuentaRepository.save(cuenta);
    }

    /**
     * Debita fondos de una cuenta bancaria específica.
     *
     * <p>Realiza múltiples validaciones de seguridad previo al débito: verifica que el monto
     * sea positivo, que la cuenta esté {@link EstadoCuenta#ACTIVA}, y que existan fondos
     * suficientes. En el caso de instancias de {@link CuentaCorriente}, contempla dinámicamente
     * el margen de descubierto asignado para el cálculo de los fondos disponibles.</p>
     *
     * @param idCuenta El identificador único (UUID) de la cuenta de origen.
     * @param monto El importe a extraer (debe ser estrictamente mayor a cero).
     * @return La entidad {@link CuentaBancaria} actualizada con el saldo deducido.
     * @throws IllegalArgumentException si el monto a extraer es nulo o igual/menor a cero.
     * @throws IllegalStateException si la cuenta no está activa o si los fondos son insuficientes.
     */
    @Override
    @Transactional
    public CuentaBancaria extraer(UUID idCuenta, BigDecimal monto) {
        log.info("Iniciando extraccion en cuenta ID: {} por monto: {}", idCuenta, monto);

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a extraer debe ser superior a cero.");
        }

        CuentaBancaria cuenta = obtenerPorId(idCuenta);

        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            log.error("Operacion rechazada: La cuenta {} se encuentra en estado {}", idCuenta, cuenta.getEstado());
            throw new IllegalStateException("La cuenta no se encuentra activa para operar.");
        }

        BigDecimal fondosDisponibles = cuenta.getSaldoOperativo();

        if (cuenta instanceof CuentaCorriente cc) {
            if (cc.getMargenDescubierto() != null) {
                fondosDisponibles = fondosDisponibles.add(cc.getMargenDescubierto());
            }
        }

        if (fondosDisponibles.compareTo(monto) < 0) {
            log.error("Fondos insuficientes en cuenta {}. Disponibles: {}, Requeridos: {}", idCuenta, fondosDisponibles, monto);
            throw new IllegalStateException("Fondos insuficientes para efectuar la extraccion.");
        }

        cuenta.setSaldoOperativo(cuenta.getSaldoOperativo().subtract(monto));
        return cuentaRepository.save(cuenta);
    }

    /**
     * Recupera una cuenta bancaria utilizando su Clave Bancaria Uniforme (CBU).
     *
     * @param cbu El CBU exacto de la cuenta a buscar, en formato de cadena de texto.
     * @return La entidad {@link CuentaBancaria} asociada al CBU proporcionado.
     * @throws RecursoNoEncontradoException si no existe ninguna cuenta asociada al CBU provisto.
     */
    @Override
    @Transactional(readOnly = true)
    public CuentaBancaria obtenerPorCbu(String cbu) {
        return cuentaRepository.findByCbu(cbu)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con CBU: " + cbu));
    }

    /**
     * Recupera una cuenta bancaria específica utilizando su identificador único (UUID).
     *
     * @param idCuenta El identificador único (UUID) de la cuenta bancaria a consultar.
     * @return La entidad {@link CuentaBancaria} correspondiente al ID especificado.
     * @throws RecursoNoEncontradoException si no existe ninguna cuenta asociada al ID provisto.
     */
    @Override
    @Transactional(readOnly = true)
    public CuentaBancaria obtenerPorId(UUID idCuenta) {
        return cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con ID: " + idCuenta));
    }
}