package com.example.demo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CAJA_AHORRO")
public class CajaAhorro extends CuentaBancaria {
    private BigDecimal tasaInteresAnual;
    private BigDecimal cupoEntero;
    public BigDecimal getTasaInteresAnual() { return tasaInteresAnual; }
    public void setTasaInteresAnual(BigDecimal tasaInteresAnual) { this.tasaInteresAnual = tasaInteresAnual; }
    public BigDecimal getCupoEntero() { return cupoEntero; }
    public void setCupoEntero(BigDecimal cupoEntero) { this.cupoEntero = cupoEntero; }
}