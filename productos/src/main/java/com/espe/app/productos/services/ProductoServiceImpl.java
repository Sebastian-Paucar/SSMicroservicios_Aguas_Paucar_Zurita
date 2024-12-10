package com.espe.app.productos.services;

import com.espe.app.productos.models.Categoria;
import com.espe.app.productos.models.Producto;
import com.espe.app.productos.repositorys.CategoriaRepository;
import com.espe.app.productos.repositorys.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Producto findById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }




    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public void addCategoriaToProducto(Long idProducto, Long idCategoria) {
        Producto producto = productoRepository.findById(idProducto).orElseThrow();
        Categoria categoria = categoriaRepository.findById(idCategoria).orElseThrow();

        producto.getCategorias().add(categoria);
        productoRepository.save(producto);
    }

    @Override
    public void removeCategoriaFromProducto(Long idProducto, Long idCategoria) {
        Producto producto = productoRepository.findById(idProducto).orElseThrow();
        Categoria categoria = categoriaRepository.findById(idCategoria).orElseThrow();

        producto.getCategorias().remove(categoria);
        productoRepository.save(producto);
    }

    @Override
    public List<Producto> findByCategoriaId(Long idCategoria) {
        return productoRepository.findByCategoriaId(idCategoria);
    }

}
