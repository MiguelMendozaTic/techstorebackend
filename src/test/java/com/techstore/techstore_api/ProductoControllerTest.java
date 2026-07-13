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

import com.techstore.techstore_api.web.ProductoController;
import com.techstore.techstore_api.model.Producto;
import com.techstore.techstore_api.service.ProductoService;

class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProductos_ListaProductos_RetornaTodosLosProductos() {
        Producto laptop = new Producto("Laptop", "Dell", "Inspiron 15", "Laptop básica",
                10, 800.0, 1200.0, "unidad", 1, 5);
        Producto mouse = new Producto("Mouse", "Logitech", "M100", "Mouse óptico",
                50, 10.0, 25.0, "unidad", 1, 10);
        List<Producto> productos = Arrays.asList(laptop, mouse);

        when(productoService.findAll()).thenReturn(productos);

        ResponseEntity<List<Producto>> response = productoController.getAllProductos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testCreateProducto_NuevoProducto_ProductoCreadoExitosamente() {
        Producto nuevoProducto = new Producto("Teclado", "HP", "K200", "Teclado estándar",
                30, 15.0, 35.0, "unidad", 1, 5);

        when(productoService.save(any(Producto.class))).thenReturn(nuevoProducto);

        ResponseEntity<Producto> response = productoController.createProducto(nuevoProducto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetProductosActivos_ListaActivos_RetornaProductosActivos() {
        Producto productoActivo = new Producto("Monitor", "Samsung", "S24", "Monitor Full HD",
                20, 150.0, 250.0, "unidad", 2, 5);
        productoActivo.setEstado("ACTIVO");
        List<Producto> productosActivos = Arrays.asList(productoActivo);

        when(productoService.findByEstado("ACTIVO")).thenReturn(productosActivos);

        ResponseEntity<List<Producto>> response = productoController.getProductosActivos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testBuscarProductos_PorNombre_RetornaProductosCoincidentes() {
        Producto producto = new Producto("Auriculares", "Sony", "WH-1000XM4", "Auriculares bluetooth",
                15, 100.0, 180.0, "unidad", 3, 5);
        List<Producto> productos = Arrays.asList(producto);

        when(productoService.buscarPorNombre("Auriculares")).thenReturn(productos);

        ResponseEntity<List<Producto>> response = productoController.buscarProductos("Auriculares");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }
}