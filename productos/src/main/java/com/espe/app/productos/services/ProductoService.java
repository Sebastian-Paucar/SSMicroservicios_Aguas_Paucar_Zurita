package com.espe.app.productos.services;



import com.espe.app.productos.models.Producto;

import java.util.List;

public interface ProductoService {
    List<Producto> findAll();
    Producto findById(Long id);
    Producto save(Producto producto);
    void deleteById(Long id);
    void addCategoriaToProducto(Long idProducto, Long idCategoria);
    void removeCategoriaFromProducto(Long idProducto, Long idCategoria);
    List<Producto> findByCategoriaId(Long idCategoria);

}

