package com.espe.app.productos.services;

import com.espe.app.productos.client.UsuarioFeignClient;
import com.espe.app.productos.models.Producto;
import com.espe.app.productos.repositorys.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import java.util.UUID;

@Service
public class ProductoService {
    private static final String CARPETA_IMAGENES = "/var/lib/postgresql/storage/";

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioFeignClient usuarioFeignClient;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(int id) {
        return productoRepository.findById(id);
    }

    public Producto guardar(@Valid Producto producto, MultipartFile file, String emailUsuario) throws IOException {
        // Verificar si el usuario existe en el sistema de usuarios
        if (usuarioFeignClient.obtenerUsuarioPorEmail(emailUsuario) == null) {
            throw new IllegalArgumentException("Usuario no encontrado en el sistema");
        }

        // Guardamos solo el email del usuario
        producto.setEmailUsuario(emailUsuario);

        // Validar nombre obligatorio
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        // Validar precio
        if (producto.getPrecio() == null || producto.getPrecio().doubleValue() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        // Validar estado
        if (producto.getEstado() == null || (!producto.getEstado().equalsIgnoreCase("DISPONIBLE") &&
                !producto.getEstado().equalsIgnoreCase("NO DISPONIBLE"))) {
            throw new IllegalArgumentException("El estado del producto debe ser DISPONIBLE o NO DISPONIBLE");
        }

        // Si hay archivo, guardarlo en storage y actualizar la URL
        if (file != null && !file.isEmpty()) {
            String nombreArchivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String rutaArchivo = CARPETA_IMAGENES + nombreArchivo;

            File directorio = new File(CARPETA_IMAGENES);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            Files.write(Paths.get(rutaArchivo), file.getBytes());
            producto.setImagenUrl(rutaArchivo);
        }

        return productoRepository.save(producto);
    }

    public void eliminar(int id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isEmpty()) {
            throw new IllegalArgumentException("El producto no existe");
        }

        // Eliminar imagen si existe
        if (producto.get().getImagenUrl() != null) {
            File archivo = new File(producto.get().getImagenUrl());
            if (archivo.exists()) {
                archivo.delete();
            }
        }

        productoRepository.deleteById(id);
    }
}
