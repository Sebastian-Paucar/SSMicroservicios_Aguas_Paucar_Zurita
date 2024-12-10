package com.espe.app.msvc_usuarios.services;

import com.espe.app.msvc_usuarios.models.Role;
import com.espe.app.msvc_usuarios.models.Usuario;
import com.espe.app.msvc_usuarios.repositorys.RoleRepository;
import com.espe.app.msvc_usuarios.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> porId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> porEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        // Validate required fields
        validateUsuarioFields(usuario);

        // Check for duplicate email
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está en uso: " + usuario.getEmail());
        }

        // Encode the password if provided
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        // Process and assign roles
        try {
            Set<Role> rolesGuardados = procesarRoles(usuario.getRoles());
            usuario.setRoles(rolesGuardados);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al procesar los roles: " + e.getMessage(), e);
        }

        // Save the user
        try {
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al guardar el usuario: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void eliminar(Long idUsuario) {
        // Fetch the user to ensure it exists
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con el id: " + idUsuario));

        // Clear roles explicitly if necessary (optional if cascade settings handle it)
        usuario.getRoles().clear();

        // Delete the user
        usuarioRepository.deleteById(idUsuario);
    }

    @Override
    @Transactional
    public Optional<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    @Override
    @Transactional
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con el id: " + id));

        // Check for duplicate email if email is being updated
        if (usuarioActualizado.getEmail() != null && !usuarioActualizado.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.findByEmail(usuarioActualizado.getEmail()).isPresent()) {
                throw new IllegalArgumentException("El email ya está en uso: " + usuarioActualizado.getEmail());
            }
            usuario.setEmail(usuarioActualizado.getEmail());
        }

        // Update other fields
        if (usuarioActualizado.getNombre() != null) {
            usuario.setNombre(usuarioActualizado.getNombre());
        }
        if (usuarioActualizado.getTelefono() != null) {
            usuario.setTelefono(usuarioActualizado.getTelefono());
        }
        if (usuarioActualizado.getImagenPerfil() != null) {
            usuario.setImagenPerfil(usuarioActualizado.getImagenPerfil());
        }
        if (usuarioActualizado.getPassword() != null) {
            usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }
        if (usuarioActualizado.getRoles() != null && !usuarioActualizado.getRoles().isEmpty()) {
            usuario.setRoles(procesarRoles(usuarioActualizado.getRoles()));
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Helper method to process roles: saves new roles and reuses existing ones.
     */
    private Set<Role> procesarRoles(Set<Role> roles) {
        Set<Role> rolesGuardados = new HashSet<>();
        for (Role role : roles) {
            Optional<Role> roleExistente = roleRepository.findByRole(role.getRole());
            rolesGuardados.add(roleExistente.orElseGet(() -> roleRepository.save(role)));
        }
        return rolesGuardados;
    }

    /**
     * Validate required fields for a user.
     */
    private void validateUsuarioFields(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El campo 'nombre' es obligatorio.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El campo 'email' es obligatorio.");
        }
        if (usuario.getTelefono() == null || usuario.getTelefono().isEmpty()) {
            throw new IllegalArgumentException("El campo 'telefono' es obligatorio.");
        }
    }
}

