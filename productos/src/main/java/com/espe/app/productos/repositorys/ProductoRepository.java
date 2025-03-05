package com.espe.app.productos.repositorys;

import com.espe.app.productos.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
