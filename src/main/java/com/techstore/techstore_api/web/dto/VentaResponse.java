package com.techstore.techstore_api.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.techstore.techstore_api.model.DetalleVenta;
import com.techstore.techstore_api.model.Venta;

public class VentaResponse {
    private Integer id;
    private LocalDateTime fecha;
    private Double total;
    private Double subtotal;  
    private Double igv;       
    private String estado;
    private String tipoPago;
    private ClienteDTO cliente;
    private UsuarioDTO usuario;
    private List<DetalleVentaDTO> detalles;

    public VentaResponse() {}

    public VentaResponse(Venta venta) {
        this.id = venta.getId();
        this.fecha = venta.getFecha();
        this.total = venta.getTotal();
        this.subtotal = venta.getSubtotal();  
        this.igv = venta.getIgv();            
        this.estado = venta.getEstado();
        this.tipoPago = venta.getTipoPago();

        // Cargar cliente
        if (venta.getCliente() != null) {
            this.cliente = new ClienteDTO(venta.getCliente());
        }

        // Cargar usuario
        if (venta.getUsuario() != null) {
            this.usuario = new UsuarioDTO(venta.getUsuario());
        }

        // Cargar detalles
        if (venta.getDetalles() != null) {
            this.detalles = venta.getDetalles().stream()
                    .map(DetalleVentaDTO::new)
                    .collect(Collectors.toList());
        }
    }

    // GETTERS Y SETTERS
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getIgv() { return igv; }
    public void setIgv(Double igv) { this.igv = igv; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTipoPago() { return tipoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }

    public ClienteDTO getCliente() { return cliente; }
    public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }

    public UsuarioDTO getUsuario() { return usuario; }
    public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }

    public List<DetalleVentaDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVentaDTO> detalles) { this.detalles = detalles; }

    // CLASE CLIENTE DTO
    public static class ClienteDTO {
        private Integer id;
        private String nombre;
        private String dni;
        private String correo;
        private String telefono;  

        public ClienteDTO(com.techstore.techstore_api.model.Cliente cliente) {
            this.id = cliente.getId();
            this.nombre = cliente.getNombre();
            this.dni = cliente.getDni();
            this.correo = cliente.getCorreo();
            this.telefono = cliente.getTelefono();
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getDni() { return dni; }
        public void setDni(String dni) { this.dni = dni; }
        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
    }

    // CLASE USUARIO DTO
    public static class UsuarioDTO {
        private Integer id;
        private String nombre;
        private String username;
        private String rol;  

        public UsuarioDTO(com.techstore.techstore_api.model.Usuario usuario) {
            this.id = usuario.getId();
            this.nombre = usuario.getNombre();
            this.username = usuario.getUsername();
            this.rol = usuario.getRol();
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }

    // CLASE DETALLE VENTA DTO
    public static class DetalleVentaDTO {
        private Integer id;
        private Integer cantidad;
        private Double precioUnitario;
        private Double subtotal;
        private ProductoDTO producto;

        public DetalleVentaDTO(DetalleVenta detalle) {
            this.id = detalle.getId();
            this.cantidad = detalle.getCantidad();
            this.precioUnitario = detalle.getPrecioUnitario();
            this.subtotal = detalle.getSubtotal();

            if (detalle.getProducto() != null) {
                this.producto = new ProductoDTO(detalle.getProducto());
            }
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public Double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
        public ProductoDTO getProducto() { return producto; }
        public void setProducto(ProductoDTO producto) { this.producto = producto; }
    }

    // CLASE PRODUCTO DTO
    public static class ProductoDTO {
        private Integer id;
        private String nombre;
        private String descripcion;
        private String unidad;

        public ProductoDTO(com.techstore.techstore_api.model.Producto producto) {
            this.id = producto.getId();
            this.nombre = producto.getNombre();
            this.descripcion = producto.getDescripcion();
            this.unidad = producto.getUnidad();
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public String getUnidad() { return unidad; }
        public void setUnidad(String unidad) { this.unidad = unidad; }
    }
}