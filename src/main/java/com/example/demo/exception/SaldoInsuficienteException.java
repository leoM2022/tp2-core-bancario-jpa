package com.example.demo.exception;

public class SaldoInsuficienteException extends RuntimeException{
    public SaldoInsuficienteException(String mensaje){
        super(mensaje);
    }
}
