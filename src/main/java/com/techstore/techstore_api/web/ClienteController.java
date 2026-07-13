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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.techstore.techstore_api.model.Cliente;
import com.techstore.techstore_api.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "https://techstorefrontend-six.vercel.app")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClienteById(@PathVariable("id") Integer id) {
        Integer clienteId = Objects.requireNonNull(id, "El ID no puede ser null");
        Optional<Cliente> cliente = clienteService.findById(clienteId);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Cliente no encontrado"));
    }

    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente) {
        try {
            Cliente clienteGuardado = clienteService.crearCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno al crear el cliente"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable("id") Integer id, @RequestBody Cliente cliente) {
        try {
            Integer clienteId = Objects.requireNonNull(id, "El ID no puede ser null");
            if (!clienteService.findById(clienteId).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Cliente no encontrado"));
            }
            Cliente clienteActualizado = clienteService.actualizarCliente(clienteId, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno al actualizar el cliente"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable("id") Integer id) {
        Integer clienteId = Objects.requireNonNull(id, "El ID no puede ser null");
        if (!clienteService.findById(clienteId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Cliente no encontrado"));
        }
        clienteService.deleteById(clienteId);
        return ResponseEntity.ok(Map.of("message", "Cliente desactivado correctamente"));
    }

    @PutMapping("/{id}/reactivar")
    public ResponseEntity<?> reactivarCliente(@PathVariable("id") Integer id) {
        Integer clienteId = Objects.requireNonNull(id, "El ID no puede ser null");
        if (!clienteService.findById(clienteId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Cliente no encontrado"));
        }
        clienteService.reactivarById(clienteId);
        return ResponseEntity.ok(Map.of("message", "Cliente reactivado correctamente"));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarClientes(@RequestParam("nombre") String nombre) {
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> getClienteByDni(@PathVariable("dni") String dni) {
        Optional<Cliente> cliente = clienteService.findByDni(dni);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Cliente no encontrado"));
    }
}