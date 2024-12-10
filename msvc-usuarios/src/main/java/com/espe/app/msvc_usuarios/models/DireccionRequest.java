package com.espe.app.msvc_usuarios.models;

import java.util.List;

public class DireccionRequest {
    private Long idUsuario;
    private List<Direccion> direcciones;

    // Getters y setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }
}
