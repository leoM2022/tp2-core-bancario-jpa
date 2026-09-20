package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Clase Abstracta que provee los atributos y Listeners reutilizables para
 * todas las Entidades del Modelo de Dominio.
 * <p>
 * Al estar anotada con {@link MappedSuperclass}, hereda sus mapeos a las subclases
 * sin generar una tabla física independiente en la base de datos.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 * @see AuditingEntityListener
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter


public abstract class EntidadAuditable {

    /**
     * Fecha y hora en la cual la entidad fue creada.
     */
    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Fecha y hora de la ultia modificacion de la entidad.
     */
    @LastModifiedDate
    @Column(name = "ultima-modificacion")
    private LocalDateTime UltimaModificacion;
}
