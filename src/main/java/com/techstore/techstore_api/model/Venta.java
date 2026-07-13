package com.techstore.techstore_api.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha")
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(name = "total")
    private Double total = 0.0;

    // NUEVOS CAMPOS AGREGADOS
    @Column(name = "subtotal")
    private Double subtotal = 0.0;

    @Column(name = "igv")
    private Double igv = 0.0;

    @Column(name = "cliente_id")
    private Integer clienteId;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "estado", length = 50)
    private String estado = "COMPLETADA";

    @Column(name = "tipo_pago", length = 50)
    private String tipoPago = "EFECTIVO";

    // Relaciones transaccionales
    @Transient
    private Cliente cliente;

    @Transient
    private Usuario usuario;

    @Transient
    private List<DetalleVenta> detalles;

    public Venta() {}

    public Venta(Integer clienteId, Integer usuarioId, String tipoPago) {
        this.clienteId = clienteId;
        this.usuarioId = usuarioId;
        this.tipoPago = tipoPago;
    }

    // GETTERS Y SETTERS
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    // NUEVOS GETTERS Y SETTERS
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getIgv() { return igv; }
    public void setIgv(Double igv) { this.igv = igv; }

    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTipoPago() { return tipoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
}