package com.example.demo.exception;

public class TransaccionRechazadaException extends RuntimeException {
    public TransaccionRechazadaException(String message) {
        super(message);
    }
}
