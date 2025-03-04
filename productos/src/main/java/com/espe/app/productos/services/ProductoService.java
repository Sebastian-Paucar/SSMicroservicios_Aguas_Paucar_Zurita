package com.espe.app.productos.services;

import com.espe.app.productos.client.UsuarioFeignClient;
import com.espe.app.productos.models.Categoria;
import com.espe.app.productos.models.Producto;
import com.espe.app.productos.repositorys.CategoriaRepository;
import com.espe.app.productos.repositorys.ProductoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ProductoService {
    private static final String CARPETA_IMAGENES = "/var/lib/postgresql/storage/";

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioFeignClient usuarioFeignClient;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(int id) {
        return productoRepository.findById(id);
    }

    @Transactional
    public Producto guardar(@Valid Producto producto, MultipartFile file, String emailUsuario) throws IOException {
        log.info("Guardando producto para usuario: {}", emailUsuario);

        if (usuarioFeignClient.obtenerUsuarioPorEmail(emailUsuario) == null) {
            log.error("Usuario no encontrado en el sistema");
            throw new IllegalArgumentException("Usuario no encontrado en el sistema");
        }

        producto.setEmailUsuario(emailUsuario);

        // Validaciones con mensajes más detallados
        validarProducto(producto);

        // Manejo de archivos de manera segura
        if (file != null && !file.isEmpty()) {
            String nombreArchivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String rutaArchivo = CARPETA_IMAGENES + nombreArchivo;

            File directorio = new File(CARPETA_IMAGENES);
            if (!directorio.exists() && !directorio.mkdirs()) {
                log.error("No se pudo crear el directorio de imágenes.");
                throw new IOException("No se pudo crear el directorio de almacenamiento");
            }

            try {
                Files.write(Paths.get(rutaArchivo), file.getBytes());
                producto.setImagenUrl(rutaArchivo);
            } catch (IOException e) {
                log.error("Error al guardar la imagen del producto", e);
                throw new IOException("Error al guardar la imagen del producto");
            }
        }

        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarCantidad(int id, int nuevaCantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Producto con ID {} no encontrado", id);
                    return new IllegalArgumentException("El producto no existe");
                });

        if (nuevaCantidad < 0) {
            log.error("Cantidad negativa para producto con ID {}", id);
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        producto.setCantidad(nuevaCantidad);
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminar(int id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Producto con ID {} no encontrado", id);
                    return new IllegalArgumentException("El producto no existe");
                });

        if (producto.getImagenUrl() != null) {
            File archivo = new File(producto.getImagenUrl());
            if (archivo.exists() && !archivo.delete()) {
                log.warn("No se pudo eliminar la imagen del producto con ID {}", id);
            }
        }

        productoRepository.deleteById(id);
        log.info("Producto con ID {} eliminado", id);
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio().doubleValue() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (producto.getEstado() == null || (!producto.getEstado().equalsIgnoreCase("DISPONIBLE") &&
                !producto.getEstado().equalsIgnoreCase("NO DISPONIBLE"))) {
            throw new IllegalArgumentException("El estado del producto debe ser DISPONIBLE o NO DISPONIBLE");
        }
    }
    @Transactional
    public Producto asociarCategorias(int idProducto, List<Integer> categoriasIds) {
        // Buscar el producto
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Buscar las categorías
        List<Categoria> categorias = categoriaRepository.findAllById(categoriasIds);

        if (categorias.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron las categorías especificadas");
        }

        // Asociar las categorías al producto
        producto.setCategorias(new HashSet<>(categorias));

        // Guardar cambios
        return productoRepository.save(producto);
    }

}
