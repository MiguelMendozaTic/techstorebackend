package com.techstore.techstore_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.techstore.techstore_api.web.ClienteController;
import com.techstore.techstore_api.model.Cliente;
import com.techstore.techstore_api.service.ClienteService;

class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClientes_ListaClientes_RetornaTodosLosClientes() {
        Cliente cliente1 = new Cliente("Juan Pérez", "12345678", "juan@email.com", "123456789", "Calle 123");
        Cliente cliente2 = new Cliente("María García", "87654321", "maria@email.com", "987654321", "Avenida 456");
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

        when(clienteService.findAll()).thenReturn(clientes);

        ResponseEntity<List<Cliente>> response = clienteController.getAllClientes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testCreateCliente_NuevoCliente_ClienteCreadoExitosamente() {
        Cliente nuevoCliente = new Cliente("Carlos López", "11223344", "carlos@email.com", "555555555", "Calle 789");

        when(clienteService.crearCliente(any(Cliente.class))).thenReturn(nuevoCliente);

        ResponseEntity<?> response = clienteController.createCliente(nuevoCliente);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateCliente_ClienteConDniExistente_RetornaErrorDuplicado() {
        Cliente clienteDuplicado = new Cliente("Ana Martínez", "12345678", "ana@email.com", "666666666", "Calle 000");

        when(clienteService.crearCliente(any(Cliente.class)))
                .thenThrow(new RuntimeException("Ya existe un cliente registrado con el DNI: 12345678"));

        ResponseEntity<?> response = clienteController.createCliente(clienteDuplicado);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testBuscarClientes_PorNombre_RetornaClientesCoincidentes() {
        Cliente cliente = new Cliente("Juan Pérez", "12345678", "juan@email.com", "123456789", "Calle 123");
        List<Cliente> clientes = Arrays.asList(cliente);

        when(clienteService.buscarPorNombre("Juan")).thenReturn(clientes);

        ResponseEntity<List<Cliente>> response = clienteController.buscarClientes("Juan");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }
}