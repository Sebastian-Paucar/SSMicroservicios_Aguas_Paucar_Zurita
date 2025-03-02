package com.espe.app.productos.models;

import lombok.Data;

@Data
public class Usuario {
    private String email;
    private String nombre;
    private String telefono;
    private String imagenPerfil;
}