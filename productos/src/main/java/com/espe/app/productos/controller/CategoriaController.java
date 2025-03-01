package com.espe.app.productos.controller;

import com.espe.app.productos.models.Categoria;
import com.espe.app.productos.models.Producto;
import com.espe.app.productos.services.CategoriaService;
import com.espe.app.productos.services.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final ProductoService productoService;

    public CategoriaController(CategoriaService categoriaService, ProductoService productoService) {
        this.categoriaService = categoriaService;
        this.productoService = productoService;
    }

    @GetMapping
    public List<Categoria> getAll() {
        return categoriaService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Categoria> getById(@PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);
        return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public Categoria create(@RequestBody Categoria categoria) {
        return categoriaService.save(categoria);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Categoria> update(@PathVariable Long id, @RequestBody Categoria categoria) {
        Categoria existing = categoriaService.findById(id);
        if (existing != null) {
            categoria.setIdCategoria(Math.toIntExact(id));
            return ResponseEntity.ok(categoriaService.save(categoria));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
            categoriaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    @GetMapping("/clasificar/{idCategoria}")
    public ResponseEntity<List<Producto>> getByCategoriaId(@PathVariable Long idCategoria) {
        List<Producto> productos = productoService.findByCategoriaId(idCategoria);
        return productos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(productos);
    }

}

