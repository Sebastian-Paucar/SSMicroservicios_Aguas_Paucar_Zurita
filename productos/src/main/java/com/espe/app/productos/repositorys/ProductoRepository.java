package com.espe.app.productos.repositorys;

import com.espe.app.productos.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("SELECT p FROM Producto p JOIN p.categorias c WHERE c.idCategoria = :idCategoria")
    List<Producto> findByCategoriaId(@Param("idCategoria") Long idCategoria);
}
