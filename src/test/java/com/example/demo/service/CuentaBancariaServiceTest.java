package com.example.demo.service;

import com.example.demo.model.CuentaBancaria;
import com.example.demo.model.CuentaCorriente;
import com.example.demo.model.EstadoCuenta;
import com.example.demo.repository.CuentaBancariaRepository;
import com.example.demo.service.impl.CuentaBancariaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para la implementación {@link CuentaBancariaServiceImpl}.
 *
 * <p>Utiliza JUnit 5 y Mockito para aislar el comportamiento del servicio financiero.
 * Valida de forma rigurosa los cálculos transaccionales, las actualizaciones de saldo
 * y las reglas de negocio vinculadas al uso de márgenes de descubierto y estados de cuenta,
 * sin afectar la base de datos real.</p>
 *
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 * @since 2026-09-21
 */
@ExtendWith(MockitoExtension.class)
class CuentaBancariaServiceTest {

    @Mock
    private CuentaBancariaRepository cuentaRepository;

    @InjectMocks
    private CuentaBancariaServiceImpl cuentaService;

    private CuentaCorriente cuentaPrueba;
    private UUID idCuenta;

    /**
     * Configuración inicial ejecutada antes de cada prueba.
     *
     * <p>Prepara un escenario financiero base instanciando una {@link CuentaCorriente} activa.
     * Se le asigna un saldo operativo inicial y un margen de descubierto autorizado para
     * poder evaluar las variaciones de fondos a lo largo de las pruebas.</p>
     */
    @BeforeEach
    void setUp() {
        idCuenta = UUID.randomUUID();
        cuentaPrueba = new CuentaCorriente();
        cuentaPrueba.setIdCuentaBancaria(idCuenta);
        cuentaPrueba.setCbu("0000003100010000000001");
        cuentaPrueba.setAlias("TEST.CUENTA.BANCO");
        cuentaPrueba.setSaldoOperativo(new BigDecimal("10000.00"));
        cuentaPrueba.setMargenDescubierto(new BigDecimal("5000.00"));
        cuentaPrueba.setEstado(EstadoCuenta.ACTIVA);
    }

    /**
     * Prueba el flujo exitoso de acreditación de fondos.
     *
     * <p>Verifica que al realizar un depósito válido, el saldo operativo se incremente
     * correctamente a nivel aritmético y que el servicio delegue la persistencia de los
     * nuevos valores al repositorio subyacente.</p>
     */
    @Test
    @DisplayName("Debe acreditar el saldo correctamente al realizar un deposito")
    void depositar_Exitoso() {
        when(cuentaRepository.findById(idCuenta)).thenReturn(Optional.of(cuentaPrueba));
        when(cuentaRepository.save(any(CuentaBancaria.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CuentaBancaria resultado = cuentaService.depositar(idCuenta, new BigDecimal("3000.00"));

        assertNotNull(resultado);
        assertEquals(new BigDecimal("13000.00"), resultado.getSaldoOperativo());
        verify(cuentaRepository, times(1)).save(cuentaPrueba);
    }

    /**
     * Prueba el flujo de extracción utilizando el margen de descubierto autorizado.
     *
     * <p>Valida una de las reglas de negocio más críticas: permitir que el saldo operativo
     * pase a valores negativos siempre y cuando el monto a extraer no supere la suma del
     * saldo actual más el margen de descubierto asignado a la {@link CuentaCorriente}.</p>
     */
    @Test
    @DisplayName("Debe permitir extraer usando saldo operativo mas margen de descubierto")
    void extraer_Exitoso_ConDescubierto() {
        when(cuentaRepository.findById(idCuenta)).thenReturn(Optional.of(cuentaPrueba));
        when(cuentaRepository.save(any(CuentaBancaria.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Saldo 10000 + Descubierto 5000 = 15000 disponibles. Extraemos 12000:
        CuentaBancaria resultado = cuentaService.extraer(idCuenta, new BigDecimal("12000.00"));

        assertNotNull(resultado);
        assertEquals(new BigDecimal("-2000.00"), resultado.getSaldoOperativo());
        verify(cuentaRepository, times(1)).save(cuentaPrueba);
    }

    /**
     * Prueba la restricción operativa por fondos insuficientes.
     *
     * <p>Asegura que el sistema prevenga un sobregiro no autorizado. Si el intento de extracción
     * excede tanto el saldo operativo como el margen de descubierto disponible, se debe emitir
     * una {@link IllegalStateException} abortando por completo la operación financiera.</p>
     */
    @Test
    @DisplayName("Debe lanzar excepcion si la extraccion excede el saldo y el descubierto autorizado")
    void extraer_Falla_FondosInsuficientes() {
        when(cuentaRepository.findById(idCuenta)).thenReturn(Optional.of(cuentaPrueba));

        // Intento de extraer 20000 cuando el maximo con descubierto es 15000:
        assertThrows(IllegalStateException.class, () -> cuentaService.extraer(idCuenta, new BigDecimal("20000.00")));
        verify(cuentaRepository, never()).save(any(CuentaBancaria.class));
    }
}