package com.espe.app.msvc_usuarios.services;

import com.espe.app.msvc_usuarios.models.Usuario;


import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listar();
    Optional<Usuario> porId(Long id);
    Optional<Usuario> porEmail(String email);
    Usuario guardar(Usuario usuario);
    Usuario actualizar(Long id,Usuario usuario);
    void eliminar(Long id);
    Optional<Usuario> buscarPorNombre(String nombre);
}
