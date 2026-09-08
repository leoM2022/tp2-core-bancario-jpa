package com.example.demo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_cuenta", discriminatorType = DiscriminatorType.STRING)
@Table(name = "cuentas_bancarias")
public abstract class CuentaBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuentaBancaria;

    private String cbu;
    private String alias;
    private BigDecimal saldoOperativo;

    @Enumerated(EnumType.STRING)
    private EstadoCuenta estado;

    public CuentaBancaria() {}

    public Long getIdCuentaBancaria() { return idCuentaBancaria; }
    public void setIdCuentaBancaria(Long idCuentaBancaria) { this.idCuentaBancaria = idCuentaBancaria; }
    public String getCbu() { return cbu; }
    public void setCbu(String cbu) { this.cbu = cbu; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public BigDecimal getSaldoOperativo() { return saldoOperativo; }
    public void setSaldoOperativo(BigDecimal saldoOperativo) { this.saldoOperativo = saldoOperativo; }
    public EstadoCuenta getEstado() { return estado; }
    public void setEstado(EstadoCuenta estado) { this.estado = estado; }
}