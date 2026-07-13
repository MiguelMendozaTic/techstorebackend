package com.techstore.techstore_api.service;

import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import com.techstore.techstore_api.model.Categoria;
import com.techstore.techstore_api.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }
    
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> findByEstado(String estado) {
        return categoriaRepository.findByEstado(estado);
    }

    public Optional<Categoria> findById(@NonNull Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria save(@NonNull Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void deleteById(@NonNull Integer id) {
        categoriaRepository.deleteById(id);
    }

    public boolean existsByNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }

    public boolean existsByNombreAndIdNot(String nombre, Integer id) {
        return categoriaRepository.existsByNombreAndIdNot(nombre, id);
    }

    public boolean verificarCategoriaExistente(String nombre, Integer categoriaId) {
        if (categoriaId != null) {
            return existsByNombreAndIdNot(nombre, categoriaId);
        } else {
            return existsByNombre(nombre);
        }
    }
}