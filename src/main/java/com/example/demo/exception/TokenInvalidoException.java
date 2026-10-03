package com.example.demo.exception;
/**
 * Excepcion de negocio lanzada cuando un token de activacion no existe,
 * ya fue utlizado o ha superado su plazo de caducidad tempral de las 24hs
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
public class TokenInvalidoException extends RuntimeException{
    public TokenInvalidoException(String message) {
        super(message);
    }
}
