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
import com.techstore.techstore_api.model.Categoria;
import com.techstore.techstore_api.service.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "https://techstorefrontend-six.vercel.app")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        List<Categoria> categorias = categoriaService.findAll();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Categoria>> getCategoriasActivas() {
        List<Categoria> categorias = categoriaService.findByEstado("ACTIVO");
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/inactivas")
    public ResponseEntity<List<Categoria>> getCategoriasInactivas() {
        List<Categoria> categorias = categoriaService.findByEstado("INACTIVO");
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoriaById(@PathVariable("id") Integer id) {
        Integer categoriaId = Objects.requireNonNull(id, "El ID no puede ser null");
        Optional<Categoria> categoria = categoriaService.findById(categoriaId);
        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Categoría no encontrada"));
    }

    @PostMapping
    public ResponseEntity<?> createCategoria(@RequestBody Categoria categoria) {
        if (categoriaService.existsByNombre(categoria.getNombre())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ya existe una categoría con ese nombre"));
        }
        Categoria categoriaGuardada = categoriaService.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaGuardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoria(@PathVariable("id") Integer id, @RequestBody Categoria categoria) {
        Integer categoriaId = Objects.requireNonNull(id, "El ID no puede ser null");
        if (!categoriaService.findById(categoriaId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Categoría no encontrada"));
        }
        categoria.setId(categoriaId);
        Categoria categoriaActualizada = categoriaService.save(categoria);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable("id") Integer id) {
        Integer categoriaId = Objects.requireNonNull(id, "El ID no puede ser null");
        if (!categoriaService.findById(categoriaId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Categoría no encontrada"));
        }
        categoriaService.deleteById(categoriaId);
        return ResponseEntity.ok(Map.of("message", "Categoría eliminada correctamente"));
    }

    @GetMapping("/verificar")
    public ResponseEntity<Boolean> verificarCategoriaExistente(
            @RequestParam String nombre,
            @RequestParam(required = false) Integer categoriaId) {
        try {
            boolean existe = categoriaService.verificarCategoriaExistente(nombre, categoriaId);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(true);
        }
    }
}