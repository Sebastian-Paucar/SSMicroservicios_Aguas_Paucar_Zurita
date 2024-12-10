package com.espe.app.carrito.service;

import com.espe.app.carrito.dto.CartDTO;

public interface CartService {
    CartDTO getCartByUserId(Integer userId);
    CartDTO createCart(CartDTO cartDTO);
    void deleteCart(Integer userId);
}
