package com.espe.app.msvc_usuarios.services;

import com.espe.app.msvc_usuarios.models.Direccion;

import java.util.List;
import java.util.Optional;

public interface DireccionService {
    List<Direccion> listar();
    void asociarDireccionAUsuario(Long idUsuario, Direccion direccion);
    Direccion guardar(Direccion direccion);
    Direccion actualizar(Long id, Direccion direccionActualizada);
    void eliminar(Long id);
    Optional<Direccion> porId(Long id);
    List<Direccion> listarPorUsuarioId(Long idUsuario);
}
