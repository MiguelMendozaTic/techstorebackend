package com.techstore.techstore_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techstore.techstore_api.model.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

    // Buscar detalles por venta
    List<DetalleVenta> findByVentaId(Integer ventaId);

    // Buscar detalles por producto
    List<DetalleVenta> findByProductoId(Integer productoId);

    // Obtener productos más vendidos - CORREGIDO
    @Query("SELECT dv.productoId, p.nombre, SUM(dv.cantidad) as totalVendido " +
           "FROM DetalleVenta dv " +
           "JOIN Producto p ON dv.productoId = p.id " +
           "GROUP BY dv.productoId, p.nombre " +
           "ORDER BY totalVendido DESC")
    List<Object[]> findProductosMasVendidos();

    // Obtener productos más vendidos por rango de fechas - CORREGIDO
    @Query("SELECT dv.productoId, p.nombre, SUM(dv.cantidad) as totalVendido " +
           "FROM DetalleVenta dv " +
           "JOIN Producto p ON dv.productoId = p.id " +
           "JOIN Venta v ON dv.ventaId = v.id " +
           "WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY dv.productoId, p.nombre " +
           "ORDER BY totalVendido DESC")
    List<Object[]> findProductosMasVendidosPorFecha(@Param("fechaInicio") LocalDateTime fechaInicio,
                                                   @Param("fechaFin") LocalDateTime fechaFin);
}