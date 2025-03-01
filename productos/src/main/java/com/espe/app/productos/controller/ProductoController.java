package com.espe.app.productos.controller;

import com.espe.app.productos.models.Producto;
import com.espe.app.productos.services.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> getAll() {
        return productoService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
    }


    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Producto> create(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.save(producto);
        return ResponseEntity.ok(nuevoProducto);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto producto) {
        Producto existing = productoService.findById(id);
        if (existing != null) {
            producto.setIdProducto(Math.toIntExact(id));
            return ResponseEntity.ok(productoService.save(producto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
        @DeleteMapping("/{id}")
        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
        public ResponseEntity<Void> delete (@PathVariable Long id){
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        @PostMapping("/{idProducto}/categorias/{idCategoria}")
        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
        public ResponseEntity<Void> addCategoria (@PathVariable Long idProducto, @PathVariable Long idCategoria){
            productoService.addCategoriaToProducto(idProducto, idCategoria);
            return ResponseEntity.ok().build();
        }

        @DeleteMapping("/{idProducto}/categorias/{idCategoria}")
        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
        public ResponseEntity<Void> removeCategoria (@PathVariable Long idProducto, @PathVariable Long idCategoria){
            productoService.removeCategoriaFromProducto(idProducto, idCategoria);
            return ResponseEntity.ok().build();
        }




}