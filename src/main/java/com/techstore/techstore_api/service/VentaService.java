package com.techstore.techstore_api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techstore.techstore_api.web.dto.VentaRequest;
import com.techstore.techstore_api.web.dto.VentaResponse;
import com.techstore.techstore_api.model.DetalleVenta;
import com.techstore.techstore_api.model.Producto;
import com.techstore.techstore_api.model.Venta;
import com.techstore.techstore_api.repository.DetalleVentaRepository;
import com.techstore.techstore_api.repository.VentaRepository;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoService productoService;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    public VentaService(VentaRepository ventaRepository,
                        DetalleVentaRepository detalleVentaRepository,
                        ProductoService productoService,
                        ClienteService clienteService,
                        UsuarioService usuarioService) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoService = productoService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> findById(@NonNull Integer id) {
        return ventaRepository.findById(id);
    }

    @Transactional
    public VentaResponse crearVenta(VentaRequest ventaRequest) {
        Integer clienteId = Objects.requireNonNull(
            ventaRequest.getClienteId(), "El ID del cliente no puede ser null");
        if (!clienteService.findById(clienteId).isPresent()) {
            throw new RuntimeException("Cliente no encontrado");
        }

        Integer usuarioId = Objects.requireNonNull(
            ventaRequest.getUsuarioId(), "El ID del usuario no puede ser null");
        if (!usuarioService.findById(usuarioId).isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (ventaRequest.getDetalles() == null || ventaRequest.getDetalles().isEmpty()) {
            throw new RuntimeException("La venta debe tener al menos un producto");
        }

        Venta venta = new Venta();
        venta.setClienteId(clienteId);
        venta.setUsuarioId(usuarioId);
        venta.setTipoPago(ventaRequest.getTipoPago());
        venta.setFecha(LocalDateTime.now());
        venta.setEstado("COMPLETADA");

        double subtotalConIgv = 0;

        for (var detalleRequest : ventaRequest.getDetalles()) {
            Integer productoId = Objects.requireNonNull(
                detalleRequest.getProductoId(), "El ID del producto no puede ser null");
            Optional<Producto> productoOpt = productoService.findById(productoId);
            if (!productoOpt.isPresent()) {
                throw new RuntimeException("Producto no encontrado: " + productoId);
            }

            Producto producto = productoOpt.get();

            if (producto.getCantidad() < detalleRequest.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre() +
                                          ". Stock disponible: " + producto.getCantidad());
            }

            if (!"ACTIVO".equals(producto.getEstado())) {
                throw new RuntimeException("El producto no está disponible: " + producto.getNombre());
            }

            Double precioUnitario = detalleRequest.getPrecioUnitario();
            if (precioUnitario == null || precioUnitario <= 0) {
                precioUnitario = producto.getPrecioVenta();
            }

            if (precioUnitario == null || precioUnitario <= 0) {
                throw new RuntimeException("Precio unitario inválido para el producto: " + producto.getNombre());
            }

            double subtotalDetalle = detalleRequest.getCantidad() * precioUnitario;
            subtotalConIgv += subtotalDetalle;

            producto.setCantidad(producto.getCantidad() - detalleRequest.getCantidad());
            productoService.save(producto);
        }

        double subtotalSinIgv = subtotalConIgv / 1.18;
        double igv = subtotalConIgv - subtotalSinIgv;
        double total = subtotalConIgv;

        venta.setSubtotal(subtotalSinIgv);
        venta.setIgv(igv);
        venta.setTotal(total);

        Venta ventaGuardada = ventaRepository.save(venta);

        for (var detalleRequest : ventaRequest.getDetalles()) {
            Integer productoId = Objects.requireNonNull(
                detalleRequest.getProductoId(), "El ID del producto no puede ser null");
            Producto producto = productoService.findById(productoId).get();
            Double precioUnitario = detalleRequest.getPrecioUnitario() != null ?
                    detalleRequest.getPrecioUnitario() : producto.getPrecioVenta();

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVentaId(ventaGuardada.getId());
            detalle.setProductoId(productoId);
            detalle.setCantidad(detalleRequest.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(detalleRequest.getCantidad() * precioUnitario);

            detalleVentaRepository.save(detalle);
        }

        return obtenerVentaCompleta(Objects.requireNonNull(
            ventaGuardada.getId(), "El ID de la venta guardada no puede ser null"));
    }

    public VentaResponse obtenerVentaCompleta(@NonNull Integer ventaId) {
        Optional<Venta> ventaOpt = ventaRepository.findById(ventaId);
        if (!ventaOpt.isPresent()) {
            throw new RuntimeException("Venta no encontrada");
        }

        Venta venta = ventaOpt.get();

        Integer clienteId = Objects.requireNonNull(
            venta.getClienteId(), "El ID del cliente no puede ser null");
        clienteService.findById(clienteId).ifPresent(venta::setCliente);

        Integer usuarioId = Objects.requireNonNull(
            venta.getUsuarioId(), "El ID del usuario no puede ser null");
        usuarioService.findById(usuarioId).ifPresent(venta::setUsuario);

        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(ventaId);
        for (DetalleVenta detalle : detalles) {
            Integer productoId = Objects.requireNonNull(
                detalle.getProductoId(), "El ID del producto no puede ser null");
            productoService.findById(productoId).ifPresent(detalle::setProducto);
        }
        venta.setDetalles(detalles);

        return new VentaResponse(venta);
    }

    public List<VentaResponse> obtenerTodasLasVentasCompletas() {
        List<Venta> ventas = ventaRepository.findAll();
        return ventas.stream()
                .map(venta -> obtenerVentaCompleta(Objects.requireNonNull(
                    venta.getId(), "El ID de la venta no puede ser null")))
                .collect(Collectors.toList());
    }

    @Transactional
    public Venta cancelarVenta(@NonNull Integer ventaId) {
        Optional<Venta> ventaOpt = ventaRepository.findById(ventaId);
        if (!ventaOpt.isPresent()) {
            throw new RuntimeException("Venta no encontrada");
        }

        Venta venta = ventaOpt.get();
        if ("CANCELADA".equals(venta.getEstado())) {
            throw new RuntimeException("La venta ya está cancelada");
        }

        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(ventaId);
        for (DetalleVenta detalle : detalles) {
            Integer productoId = Objects.requireNonNull(
                detalle.getProductoId(), "El ID del producto no puede ser null");
            Optional<Producto> productoOpt = productoService.findById(productoId);
            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();
                producto.setCantidad(producto.getCantidad() + detalle.getCantidad());
                productoService.save(producto);
            }
        }

        venta.setEstado("CANCELADA");
        return ventaRepository.save(venta);
    }

    public List<Venta> findByClienteId(Integer clienteId) {
        return ventaRepository.findByClienteId(clienteId);
    }

    public List<Venta> findByUsuarioId(Integer usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId);
    }

    public List<Venta> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    public List<Venta> findByEstado(String estado) {
        return ventaRepository.findByEstado(estado);
    }

    public List<Venta> findByTipoPago(String tipoPago) {
        return ventaRepository.findByTipoPago(tipoPago);
    }

    public List<Venta> findVentasDelDia() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioDia = hoy.atStartOfDay();
        LocalDateTime finDia = hoy.atTime(LocalTime.MAX);
        return ventaRepository.findByFechaBetween(inicioDia, finDia);
    }

    public Double obtenerTotalVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Double total = ventaRepository.findTotalVentasByFecha(fechaInicio, fechaFin);
        return total != null ? total : 0.0;
    }

    public List<Object[]> obtenerProductosMasVendidos() {
        return detalleVentaRepository.findProductosMasVendidos();
    }

    public List<Object[]> obtenerProductosMasVendidosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return detalleVentaRepository.findProductosMasVendidosPorFecha(fechaInicio, fechaFin);
    }

    public List<Object[]> obtenerVentasPorTipoPago(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findVentasPorTipoPago(fechaInicio, fechaFin);
    }
}