package com.techstore.techstore_api.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techstore.techstore_api.model.Usuario;
import com.techstore.techstore_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> findByEstado(String estado) {
        return usuarioRepository.findByEstadoIgnoreCase(estado);
    }

    public Optional<Usuario> findById(@NonNull Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario save(Usuario usuario) {
        if (usuario.getId() != null) {
            Integer id = Objects.requireNonNull(
                usuario.getId(), "El ID del usuario no puede ser null");
            Optional<Usuario> usuarioExistenteOpt = usuarioRepository.findById(id);
            if (usuarioExistenteOpt.isPresent()) {
                Usuario usuarioExistente = usuarioExistenteOpt.get();
                if (usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty()) {
                    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                } else {
                    usuario.setPassword(usuarioExistente.getPassword());
                }
            }
        } else {
            if (usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            } else {
                throw new IllegalArgumentException("La contraseña es requerida para nuevos usuarios");
            }
        }

        if (usuario.getEstado() == null) {
            usuario.setEstado("ACTIVO");
        }
        return usuarioRepository.save(usuario);
    }

    public void deleteById(@NonNull Integer id) {
        usuarioRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }
}