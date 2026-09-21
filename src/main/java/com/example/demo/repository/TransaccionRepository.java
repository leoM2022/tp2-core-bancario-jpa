package com.example.demo.repository;

import com.example.demo.model.EstadoCuenta;
import com.example.demo.model.TipoTransaccion;
import com.example.demo.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA para la gestión de persistencia de la entidad {@link Transaccion }
 *
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 */
@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, UUID> {

    /**
     * Retorna la lista de transacciones realizadas en X día.
     *
     * @param fechaHora
     * @return {@link List<Transaccion>}
     */
    List<Transaccion> findByFechaHora(LocalDateTime fechaHora);

    /**
     * Retorna la lista de las transacciones que están en el estado ingresado.
     *
     * @param estadoTransaccion Estado de la Transacción: [PENDIENTE, COMPLETADA, RECHAZADA, REVERTIDA].
     * @return {@link List<Transaccion>}
     */
    List<Transaccion> findByEstadoTransaccion(EstadoCuenta estadoTransaccion);

    /**
     * Retorna la lista de las transacciones del tipo ingresado.
     * @param tipoTransaccion Tipo de transacción: [DEPÓSITO, EXTRACCIÓN, TRANSFERENCIA_ENVIADA, TRANSFERENCIA_RECIBIDA].
     * @return {@link List<Transaccion>}
     */
    List<Transaccion> findByTipoTransaccion(TipoTransaccion tipoTransaccion);

    /**
     * Retorna una lista de transacciones vinculadas a una cuenta bancaria por medio
     * de su CBU.
     * @param cbu Clave Bancaria Uniforme de 22 dígitos numéricos.
     * @return {@link List<Transaccion>}
     */
    List<Transaccion> findByCuentaBancaria_Cbu(String cbu);

    /**
     * Retorna una lista de transacciones vinculadas a una cuenta bancaria por medio
     * de su ALIAS
     * @param alias
     * @return {@link List<Transaccion>}
     */
    List<Transaccion> findByCuentaBancaria_Alias(String alias);
}
