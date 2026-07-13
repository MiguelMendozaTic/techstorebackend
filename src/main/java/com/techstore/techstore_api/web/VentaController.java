package com.techstore.techstore_api.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.techstore.techstore_api.web.dto.VentaRequest;
import com.techstore.techstore_api.web.dto.VentaResponse;
import com.techstore.techstore_api.model.Venta;
import com.techstore.techstore_api.service.VentaService;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "https://techstorefrontend-six.vercel.app")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<VentaResponse>> getAllVentas() {
        List<VentaResponse> ventas = ventaService.obtenerTodasLasVentasCompletas();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVentaById(@PathVariable("id") Integer id) {
        try {
            Integer ventaId = Objects.requireNonNull(id, "El ID no puede ser null");
            VentaResponse venta = ventaService.obtenerVentaCompleta(ventaId);
            return ResponseEntity.ok(venta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createVenta(@RequestBody VentaRequest ventaRequest) {
        try {
            VentaResponse ventaCreada = ventaService.crearVenta(ventaRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaCreada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear la venta: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarVenta(@PathVariable("id") Integer id) {
        try {
            Integer ventaId = Objects.requireNonNull(id, "El ID no puede ser null");
            Venta ventaCancelada = ventaService.cancelarVenta(ventaId);
            return ResponseEntity.ok(Map.of(
                "message", "Venta cancelada correctamente",
                "venta", ventaCancelada
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venta>> getVentasByCliente(@PathVariable("clienteId") Integer clienteId) {
        List<Venta> ventas = ventaService.findByClienteId(clienteId);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Venta>> getVentasByUsuario(@PathVariable("usuarioId") Integer usuarioId) {
        List<Venta> ventas = ventaService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Venta>> getVentasByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<Venta> ventas = ventaService.findByFechaBetween(inicio, fin);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/hoy")
    public ResponseEntity<List<Venta>> getVentasDelDia() {
        List<Venta> ventas = ventaService.findVentasDelDia();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Venta>> getVentasByEstado(@PathVariable("estado") String estado) {
        List<Venta> ventas = ventaService.findByEstado(estado);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/pago/{tipoPago}")
    public ResponseEntity<List<Venta>> getVentasByTipoPago(@PathVariable("tipoPago") String tipoPago) {
        List<Venta> ventas = ventaService.findByTipoPago(tipoPago);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/reportes/productos-mas-vendidos")
    public ResponseEntity<List<Object[]>> getProductosMasVendidos() {
        List<Object[]> productos = ventaService.obtenerProductosMasVendidos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/reportes/productos-mas-vendidos-fecha")
    public ResponseEntity<List<Object[]>> getProductosMasVendidosPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<Object[]> productos = ventaService.obtenerProductosMasVendidosPorFecha(inicio, fin);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/reportes/ventas-por-pago")
    public ResponseEntity<List<Object[]>> getVentasPorTipoPago(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<Object[]> ventas = ventaService.obtenerVentasPorTipoPago(inicio, fin);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/reportes/total-ventas")
    public ResponseEntity<Map<String, Object>> getTotalVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        Double total = ventaService.obtenerTotalVentasPorFecha(inicio, fin);
        return ResponseEntity.ok(Map.of(
            "fechaInicio", inicio,
            "fechaFin", fin,
            "totalVentas", total != null ? total : 0.0
        ));
    }
}