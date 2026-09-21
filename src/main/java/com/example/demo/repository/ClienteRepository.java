package com.example.demo.repository;

import com.example.demo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA para la gestión de persistencia de la entidad {@link Cliente }
 *
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    /**
     * Búsqueda de un cliente por su número de CUIL.
     *
     * @param cuil CUIL del cliente a buscar
     * @return {@link Optional}
     */
    Optional<Cliente>findByCuil(String cuil);

    /**
     * Búsqueda de un cliente por medio de su correo electrónico.
     *
     * @param email Correo electrónico del cliente
     * @return {@link Optional}
     */
    Optional<Cliente>findByEmail(String email);

    /**
     * Búsqueda de un cliente por medio de su número telefónico.
     *
     * @param telefono Número telefónico del cliente
     * @return {@link Optional}
     */
    Optional<Cliente>findByTelefono(String telefono);

    /**
     * Recupera a un cliente junto con sus cuentas asociadas
     * @param id Identificador único del cliente
     * @return {@link Optional} del cliente con la colección de cuentas inicializada.
     */
    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.cuentas WHERE c.id = :idCuentaBancaria")
    Optional<Cliente>findByIdWithCuentas(@Param("idCuentaBancaria") UUID idCuentaBancaria);

    /**
     * Retorna la lista de Clientes asociados a la cuenta mediante
     * el identificador único de la misma.
     * @param cuentaIdCuentaBancaria Identificador único de la cuenta en UUID.
     * @return {@link List<Cliente>}
     */
    List<Cliente> findByCuentas_IdCuentaBancaria(UUID cuentaIdCuentaBancaria);

    /**
     * Retorna la lista de Clientes asociados a la cuenta mediante
     * la Clave Bancaria Uniforme de la misma.
     * @param cbu
     * @return {@link List<Cliente>}
     */
    List<Cliente> findByCuentas_Cbu(String cbu);

    /**
     * Retorna la lista de Clientes asociados a la cuenta mediante
     * el Alias de la misma.
     * @param alias Alias de la cuenta bancaria.
     * @return {@link List<Cliente>}
     */
    List<Cliente> findByCuentas_Alias(String alias);
}
