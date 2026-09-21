/**
 * Implementación de la interfaz {@link ClienteService}.
 *
 * <p>Este servicio gestiona toda la lógica de negocio relacionada con la entidad {@link Cliente},
 * incluyendo la creación, recuperación, listado y eliminación de registros.
 * Interactúa directamente con {@link ClienteRepository} para las operaciones de persistencia de datos.</p>
 *
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 * @since 2026-09-21
 */
package com.example.demo.service.impl;

import com.example.demo.exception.RecursoDuplicadoException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.model.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * Persiste un nuevo cliente en el sistema.
     *
     * <p>Aplica reglas de negocio validando que el CUIL y el correo electrónico
     * del cliente no se encuentren previamente registrados antes de proceder con
     * la persistencia de la entidad.</p>
     *
     * @param cliente El objeto {@link Cliente} que contiene los datos a registrar.
     * @return La instancia del {@link Cliente} persistido, incluyendo su ID generado.
     * @throws RecursoDuplicadoException si el CUIL o el correo electrónico ya existen en el sistema.
     */
    @Override
    @Transactional
    public Cliente crearCliente(Cliente cliente) {
        log.info("Validando alta de cliente con CUIL: {}", cliente.getCuil());

        if (clienteRepository.findByCuil(cliente.getCuil()).isPresent()) {
            log.error("El CUIL {} ya se encuentra registrado", cliente.getCuil());
            throw new RecursoDuplicadoException("El CUIL " + cliente.getCuil() + " ya se encuentra registrado.");
        }

        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            log.error("El correo {} ya se encuentra registrado", cliente.getEmail());
            throw new RecursoDuplicadoException("El correo electronico ya esta en uso.");
        }

        Cliente guardado = clienteRepository.save(cliente);
        log.info("Cliente registrado exitosamente con ID: {}", guardado.getId());
        return guardado;
    }

    /**
     * Recupera un cliente específico utilizando su identificador único (UUID).
     *
     * @param id El identificador único (UUID) del cliente a recuperar.
     * @return El {@link Cliente} correspondiente al ID especificado.
     * @throws RecursoNoEncontradoException si no existe ningún cliente asociado al ID provisto.
     */
    @Override
    @Transactional(readOnly = true)
    public Cliente obtenerClientePorId(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id));
    }

    /**
     * Recupera un cliente utilizando su Código Único de Identificación Laboral (CUIL).
     *
     * @param cuil El CUIL exacto del cliente a buscar, en formato de cadena de texto.
     * @return El {@link Cliente} asociado al CUIL proporcionado.
     * @throws RecursoNoEncontradoException si no existe ningún cliente asociado al CUIL provisto.
     */
    @Override
    @Transactional(readOnly = true)
    public Cliente obtenerClientePorCuil(String cuil) {
        return clienteRepository.findByCuil(cuil)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con CUIL: " + cuil));
    }

    /**
     * Obtiene una lista paginada de todos los clientes registrados en el sistema.
     *
     * @param pageable Objeto {@link Pageable} que contiene la información de paginación
     *                 (número de página, tamaño de la página y criterios de orden).
     * @return Una {@link Page} que contiene las entidades de clientes solicitadas.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> listarPaginado(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    /**
     * Elimina un cliente del sistema basándose en su identificador único.
     *
     * <p>El método verifica primero la existencia del cliente utilizando
     * {@link #obtenerClientePorId(UUID)}. Si el cliente existe, procede con su eliminación física.</p>
     *
     * @param id El identificador único (UUID) del cliente que se desea eliminar.
     * @throws RecursoNoEncontradoException si el cliente a eliminar no se encuentra en el sistema.
     */
    @Override
    @Transactional
    public void eliminarCliente(UUID id) {
        Cliente cliente = obtenerClientePorId(id);
        clienteRepository.delete(cliente);
        log.info("Cliente con ID {} eliminado exitosamente", id);
    }
}