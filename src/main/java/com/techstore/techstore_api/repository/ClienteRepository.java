package com.techstore.techstore_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techstore.techstore_api.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByDni(String dni);
    Boolean existsByDni(String dni);
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
}