package com.example.demo.repository;

import com.example.demo.model.CuentaBancaria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA para la gestión de persistencia de la jerarquía {@link CuentaBancaria}.
 * <p>
 * Implementa el Patrón Almacén del Dominio (Domain Store) abstrayendo las consultas relacionales
 * sobre la tabla unificada generada bajo la estrategia SINGLE_TABLE.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see CuentaBancaria
 * @see JpaRepository
 */
@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, UUID> {

    /**
     * Búsqueda unívoca de cuenta por su Clave Bancaria Uniforme (CBU).
     *
     * @param cbu Clave Bancaria Uniforme de 22 dígitos numéricos.
     * @return {@link Optional} con la cuenta bancaria si existe.
     */
    Optional<CuentaBancaria> findByCbu(String cbu);

    /**
     * Búsqueda unívoca de cuenta bancaria por su Alias alfanumérico.
     *
     * @param alias Alias identificador en el sistema financiero.
     * @return {@link Optional} con la cuenta bancaria localizada.
     */
    Optional<CuentaBancaria> findByAlias(String alias);

    /**
     * Comprobación rápida de existencia por CBU para validaciones previas al alta.
     *
     * @param cbu CBU a verificar.
     * @return true si ya existe en la base relacional, false en caso contrario.
     */
    boolean existsByCbu(String cbu);

    /**
     * Comprobación rápida de existencia por Alias financiero.
     *
     * @param alias Alias a verificar.
     * @return true si ya se encuentra registrado, false en caso contrario.
     */
    boolean existsByAlias(String alias);

    /**
     * Recupera una cuenta bancaria cargando anticipadamente su colección de transacciones.
     * <p>
     * Utiliza FETCH JOIN para mitigar la sobrecarga de consultas perezosas (N+1 Selects).
     * </p>
     *
     * @param id Identificador UUID de la cuenta bancaria.
     * @return {@link Optional} con la cuenta y sus movimientos asociados inicializados.
     */
    @Query("SELECT c FROM CuentaBancaria c LEFT JOIN FETCH c.transacciones WHERE c.idCuentaBancaria = :id")
    Optional<CuentaBancaria> findByIdWithTransacciones(@Param("id") UUID id);

    /**
     * Recuperación paginada de cuentas para optimizar el rendimiento y evitar saturación de memoria.
     *
     * @param pageable Configuración de paginación y ordenamiento.
     * @return {@link Page} conteniendo el lote de cuentas bancarias solicitado.
     */
    Page<CuentaBancaria> findAll(Pageable pageable);
}