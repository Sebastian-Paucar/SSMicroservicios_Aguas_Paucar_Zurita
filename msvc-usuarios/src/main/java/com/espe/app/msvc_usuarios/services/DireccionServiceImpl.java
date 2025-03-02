package com.espe.app.msvc_usuarios.services;

import com.espe.app.msvc_usuarios.models.Direccion;
import com.espe.app.msvc_usuarios.models.Usuario;
import com.espe.app.msvc_usuarios.repositorys.DireccionRepository;
import com.espe.app.msvc_usuarios.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DireccionServiceImpl implements DireccionService {

    private final DireccionRepository direccionRepository;
    private final UsuarioRepository usuarioRepository;
    @Autowired
    public DireccionServiceImpl(DireccionRepository direccionRepository, UsuarioRepository usuarioRepository) {
        this.direccionRepository = direccionRepository;
        this.usuarioRepository = usuarioRepository;
    }
    @Override
    @Transactional
    public void asociarDireccionAUsuario(Long idUsuario, Direccion direccion) {
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con el id: " + idUsuario));

        // Guardar la dirección si es nueva
        Direccion direccionGuardada = direccionRepository.save(direccion);

        // Asociar la dirección al usuario
        usuario.getDirecciones().add(direccionGuardada);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Direccion> listar() {
        return direccionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Direccion> porId(Long id) {
        return direccionRepository.findById(Math.toIntExact(id));
    }

    @Override
    @Transactional
    public Direccion guardar(Direccion direccion) {
        // Validar campos obligatorios
        if (direccion.getCallePrincipal() == null || direccion.getCallePrincipal().isEmpty()) {
            throw new IllegalArgumentException("El campo 'callePrincipal' es obligatorio.");
        }
        if (direccion.getCiudad() == null || direccion.getCiudad().isEmpty()) {
            throw new IllegalArgumentException("El campo 'ciudad' es obligatorio.");
        }
        if (direccion.getProvincia() == null || direccion.getProvincia().isEmpty()) {
            throw new IllegalArgumentException("El campo 'provincia' es obligatorio.");
        }
        if (direccion.getCodigoPostal() == null || direccion.getCodigoPostal().isEmpty()) {
            throw new IllegalArgumentException("El campo 'codigoPostal' es obligatorio.");
        }

        // Guardar la dirección
        try {
            return direccionRepository.save(direccion);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al guardar la dirección: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Direccion actualizar(Long id, Direccion direccionActualizada) {
        // Buscar la dirección o lanzar excepción si no se encuentra
        Direccion direccion = direccionRepository.findById(Math.toIntExact(id)).orElseThrow(() ->
                new IllegalArgumentException("Dirección no encontrada con el id: " + id));

        if (direccionActualizada.getCallePrincipal() != null) {
            direccion.setCallePrincipal(direccionActualizada.getCallePrincipal());
        }
        if (direccionActualizada.getCalleSecundaria() != null) {
            direccion.setCalleSecundaria(direccionActualizada.getCalleSecundaria());
        }
        if (direccionActualizada.getCiudad() != null) {
            direccion.setCiudad(direccionActualizada.getCiudad());
        }
        if (direccionActualizada.getProvincia() != null) {
            direccion.setProvincia(direccionActualizada.getProvincia());
        }
        if (direccionActualizada.getCodigoPostal() != null) {
            direccion.setCodigoPostal(direccionActualizada.getCodigoPostal());
        }
        if (direccionActualizada.getDetalle() != null) {
            direccion.setDetalle(direccionActualizada.getDetalle());
        }

        // Guardar la dirección actualizada
        return direccionRepository.save(direccion);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        direccionRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Direccion> listarPorUsuarioId(Long idUsuario) {
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con el id: " + idUsuario));

        // Retornar la lista de direcciones del usuario
        return new ArrayList<>(usuario.getDirecciones());
    }


}
