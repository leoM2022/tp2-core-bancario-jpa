/**
 * Interfaz de servicio para las operaciones de la entidad Cliente.
 *
 * Define el contrato para las operaciones de gestión de clientes, incluyendo
 * la creación, recuperación, listado y eliminación de registros de clientes.
 */
package com.example.demo.service;

import com.example.demo.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interfaz de servicio principal para la gestión de la entidad {@link Cliente}.
 *
 * <p>Esta interfaz define el contrato que deben implementar las clases de servicio
 * para manejar la lógica de negocio relacionada con los clientes. Expone las operaciones
 * esenciales de persistencia y consulta adaptadas a los requisitos del dominio.</p>
 *
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 * @since 2026-09-21
 */
public interface ClienteService {

    /**
     * Persiste un nuevo cliente en el sistema.
     *
     * @param cliente El objeto {@link Cliente} que contiene los datos a guardar.
     * @return La instancia del {@link Cliente} persistido, incluyendo su ID generado.
     */
    Cliente crearCliente(Cliente cliente);

    /**
     * Recupera un cliente específico utilizando su identificador único (UUID).
     *
     * @param id El identificador único (UUID) del cliente a recuperar.
     * @return El {@link Cliente} correspondiente al ID especificado.
     */
    Cliente obtenerClientePorId(UUID id);

    /**
     * Recupera un cliente utilizando su Código Único de Identificación Laboral (CUIL).
     *
     * @param cuil El CUIL exacto del cliente a buscar, en formato de cadena de texto.
     * @return El {@link Cliente} asociado al CUIL proporcionado.
     */
    Cliente obtenerClientePorCuil(String cuil);

    /**
     * Obtiene una lista paginada de todos los clientes registrados en el sistema.
     *
     * <p>Ideal para optimizar el rendimiento al consultar grandes volúmenes de datos,
     * permitiendo gestionar el tamaño de la respuesta y su ordenamiento.</p>
     *
     * @param pageable Objeto {@link Pageable} que contiene la información de paginación
     *                 (número de página, tamaño de la página y criterios de orden).
     * @return Una {@link Page} que contiene los clientes de la página solicitada.
     */
    Page<Cliente> listarPaginado(Pageable pageable);

    /**
     * Elimina un cliente del sistema basándose en su identificador único.
     *
     * @param id El identificador único (UUID) del cliente que se desea eliminar.
     */
    void eliminarCliente(UUID id);
}