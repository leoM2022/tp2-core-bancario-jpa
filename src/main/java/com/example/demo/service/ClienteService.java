package com.example.demo.service;

import com.example.demo.dto.ClienteRequestDto;
import com.example.demo.dto.ClienteResponseDto;
import com.example.demo.exception.RecursoDuplicadoException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interfaz de servicio principal para la gestión integral de la entidad {@link Cliente}.
 *
 * <p>Define el contrato de operaciones de negocio bajo el patrón Application Service,
 * abarcando operaciones de dominio puro (entidades) y operaciones desacopladas
 * para consumo externo mediante contratos DTO (Data Transfer Object).</p>
 *
 * @version 1.1.0
 * @author Dyevara23 & leoM2022
 * @since 2026-09-21
 * @see Cliente
 * @see ClienteRequestDto
 * @see ClienteResponseDto
 */
public interface ClienteService {

    /**
     * Registra un nuevo cliente a partir de un DTO validado desde la capa Controller (TP4).
     *
     * <p>Aplica las validaciones de unicidad de CUIL y Email, mapea la entidad correspondiente,
     * la persiste en MySQL y retorna el DTO de respuesta desacoplado del modelo JPA.</p>
     *
     * @param requestDto Objeto de transferencia de datos con la carga útil validada de entrada.
     * @return {@link ClienteResponseDto} con los datos públicos del cliente y su UUID asignado.
     * @throws RecursoDuplicadoException si el CUIL o el Email ya se encuentran registrados.
     */
    ClienteResponseDto registrarClienteDto(ClienteRequestDto requestDto);

    /**
     * Persiste un nuevo cliente en el sistema a partir de la entidad directa (TP3).
     *
     * @param cliente El objeto {@link Cliente} que contiene los datos a guardar.
     * @return La instancia del {@link Cliente} persistido, incluyendo su ID generado.
     * @throws RecursoDuplicadoException si el CUIL o el Email ya existen en la base de datos.
     */
    Cliente crearCliente(Cliente cliente);

    /**
     * Recupera un cliente específico utilizando su identificador único (UUID).
     *
     * @param id El identificador único (UUID) del cliente a recuperar.
     * @return El {@link Cliente} correspondiente al ID especificado.
     * @throws RecursoNoEncontradoException si no existe cliente asociado al ID provisto.
     */
    Cliente obtenerClientePorId(UUID id);

    /**
     * Recupera un cliente utilizando su Código Único de Identificación Laboral (CUIL).
     *
     * @param cuil El CUIL exacto del cliente a buscar, en formato de cadena de texto.
     * @return El {@link Cliente} asociado al CUIL proporcionado.
     * @throws RecursoNoEncontradoException si no existe cliente con el CUIL especificado.
     */
    Cliente obtenerClientePorCuil(String cuil);

    /**
     * Obtiene una lista paginada de todos los clientes registrados en el sistema.
     *
     * <p>Optimiza el rendimiento evitando la recuperación masiva en memoria RAM (antipatrón findAll).</p>
     *
     * @param pageable Objeto {@link Pageable} con la configuración de paginación y ordenamiento.
     * @return Una {@link Page} conteniendo las entidades {@link Cliente} del lote solicitado.
     */
    Page<Cliente> listarPaginado(Pageable pageable);

    /**
     * Elimina un cliente del sistema basándose en su identificador único.
     *
     * @param id El identificador único (UUID) del cliente que se desea eliminar.
     * @throws RecursoNoEncontradoException si el cliente no existe en la base de datos.
     */
    void eliminarCliente(UUID id);
}