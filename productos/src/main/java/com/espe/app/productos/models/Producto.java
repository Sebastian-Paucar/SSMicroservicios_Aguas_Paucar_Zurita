package com.espe.app.productos.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProducto;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private String imagen;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @Column(nullable = false)
    private LocalDate fechaActualizacion;

    @ManyToMany
    @JoinTable(
            name = "asocia", // Nombre de la tabla de unión
            joinColumns = @JoinColumn(name = "id_producto"), // Columna FK de Producto
            inverseJoinColumns = @JoinColumn(name = "id_categoria") // Columna FK de Categoría
    )

    private List<Categoria> categorias; // Relación con Categorías
}
