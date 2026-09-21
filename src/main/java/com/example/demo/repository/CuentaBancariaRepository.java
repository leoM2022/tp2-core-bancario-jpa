package com.example.demo.repository;

import com.example.demo.model.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA para la gestión de persistencia de la entidad {@link CuentaBancaria }
 *
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 */
@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, UUID> {

    /**
     * Búsqueda una Cuenta Bancaria mediante el CBU.
     *
     * @param cbu Clave Bancaria Uniforme correspondiente a la cuenta.
     * @return {@link Optional}
     */
    Optional<CuentaBancaria>findByCbu(String cbu);

    /**
     * Búsqueda de una Cuenta Bancaria por medio de su ALIAS.
     * @param alias Alias de la cuenta.
     * @return {@link Optional}
     */
    Optional<CuentaBancaria>findByAlias(String alias);

}