package com.espe.app.carrito.controller;

import com.espe.app.carrito.dto.CartDTO;
import com.espe.app.carrito.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/{userId}")
    public CartDTO getCartByUserId(@PathVariable Integer userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping
    public CartDTO createCart(@RequestBody CartDTO cartDTO) {
        return cartService.createCart(cartDTO);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteCart(@PathVariable Integer userId) {
        cartService.deleteCart(userId);
    }
}