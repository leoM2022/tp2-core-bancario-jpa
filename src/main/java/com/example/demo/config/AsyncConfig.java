package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuración para habilitar el procesamiento asíncrono y desacoplado
 * de eventos de dominio mediante hilos de ejecución secundarios.
 *
 * @author Dyevara23 & leoM2022
 * @version 1.0.0
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}