package com.techstore.techstore_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techstore.techstore_api.model.Cliente;
import com.techstore.techstore_api.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(@NonNull Integer id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> findByDni(String dni) {
        return clienteRepository.findByDni(dni);
    }

    public Cliente save(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del cliente es requerido");
        }

        if (cliente.getDni() == null || cliente.getDni().trim().isEmpty()) {
            throw new RuntimeException("El DNI del cliente es requerido");
        }

        if (cliente.getDni().length() != 8) {
            throw new RuntimeException("El DNI debe tener 8 dígitos");
        }

        if (!cliente.getDni().matches("\\d+")) {
            throw new RuntimeException("El DNI debe contener solo números");
        }

        if (cliente.getNombre().length() > 100) {
            throw new RuntimeException("El nombre no puede exceder los 100 caracteres");
        }

        if (cliente.getCorreo() != null && !cliente.getCorreo().trim().isEmpty()) {
            if (cliente.getCorreo().length() > 100) {
                throw new RuntimeException("El correo electrónico no puede exceder los 100 caracteres");
            }
            if (!cliente.getCorreo().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new RuntimeException("El formato del correo electrónico es inválido");
            }
        }

        if (cliente.getTelefono() != null && !cliente.getTelefono().trim().isEmpty()) {
            if (cliente.getTelefono().length() > 15) {
                throw new RuntimeException("El teléfono no puede exceder los 15 caracteres");
            }
            if (!cliente.getTelefono().matches("\\d+")) {
                throw new RuntimeException("El teléfono debe contener solo números");
            }
        }

        if (cliente.getDireccion() != null && !cliente.getDireccion().trim().isEmpty()) {
            if (cliente.getDireccion().length() > 150) {
                throw new RuntimeException("La dirección no puede exceder los 150 caracteres");
            }
        }

        if (cliente.getId() == null) {
            cliente.setFechaRegistro(LocalDateTime.now());
            if (clienteRepository.existsByDni(cliente.getDni())) {
                throw new RuntimeException("Ya existe un cliente registrado con el DNI: " + cliente.getDni());
            }
        }

        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente crearCliente(Cliente cliente) {
        return this.save(cliente);
    }

    @Transactional
    public Cliente actualizarCliente(@NonNull Integer id, Cliente clienteActualizado) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (!clienteOpt.isPresent()) {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }

        Cliente cliente = clienteOpt.get();

        if (clienteActualizado.getNombre() == null || clienteActualizado.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del cliente es requerido");
        }

        if (clienteActualizado.getNombre().length() > 100) {
            throw new RuntimeException("El nombre no puede exceder los 100 caracteres");
        }

        if (clienteActualizado.getCorreo() != null && !clienteActualizado.getCorreo().trim().isEmpty()) {
            if (clienteActualizado.getCorreo().length() > 100) {
                throw new RuntimeException("El correo electrónico no puede exceder los 100 caracteres");
            }
            if (!clienteActualizado.getCorreo().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new RuntimeException("El formato del correo electrónico es inválido");
            }
        }

        if (clienteActualizado.getTelefono() != null && !clienteActualizado.getTelefono().trim().isEmpty()) {
            if (clienteActualizado.getTelefono().length() > 15) {
                throw new RuntimeException("El teléfono no puede exceder los 15 caracteres");
            }
            if (!clienteActualizado.getTelefono().matches("\\d+")) {
                throw new RuntimeException("El teléfono debe contener solo números");
            }
        }

        if (clienteActualizado.getDireccion() != null && !clienteActualizado.getDireccion().trim().isEmpty()) {
            if (clienteActualizado.getDireccion().length() > 150) {
                throw new RuntimeException("La dirección no puede exceder los 150 caracteres");
            }
        }

        cliente.setNombre(clienteActualizado.getNombre());
        cliente.setCorreo(clienteActualizado.getCorreo());
        cliente.setTelefono(clienteActualizado.getTelefono());
        cliente.setDireccion(clienteActualizado.getDireccion());

        return clienteRepository.save(cliente);
    }

    public void deleteById(@NonNull Integer id) {
        clienteRepository.findById(id).ifPresent(cliente -> {
            cliente.setEstado("INACTIVO");
            clienteRepository.save(cliente);
        });
    }

    public void reactivarById(@NonNull Integer id) {
        clienteRepository.findById(id).ifPresent(cliente -> {
            cliente.setEstado("ACTIVO");
            clienteRepository.save(cliente);
        });
    }
    public boolean existsByDni(String dni) {
        return clienteRepository.existsByDni(dni);
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }
}