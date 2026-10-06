package com.example.demo.exception;

/**
 * Excepción que se lanza cuando no se permite la operación indicada por algún error en los parámetros.
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 */
public class OperacionNoPermitidaException extends RuntimeException {
    public OperacionNoPermitidaException(String message) {
        super(message);
    }
}
