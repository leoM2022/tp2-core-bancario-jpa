package com.example.demo.repository;

import com.example.demo.model.ConfiguracionTope;
import com.example.demo.model.RolCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConfiguracionTopeRepository extends JpaRepository<ConfiguracionTope, UUID> {
    Optional<ConfiguracionTope> findByRolCliente(RolCliente rolCliente);
}
