package com.techstore.techstore_api.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.techstore.techstore_api.model.Usuario;
import com.techstore.techstore_api.security.JwtUtil;
import com.techstore.techstore_api.service.UsuarioService;
import com.techstore.techstore_api.web.dto.LoginRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(loginRequest.getUsername());

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                if (passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())
                        && "ACTIVO".equals(usuario.getEstado())) {

                    String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRol());

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("usuario", Map.of(
                        "id", usuario.getId(),
                        "nombre", usuario.getNombre(),
                        "username", usuario.getUsername(),
                        "rol", usuario.getRol()
                    ));

                    return ResponseEntity.ok(response);
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas o usuario inactivo"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        try {
            if (usuarioService.existsByUsername(usuario.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El nombre de usuario ya existe"));
            }

            if (usuario.getRol() == null) {
                usuario.setRol("USER");
            }

            Usuario usuarioGuardado = usuarioService.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al registrar usuario"));
        }
    }
}
