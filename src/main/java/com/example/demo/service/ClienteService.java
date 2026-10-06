package com.example.demo.service;

import com.example.demo.dto.AdherenteRequestDto;
import com.example.demo.dto.AdherenteResponseDto;
import com.example.demo.dto.ClienteRequestDto;
import com.example.demo.dto.ClienteResponseDto;
import com.example.demo.exception.OperacionNoPermitidaException;
import com.example.demo.exception.RecursoDuplicadoException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.TokenInvalidoException;
import com.example.demo.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Contrato de operaciones de negocio para la gestión integral de la entidad {@link Cliente}.
 * <p>
 * Implementa el Patrón Servicio de Aplicación (Application Service), coordinando casos de uso
 * del dominio bancario con persistencia transaccional ACID y desacoplamiento mediante contratos DTO.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see Cliente
 * @see ClienteRequestDto
 * @see ClienteResponseDto
 */
public interface ClienteService {

    /**
     * Registra un nuevo cliente titular a partir del payload validado recibido desde la API REST.
     *
     * @param requestDto DTO con los datos de contacto y fiscales validados.
     * @return {@link ClienteResponseDto} con el identificador UUID asignado y marca temporal.
     * @throws RecursoDuplicadoException Si el CUIL o el correo electrónico ya se encuentran registrados.
     */
    ClienteResponseDto registrarClienteDto(ClienteRequestDto requestDto);

    /**
     * Persiste una entidad {@link Cliente} directa asegurando las invariantes de negocio.
     *
     * @param cliente Instancia de la entidad a persistir.
     * @return Instancia persistida con identificador técnico asignado.
     * @throws RecursoDuplicadoException Si existe conflicto de unicidad en CUIL o Email.
     */
    Cliente crearCliente(Cliente cliente);

    /**
     * Localiza un cliente mediante su identificador único universal (UUID).
     *
     * @param id Identificador UUID del cliente.
     * @return Instancia de {@link Cliente} localizada.
     * @throws RecursoNoEncontradoException Si no existe registro asociado al UUID provisto.
     */
    Cliente obtenerClientePorId(UUID id);

    /**
     * Registra y vincula un nuevo adherente bajo la tutela de un cliente titular existente.
     *
     * @param cuilTitular CUIL del titular de la cuenta bancaria.
     * @param requestDto DTO con los datos de filiación, parentesco y contacto del adherente.
     * @return {@link AdherenteResponseDto} con el UUID asignado y estado de habilitación.
     * @throws RecursoNoEncontradoException Si el titular no existe en el sistema.
     * @throws RecursoDuplicadoException Si el CUIL o DNI del adherente ya se encuentra registrado.
     * @throws OperacionNoPermitidaException Si el titular provisto es a su vez un adherente.
     */
    AdherenteResponseDto registrarAdherente(String cuilTitular, AdherenteRequestDto requestDto);

    /**
     * Desvincula un adherente de su titular o revoca su autorización de extracción.
     *
     * @param cuilTitular   CUIL del titular de la cuenta bancaria
     * @param cuilAdherente CUIL del adherente a desvincular.
     * @throws RecursoNoEncontradoException  Si el titular o el adherente no existen.
     * @throws OperacionNoPermitidaException Si el adherente no pertenece al titular indicado.
     */
    void desvincularAdherente(String cuilTitular, String cuilAdherente);

    /**
     * Localiza un cliente a partir de su Clave Única de Identificación Laboral (CUIL).
     *
     * @param cuil Código fiscal exacto en formato XX-XXXXXXXX-X.
     * @return Instancia de {@link Cliente} encontrada.
     * @throws RecursoNoEncontradoException Si no existe registro asociado al CUIL provisto.
     */
    Cliente obtenerClientePorCuil(String cuil);

    /**
     * Recupera una vista paginada de clientes para optimizar el rendimiento y evitar sobrecarga de memoria.
     *
     * @param pageable Parámetros de paginación y ordenamiento.
     * @return {@link Page} conteniendo el lote de clientes solicitado.
     */
    Page<Cliente> listarPaginado(Pageable pageable);

    /**
     * Da de baja a un cliente del sistema a partir de su identificador UUID.
     *
     * @param id Identificador del cliente a remover.
     * @throws RecursoNoEncontradoException Si el cliente no existe en la base de datos.
     */
    void eliminarCliente(UUID id);

    /**
     * Valida y activa un cliente a partir de su token único de confirmación.
     *
     * @param token Código UUID recibido mediante el enlace de activación.
     * @return {@link ClienteResponseDto} con los datos del cliente y estado actualizado a ACTIVO.
     * @throws TokenInvalidoException Si el token no existe o superó las 24 horas reglamentarias.
     */
    ClienteResponseDto activarClientePorToken(String token);

}