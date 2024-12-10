package com.espe.app.carrito.dto;


import java.time.LocalDate;
import java.util.List;


public class CartDTO {
    private Integer idUsuario;
    private LocalDate fechaCreacion;
    private List<CartProductDTO> products;

    public List<CartProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<CartProductDTO> products) {
        this.products = products;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}
