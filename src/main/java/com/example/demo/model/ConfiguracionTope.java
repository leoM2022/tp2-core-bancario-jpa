package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "topes_extraccion")
@Getter
@Setter
public class ConfiguracionTope extends EntidadAuditable{
    @Id
    private UUID id;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private RolCliente rolCliente;

    @NotNull
    @Column(scale = 2, precision = 15)
    private BigDecimal montoMaximoDiario;

}
