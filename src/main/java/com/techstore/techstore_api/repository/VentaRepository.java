package com.techstore.techstore_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techstore.techstore_api.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    // Buscar ventas por cliente
    List<Venta> findByClienteId(Integer clienteId);

    // Buscar ventas por usuario
    List<Venta> findByUsuarioId(Integer usuarioId);

    // Buscar ventas por fecha
    List<Venta> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Buscar ventas por estado
    List<Venta> findByEstado(String estado);

    // Buscar ventas por tipo de pago
    List<Venta> findByTipoPago(String tipoPago);

    // Obtener total de ventas por rango de fechas
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
    Double findTotalVentasByFecha(@Param("fechaInicio") LocalDateTime fechaInicio,
                                 @Param("fechaFin") LocalDateTime fechaFin);

    // Obtener ventas del día actual - CORREGIDO
    @Query("SELECT v FROM Venta v WHERE FUNCTION('DATE', v.fecha) = CURRENT_DATE")
    List<Venta> findVentasDelDia();

    // Obtener estadísticas de ventas por tipo de pago
    @Query("SELECT v.tipoPago, SUM(v.total) FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin GROUP BY v.tipoPago")
    List<Object[]> findVentasPorTipoPago(@Param("fechaInicio") LocalDateTime fechaInicio,
                                        @Param("fechaFin") LocalDateTime fechaFin);

    // Método alternativo para ventas del día usando fechas específicas
    @Query("SELECT v FROM Venta v WHERE v.fecha >= :inicioDia AND v.fecha < :finDia")
    List<Venta> findVentasDelDiaAlternativo(@Param("inicioDia") LocalDateTime inicioDia,
                                          @Param("finDia") LocalDateTime finDia);
}