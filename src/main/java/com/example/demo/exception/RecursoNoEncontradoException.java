package com.example.demo.exception;

/**
 * Excepción que se lanza cuando un recurso no es encontrado.
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 */
public class RecursoNoEncontradoException extends RuntimeException{
    public RecursoNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
