package com.example.demo.service;

import com.example.demo.exception.RecursoDuplicadoException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.model.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para la implementación {@link ClienteServiceImpl}.
 *
 * <p>Utiliza el marco de pruebas JUnit 5 junto con Mockito para aislar la lógica de negocio
 * del servicio, simulando (mocking) las interacciones con el {@link ClienteRepository}.
 * Garantiza que las reglas de negocio y las excepciones se manejen correctamente bajo
 * distintos escenarios.</p>
 *
 * @version 1.0.0
 * @author Dyevara23 & leoM2022
 * @since 2026-09-21
 */
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente clienteEjemplo;
    private UUID idPrueba;

    /**
     * Configuración inicial ejecutada antes de cada prueba.
     *
     * <p>Prepara el entorno de prueba generando un UUID determinista para el contexto
     * de la ejecución y construyendo una instancia de {@link Cliente} con datos ficticios
     * pero consistentes con las reglas de negocio.</p>
     */
    @BeforeEach
    void setUp() {
        idPrueba = UUID.randomUUID();
        clienteEjemplo = Cliente.builder()
                .id(idPrueba)
                .cuil("20-40123456-8")
                .nombre("Juan Perez")
                .razonSocial("Juan Perez SA")
                .direccion("Av. Belgrano 1234")
                .telefono("+54-388-123456")
                .email("ab@gmail.com")
                .build();
    }

    /**
     * Prueba el flujo exitoso de creación de un cliente.
     *
     * <p>Verifica que, cuando las comprobaciones de unicidad (CUIL y Email) pasan exitosamente,
     * el servicio invoca el método de guardado del repositorio y retorna la entidad persistida.</p>
     */
    @Test
    @DisplayName("Debe registrar cliente cuando CUIL y Email son unicos")
    void crearCliente_Exitoso() {
        when(clienteRepository.findByCuil(clienteEjemplo.getCuil())).thenReturn(Optional.empty());
        when(clienteRepository.findByEmail(clienteEjemplo.getEmail())).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteEjemplo);

        Cliente resultado = clienteService.crearCliente(clienteEjemplo);

        assertNotNull(resultado);
        assertEquals(clienteEjemplo.getCuil(), resultado.getCuil());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    /**
     * Prueba el rechazo de creación por duplicidad de CUIL.
     *
     * <p>Asegura que si el repositorio detecta que el CUIL ya existe en el sistema,
     * el servicio interrumpe la operación lanzando una {@link RecursoDuplicadoException}
     * y nunca intenta persistir la entidad.</p>
     */
    @Test
    @DisplayName("Debe fallar al registrar si el CUIL ya existe")
    void crearCliente_Falla_CuilDuplicado() {
        when(clienteRepository.findByCuil(clienteEjemplo.getCuil())).thenReturn(Optional.of(clienteEjemplo));

        assertThrows(RecursoDuplicadoException.class, () -> clienteService.crearCliente(clienteEjemplo));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    /**
     * Prueba la recuperación exitosa de un cliente específico.
     *
     * <p>Verifica que si el repositorio encuentra una coincidencia para el UUID proporcionado,
     * el servicio retorna los datos intactos del cliente buscado.</p>
     */
    @Test
    @DisplayName("Debe recuperar el cliente por su identificador UUID")
    void obtenerPorId_Exitoso() {
        when(clienteRepository.findById(idPrueba)).thenReturn(Optional.of(clienteEjemplo));

        Cliente resultado = clienteService.obtenerClientePorId(idPrueba);

        assertNotNull(resultado);
        assertEquals(idPrueba, resultado.getId());
    }

    /**
     * Prueba el manejo de errores al consultar un cliente inexistente.
     *
     * <p>Verifica que solicitar un UUID que no tiene correspondencia en la base de datos
     * resulta en la emisión correcta de una {@link RecursoNoEncontradoException}.</p>
     */
    @Test
    @DisplayName("Debe lanzar excepcion si el ID buscado no existe")
    void obtenerPorId_Falla_NoExiste() {
        UUID idInexistente = UUID.randomUUID();
        when(clienteRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> clienteService.obtenerClientePorId(idInexistente));
    }
}