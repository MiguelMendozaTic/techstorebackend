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
import com.techstore.techstore_api.model.Producto;
import com.techstore.techstore_api.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        List<Producto> productos = productoService.findAll();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> getProductosActivos() {
        List<Producto> productos = productoService.findByEstado("ACTIVO");
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<Producto>> getProductosInactivos() {
        List<Producto> productos = productoService.findByEstado("INACTIVO");
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductoById(@PathVariable("id") Integer id) {
        Integer productoId = Objects.requireNonNull(id, "El ID no puede ser null");
        Optional<Producto> producto = productoService.findById(productoId);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Producto no encontrado"));
    }

    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
        Producto productoGuardado = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducto(@PathVariable("id") Integer id, @RequestBody Producto producto) {
        Integer productoId = Objects.requireNonNull(id, "El ID no puede ser null");
        if (!productoService.findById(productoId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Producto no encontrado"));
        }
        producto.setId(productoId);
        Producto productoActualizado = productoService.save(producto);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable("id") Integer id) {
        Integer productoId = Objects.requireNonNull(id, "El ID no puede ser null");
        if (!productoService.findById(productoId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Producto no encontrado"));
        }
        productoService.deleteById(productoId);
        return ResponseEntity.ok(Map.of("message", "Producto eliminado correctamente"));
    }

    @GetMapping("/bajo-stock")
    public ResponseEntity<List<Producto>> getProductosBajoStock() {
        List<Producto> productos = productoService.findProductosBajoStock();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarProductos(@RequestParam("nombre") String nombre) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/buscar-avanzado")
    public ResponseEntity<List<Producto>> buscarAvanzado(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "categoriaId", required = false) Integer categoriaId) {

        List<Producto> productos;

        if (nombre != null && categoriaId != null) {
            productos = productoService.buscarPorNombreYCategoria(nombre, categoriaId);
        } else if (nombre != null) {
            productos = productoService.buscarPorNombre(nombre);
        } else if (categoriaId != null) {
            productos = productoService.findByCategoriaId(categoriaId);
        } else {
            productos = productoService.findByEstado("ACTIVO");
        }

        return ResponseEntity.ok(productos);
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> getProductosPorCategoria(
            @PathVariable("categoriaId") Integer categoriaId) {
        List<Producto> productos = productoService.findByCategoriaId(categoriaId);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/verificar")
    public ResponseEntity<Boolean> verificarProductoExistente(
            @RequestParam String nombre,
            @RequestParam Integer categoriaId,
            @RequestParam(required = false) Integer productoId) {
        try {
            boolean existe = productoService.existeProductoConMismoNombreEnCategoria(nombre, categoriaId, productoId);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(true);
        }
    }
}