package com.techstore.techstore_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.techstore.techstore_api.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Buscar productos por estado (ACTIVO/INACTIVO)
    List<Producto> findByEstado(String estado);

    // Buscar productos por categoría
    List<Producto> findByCategoriaId(Integer categoriaId);

    // Buscar productos con stock bajo
    @Query("SELECT p FROM Producto p WHERE p.cantidad <= p.stockMinimo AND p.estado = 'ACTIVO'")
    List<Producto> findProductosBajoStock();

    // Buscar productos por nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar productos por nombre Y categoría
    List<Producto> findByNombreContainingIgnoreCaseAndCategoriaId(String nombre, Integer categoriaId);

    boolean existsByNombreAndCategoriaId(String nombre, Integer categoriaId);
    boolean existsByNombreAndCategoriaIdAndIdNot(String nombre, Integer categoriaId, Integer id);
}