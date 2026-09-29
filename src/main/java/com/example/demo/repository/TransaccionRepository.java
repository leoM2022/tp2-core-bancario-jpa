package com.example.demo.repository;

import com.example.demo.model.EstadoTransaccion;
import com.example.demo.model.TipoTransaccion;
import com.example.demo.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA para la gestión de persistencia de la entidad {@link Transaccion}.
 *
 * @version 1.1.0
 * @author Dyevara23 & leoM2022
 */
@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, UUID> {

    /**
     * Retorna la lista de transacciones realizadas en un rango temporal.
     *
     * @param inicio Fecha y hora de inicio.
     * @param fin Fecha y hora de fin.
     * @return Lista de transacciones en dicho rango.
     */
    List<Transaccion> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Retorna la lista de transacciones según su estado.
     *
     * @param estadoTransaccion Estado de la transacción: [PENDIENTE, EXITOSA, RECHAZADA, etc.].
     * @return Lista de transacciones coincidentes.
     */
    List<Transaccion> findByEstadoTransaccion(EstadoTransaccion estadoTransaccion);

    /**
     * Retorna la lista de transacciones según su tipo operativo.
     *
     * @param tipoTransaccion Tipo de transacción: [DEPOSITO, EXTRACCION, TRANSFERENCIA, etc.].
     * @return Lista de transacciones coincidentes.
     */
    List<Transaccion> findByTipoTransaccion(TipoTransaccion tipoTransaccion);

    /**
     * Retorna las transacciones vinculadas a una cuenta bancaria por su ID primario.
     *
     * @param idCuenta Identificador UUID de la cuenta.
     * @return Lista de transacciones asociadas.
     */
    List<Transaccion> findByCuentaBancaria_IdCuentaBancaria(UUID idCuenta);

    /**
     * Retorna una lista de transacciones vinculadas a una cuenta bancaria por medio de su CBU.
     *
     * @param cbu Clave Bancaria Uniforme de 22 dígitos numéricos.
     * @return Lista de transacciones asociadas.
     */
    List<Transaccion> findByCuentaBancaria_Cbu(String cbu);

    /**
     * Retorna una lista de transacciones vinculadas a una cuenta bancaria por medio de su ALIAS.
     *
     * @param alias Alias de la cuenta.
     * @return Lista de transacciones asociadas.
     */
    List<Transaccion> findByCuentaBancaria_Alias(String alias);
}