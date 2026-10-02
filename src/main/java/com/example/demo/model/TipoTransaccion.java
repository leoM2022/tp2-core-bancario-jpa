package com.example.demo.model;

/**
 * Catálogo de tipos operacionales de transacciones soportadas por el core financiero.
 * <p>
 * Modela la naturaleza contable de cada movimiento sobre cuentas bancarias.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see Transaccion
 */
public enum TipoTransaccion {
    /**
     * Acreditación de fondos líquidos en una cuenta receptora.
     */
    DEPOSITO,

    /**
     * Débito de fondos líquidos contra el saldo o margen disponible de la cuenta.
     */
    EXTRACCION,

    /**
     * Movimiento global de giro entre cuenta origen y cuenta destino.
     */
    TRANSFERENCIA,

    /**
     * Registro de débito asociado al ordenante de una transferencia.
     */
    TRANSFERENCIA_ENVIADA,

    /**
     * Registro de crédito asociado al beneficiario de una transferencia.
     */
    TRANSFERENCIA_RECIBIDA
}