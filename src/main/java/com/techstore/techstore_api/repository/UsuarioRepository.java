package com.techstore.techstore_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techstore.techstore_api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
    Boolean existsByUsername(String username);
    @Query("SELECT u FROM Usuario u WHERE UPPER(u.estado) = UPPER(:estado)")
    List<Usuario> findByEstadoIgnoreCase(@Param("estado") String estado);
}