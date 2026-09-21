package com.example.demo.exception;
/**
 * Excepción que se lanza cuando un recurso ya existe.
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 */
public class RecursoDuplicadoException extends RuntimeException{
    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
