package com.example.demo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CUENTA_CORRIENTE")
public class CuentaCorriente extends CuentaBancaria {

    private BigDecimal margenDescubierto;
    private BigDecimal costoMantenimiento;

    public BigDecimal getMargenDescubierto() { return margenDescubierto; }
    public void setMargenDescubierto(BigDecimal margenDescubierto) { this.margenDescubierto = margenDescubierto; }
    public BigDecimal getCostoMantenimiento() { return costoMantenimiento; }
    public void setCostoMantenimiento(BigDecimal costoMantenimiento) { this.costoMantenimiento = costoMantenimiento; }
}