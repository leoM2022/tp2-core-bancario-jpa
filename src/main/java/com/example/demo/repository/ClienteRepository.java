package com.example.demo.repository;

import com.example.demo.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA para la gestión de persistencia de la entidad {@link Cliente}.
 * <p>
 * Implementa el Patrón Almacén del Dominio (Domain Store), proveyendo consultas derivadas,
 * comprobaciones de existencia de bajo costo y consultas optimizadas JPQL para evitar
 * el problema de N+1 consultas.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see Cliente
 * @see JpaRepository
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    /**
     * Búsqueda unívoca de un cliente por su número de CUIL.
     *
     * @param cuil CUIL fiscal del cliente en formato XX-XXXXXXXX-X.
     * @return {@link Optional} con el cliente hallado o vacío en caso contrario.
     */
    Optional<Cliente> findByCuil(String cuil);

    /**
     * Búsqueda unívoca de un cliente por su correo electrónico.
     *
     * @param email Dirección de correo electrónico del titular.
     * @return {@link Optional} con el cliente correspondiente.
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Búsqueda de cliente a través de su número de contacto telefónico.
     *
     * @param telefono Línea telefónica registrada.
     * @return {@link Optional} con la entidad localizada.
     */
    Optional<Cliente> findByTelefono(String telefono);

    /**
     * Comprobación rápida de existencia por CUIL para validación previa en la capa de negocio.
     * Genera una consulta relacional optimizada (SELECT 1 ... LIMIT 1) sin instanciar la entidad en memoria.
     *
     * @param cuil CUIL fiscal a verificar.
     * @return true si ya existe en MySQL, false en caso contrario.
     */
    boolean existsByCuil(String cuil);

    /**
     * Comprobación rápida de existencia por correo electrónico.
     *
     * @param email Correo electrónico a verificar.
     * @return true si el email ya está en uso, false en caso contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Recupera una entidad {@link Cliente} inicializando su colección asociada de cuentas bancarias en un solo viaje a la base de datos.
     * <p>
     * Utiliza FETCH JOIN para mitigar la sobrecarga de consultas perezosas (Lazy Loading / N+1 select problem).
     * </p>
     *
     * @param id Identificador único universal (UUID) del cliente.
     * @return {@link Optional} del cliente con su grafo de cuentas cargado.
     */
    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.cuentas WHERE c.id = :id")
    Optional<Cliente> findByIdWithCuentas(@Param("id") UUID id);

    /**
     * Obtiene una lista de clientes asociados a una cuenta mediante el identificador UUID de la misma.
     *
     * @param idCuentaBancaria Identificador UUID de la cuenta bancaria.
     * @return Lista de clientes asociados.
     */
    List<Cliente> findByCuentas_IdCuentaBancaria(UUID idCuentaBancaria);

    /**
     * Localiza clientes asociados a una cuenta bancaria a partir de su Clave Bancaria Uniforme (CBU).
     *
     * @param cbu Clave Bancaria Uniforme de 22 dígitos.
     * @return Lista de clientes cotitulares o titulares vinculados a ese CBU.
     */
    List<Cliente> findByCuentas_Cbu(String cbu);

    /**
     * Localiza clientes asociados a una cuenta mediante su Alias alfanumérico.
     *
     * @param alias Alias identificador en el sistema financiero.
     * @return Lista de clientes vinculados a la cuenta.
     */
    List<Cliente> findByCuentas_Alias(String alias);

    /**
     * Recuperación paginada de clientes para optimizar el consumo de memoria en la JVM.
     *
     * @param pageable Configuración de número de página, tamaño y ordenamiento.
     * @return {@link Page} conteniendo el lote de clientes solicitado.
     */
    Page<Cliente> findAll(Pageable pageable);
}