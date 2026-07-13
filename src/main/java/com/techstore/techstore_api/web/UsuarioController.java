package com.techstore.techstore_api.web;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.techstore.techstore_api.model.Usuario;
import com.techstore.techstore_api.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Usuario>> getUsuariosPorEstado(@PathVariable("estado") String estado) {
        List<Usuario> usuarios = usuarioService.findByEstado(estado);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable("id") Integer id) {
        Integer usuarioId = Objects.requireNonNull(id, "El ID no puede ser null");
        Optional<Usuario> usuario = usuarioService.findById(usuarioId);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Usuario no encontrado"));
    }

    @PostMapping
    public ResponseEntity<?> createUsuario(@RequestBody Usuario usuario) {
        try {
            System.out.println("POST /api/usuarios recibido");
            System.out.println("Datos recibidos: " + usuario.getNombre() + ", " + usuario.getUsername());
            System.out.println("Password recibida: " + (usuario.getPassword() != null ? "PRESENTE" : "AUSENTE"));

            if (usuarioService.existsByUsername(usuario.getUsername())) {
                System.out.println("Usuario ya existe: " + usuario.getUsername());
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Ya existe un usuario con ese nombre de usuario"));
            }

            Usuario usuarioGuardado = usuarioService.save(usuario);
            System.out.println("Usuario creado exitosamente: " + usuarioGuardado.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);

        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.out.println("Error interno: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear el usuario: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable("id") Integer id, @RequestBody Usuario usuario) {
        try {
            System.out.println("PUT /api/usuarios/" + id + " recibido");
            Integer usuarioId = Objects.requireNonNull(id, "El ID no puede ser null");
            if (!usuarioService.findById(usuarioId).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado"));
            }
            usuario.setId(usuarioId);
            Usuario usuarioActualizado = usuarioService.save(usuario);
            System.out.println("Usuario actualizado exitosamente: " + usuarioId);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            System.out.println("Error actualizando usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar el usuario"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable("id") Integer id) {
        Integer usuarioId = Objects.requireNonNull(id, "El ID no puede ser null");
        if (!usuarioService.findById(usuarioId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }
        usuarioService.deleteById(usuarioId);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado correctamente"));
    }
}