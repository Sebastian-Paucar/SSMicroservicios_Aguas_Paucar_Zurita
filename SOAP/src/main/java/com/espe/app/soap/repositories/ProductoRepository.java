package com.espe.app.soap.repositories;


import com.espe.app.soap.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
