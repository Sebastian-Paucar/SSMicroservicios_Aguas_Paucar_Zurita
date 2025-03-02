package com.espe.app.productos.controller;

import com.espe.app.productos.models.Producto;
import com.espe.app.productos.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;



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
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable int id) {
        Optional<Producto> producto = productoService.obtenerPorId(id);
        return producto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestPart("producto") Producto producto,
                                                  @RequestPart(value = "file", required = false) MultipartFile file,
                                                  @RequestParam String emailUsuario) {
        try {
            Producto nuevoProducto = productoService.guardar(producto, file, emailUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable int id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    // Nuevo endpoint para servir imágenes
    @GetMapping("/imagenes")
    public ResponseEntity<byte[]> obtenerImagen(@RequestParam String path) {
        try {
            // Extraer solo el nombre del archivo desde la ruta completa
            String nombreImagen = Paths.get(path).getFileName().toString();
            Path rutaImagen = Paths.get(CARPETA_IMAGENES).resolve(nombreImagen).normalize();

            byte[] imagenBytes = Files.readAllBytes(rutaImagen);
            return ResponseEntity.ok().body(imagenBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> manejarValidaciones(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en los datos ingresados: " + ex.getMessage());
    }
}