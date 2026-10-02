package com.example.demo.repository;

import com.example.demo.model.EstadoTransaccion;
import com.example.demo.model.TipoTransaccion;
import com.example.demo.model.Transaccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA para la persistencia y auditoría de la entidad {@link Transaccion}.
 * <p>
 * Implementa el Patrón Almacén del Dominio (Domain Store) proveyendo trazabilidad contable
 * e historial de transferencias mediante consultas indexadas.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see Transaccion
 * @see JpaRepository
 */
@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, UUID> {

    /**
     * Recupera transacciones asentadas dentro de una ventana temporal específica.
     *
     * @param inicio Límite temporal inicial.
     * @param fin Límite temporal final.
     * @return Lista de transacciones registradas en el intervalo.
     */
    List<Transaccion> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Filtra el historial de operaciones según el estado de liquidación contable.
     *
     * @param estadoTransaccion Estado de la transacción (COMPLETADA, RECHAZADA, etc.).
     * @return Lista de transacciones en dicho estado.
     */
    List<Transaccion> findByEstadoTransaccion(EstadoTransaccion estadoTransaccion);

    /**
     * Filtra operaciones según su naturaleza funcional.
     *
     * @param tipoTransaccion Tipo operativo (DEPOSITO, EXTRACCION, TRANSFERENCIA).
     * @return Lista de transacciones coincidentes.
     */
    List<Transaccion> findByTipoTransaccion(TipoTransaccion tipoTransaccion);

    /**
     * Obtiene el historial de transacciones vinculadas a una cuenta mediante su identificador UUID.
     *
     * @param idCuenta Identificador UUID de la cuenta bancaria.
     * @return Lista de transacciones ordenadas cronológicamente.
     */
    List<Transaccion> findByCuentaBancaria_IdCuentaBancaria(UUID idCuenta);

    /**
     * Obtiene el historial de movimientos de una cuenta por medio de su CBU.
     *
     * @param cbu Clave Bancaria Uniforme de 22 dígitos.
     * @return Lista de transacciones asociadas.
     */
    List<Transaccion> findByCuentaBancaria_Cbu(String cbu);

    /**
     * Obtiene una página del historial de transacciones de una cuenta resolviendo dependencias con FETCH JOIN.
     *
     * @param cbu CBU de la cuenta bancaria.
     * @param pageable Configuración de paginación y ordenamiento.
     * @return Página de transacciones con sus cuentas asociadas inicializadas.
     */
    @Query("SELECT t FROM Transaccion t " +
            "LEFT JOIN FETCH t.cuentaOrigen " +
            "LEFT JOIN FETCH t.cuentaDestino " +
            "WHERE t.cuentaBancaria.cbu = :cbu")
    Page<Transaccion> findByCuentaBancariaCbuPaginado(@Param("cbu") String cbu, Pageable pageable);
}