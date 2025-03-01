package com.espe.app.msvc_usuarios.repositorys;

import com.espe.app.msvc_usuarios.enums.RoleName;
import com.espe.app.msvc_usuarios.models.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByRole(RoleName nombre_rol); // Ajustado al nombre del atributo
}