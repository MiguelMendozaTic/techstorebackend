package com.techstore.techstore_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.techstore.techstore_api.model.Producto;
import com.techstore.techstore_api.repository.ProductoRepository;

@Service
public class ProductoService {

   private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public List<Producto> findByEstado(String estado) {
        return productoRepository.findByEstado(estado);
    }

    public Optional<Producto> findById(@NonNull Integer id) {
        return productoRepository.findById(id);
    }

    public Producto save(Producto producto) {
        if (producto.getId() == null) {
            producto.setFechaCreacion(LocalDateTime.now());
        }
        producto.setFechaActualizacion(LocalDateTime.now());
        return productoRepository.save(producto);
    }

    public void deleteById(@NonNull Integer id) {
        productoRepository.deleteById(id);
    }

    public List<Producto> findProductosBajoStock() {
        return productoRepository.findProductosBajoStock();
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> buscarPorNombreYCategoria(String nombre, Integer categoriaId) {
        return productoRepository.findByNombreContainingIgnoreCaseAndCategoriaId(nombre, categoriaId);
    }

    public List<Producto> findByCategoriaId(Integer categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public boolean existeProductoConMismoNombreEnCategoria(String nombre, Integer categoriaId, Integer productoId) {
        if (productoId != null) {
            return productoRepository.existsByNombreAndCategoriaIdAndIdNot(nombre, categoriaId, productoId);
        } else {
            return productoRepository.existsByNombreAndCategoriaId(nombre, categoriaId);
        }
    }
}