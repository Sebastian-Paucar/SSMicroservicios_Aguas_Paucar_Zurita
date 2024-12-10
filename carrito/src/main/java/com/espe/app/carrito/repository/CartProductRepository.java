package com.espe.app.carrito.repository;

import com.espe.app.carrito.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {
}
