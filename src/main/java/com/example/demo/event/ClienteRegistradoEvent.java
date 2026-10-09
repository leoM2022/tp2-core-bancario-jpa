package com.example.demo.event;

import com.example.demo.model.RolCliente;
import lombok.Getter;

import java.util.UUID;

/**
 * Evento de dominio inmutable disparado cuando un cliente ha sido persistido
 * exitosamente en estado PENDIENTE_ACTIVACION.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Getter
public class ClienteRegistradoEvent {

    private final UUID clienteId;
    private final String nombre;
    private final String email;
    private final RolCliente rolCliente;
    private final String tokenActivacion;

    public ClienteRegistradoEvent(UUID clienteId, String nombre, String email,RolCliente rolCliente, String tokenActivacion) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.email = email;
        this.rolCliente = rolCliente;
        this.tokenActivacion = tokenActivacion;
    }
}