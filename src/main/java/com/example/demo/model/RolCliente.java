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
     * Persona adherente al titular de la cuenta.
     */
    ADHERENTE
}