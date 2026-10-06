package com.example.demo.model;

/**
 * Representa los roles que puede tener un cliente en una cuenta del sistema bancario.
 * Estos pueden ser Titular o Adherente (Cónyuge y/o hijos).
 *
 * @see Cliente
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 */
public enum RolCliente {
    /**
     * El cliente titular de la cuenta bancaria.
     */
    TITULAR,

    /**
     * Persona unida en matrimonio con el/la titular de la cuenta bancaria.
     */
    CONYUGE,

    /**
     * Hijo/a del titular de la cuenta
     */
    HIJO
}