package com.techstore.techstore_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.techstore.techstore_api.web.UsuarioController;
import com.techstore.techstore_api.model.Usuario;
import com.techstore.techstore_api.service.UsuarioService;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsuarios_ListaUsuarios_RetornaTodosLosUsuarios() {
        Usuario admin = new Usuario("Admin", "admin", "password", "ADMIN");
        Usuario user = new Usuario("Usuario", "user", "password", "USER");
        List<Usuario> usuarios = Arrays.asList(admin, user);

        when(usuarioService.findAll()).thenReturn(usuarios);

        ResponseEntity<List<Usuario>> response = usuarioController.getAllUsuarios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testUpdateUsuario_UsuarioExistente_UsuarioActualizadoExitosamente() {
        Usuario usuarioExistente = new Usuario("Admin", "admin", "password", "ADMIN");
        usuarioExistente.setId(1);

        Usuario usuarioActualizado = new Usuario("Admin Modificado", "admin", "nuevoPassword", "ADMIN");

        when(usuarioService.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        ResponseEntity<?> response = usuarioController.updateUsuario(1, usuarioActualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateUsuario_UsuarioNoExistente_RetornaErrorNoEncontrado() {
        Usuario usuario = new Usuario("Usuario", "user", "password", "USER");

        when(usuarioService.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = usuarioController.updateUsuario(1, usuario);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteUsuario_UsuarioExistente_UsuarioEliminadoExitosamente() {
        Usuario usuario = new Usuario("Usuario", "user", "password", "USER");
        usuario.setId(1);

        when(usuarioService.findById(1)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioService).deleteById(1);

        ResponseEntity<?> response = usuarioController.deleteUsuario(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}