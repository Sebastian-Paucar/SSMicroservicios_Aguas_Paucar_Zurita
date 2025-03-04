package com.espe.app.productos.controller;

import com.espe.app.productos.models.Producto;
import com.espe.app.productos.services.ProductoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/productos")
@Validated
public class ProductoController {

    private static final String CARPETA_IMAGENES = "/var/lib/postgresql/storage/";

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.obtenerTodos();
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable int id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestPart("producto") Producto producto,
                                           @RequestPart(value = "file", required = false) MultipartFile file,
                                           @RequestParam String emailUsuario) {
        try {
            Producto nuevoProducto = productoService.guardar(producto, file, emailUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            log.error("Error de almacenamiento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la imagen del producto");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable int id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar producto con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para servir imágenes con seguridad
    @GetMapping("/imagenes")
    public ResponseEntity<?> obtenerImagen(@RequestParam String path) {
        try {
            // Asegurar que solo se usa el nombre del archivo, sin la ruta completa
            String nombreImagen = Paths.get(path).getFileName().toString();
            Path rutaImagen = Paths.get(CARPETA_IMAGENES).resolve(nombreImagen).normalize();

            // Verificación mejorada para evitar accesos fuera del directorio permitido
            if (!rutaImagen.startsWith(CARPETA_IMAGENES)) {
                log.warn("Intento de acceso no autorizado a {}", rutaImagen);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Acceso no autorizado a la imagen");
            }

            byte[] imagenBytes = Files.readAllBytes(rutaImagen);
            return ResponseEntity.ok().body(imagenBytes);
        } catch (IOException e) {
            log.error("Imagen no encontrada: {}", path);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Imagen no encontrada");
        }
    }
    @PatchMapping("/{id}/categorias")
    public ResponseEntity<?> asociarCategorias(@PathVariable int id, @RequestBody List<Integer> categoriasIds) {
        try {
            Producto productoActualizado = productoService.asociarCategorias(id, categoriasIds);
            return ResponseEntity.ok(productoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PatchMapping("/{id}/cantidad")
    public ResponseEntity<?> actualizarCantidad(@PathVariable int id, @RequestParam int cantidad) {
        try {
            Producto productoActualizado = productoService.actualizarCantidad(id, cantidad);
            return ResponseEntity.ok(productoActualizado);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar cantidad: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
