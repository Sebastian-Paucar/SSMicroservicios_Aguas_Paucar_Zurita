package com.espe.app.soap.services;

import com.espe.app.soap.models.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> findAll();
    Optional<Producto> findById(int id);
    Producto save(Producto producto);
    void deleteById(int id);
}