package com.espe.app.carrito.service;

import com.espe.app.carrito.dto.CartDTO;
import com.espe.app.carrito.dto.CartProductDTO;
import com.espe.app.carrito.entity.Cart;
import com.espe.app.carrito.entity.CartProduct;
import com.espe.app.carrito.feign.ProductClient;
import com.espe.app.carrito.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductClient productClient;
    @Override
    public CartDTO getCartByUserId(Integer userId) {
        Cart cart = cartRepository.findByIdUsuario(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        return mapToDTO(cart);
    }

    @Override
    public CartDTO createCart(CartDTO cartDTO) {
        Cart cart = new Cart();
        cart.setIdUsuario(cartDTO.getIdUsuario());

        cart.setCartProducts(cartDTO.getProducts().stream()
                .map(productDTO -> {
                    // Llamar al microservicio de productos para obtener el precio
                    BigDecimal precioUnitario = productClient.getProductPriceById(productDTO.getIdProducto());

                    CartProduct product = new CartProduct();
                    product.setIdProducto(productDTO.getIdProducto());
                    product.setCantidad(productDTO.getCantidad());
                    product.setPrecioUnitario(precioUnitario);
                    product.setSubtotal(precioUnitario.multiply(new BigDecimal(productDTO.getCantidad())));
                    return product;
                })
                .collect(Collectors.toList()));

        cart = cartRepository.save(cart);
        return mapToDTO(cart);
    }

    @Override
    public void deleteCart(Integer userId) {
        Cart cart = cartRepository.findByIdUsuario(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
        cartRepository.delete(cart);
    }

    private CartDTO mapToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setIdUsuario(cart.getIdUsuario());

        dto.setProducts(cart.getCartProducts().stream()
                .map(product -> {
                    CartProductDTO productDTO = new CartProductDTO();
                    productDTO.setIdProducto(product.getIdProducto());
                    productDTO.setCantidad(product.getCantidad());
                    productDTO.setPrecioUnitario(product.getPrecioUnitario());
                    return productDTO;
                }).collect(Collectors.toList()));
        return dto;
    }

    private CartProduct mapToEntity(CartProductDTO dto) {
        CartProduct product = new CartProduct();
        product.setIdProducto(dto.getIdProducto());
        product.setCantidad(dto.getCantidad());
        product.setPrecioUnitario(dto.getPrecioUnitario());
        product.setSubtotal(dto.getPrecioUnitario().multiply(new BigDecimal(dto.getCantidad())));
        return product;
    }
}
