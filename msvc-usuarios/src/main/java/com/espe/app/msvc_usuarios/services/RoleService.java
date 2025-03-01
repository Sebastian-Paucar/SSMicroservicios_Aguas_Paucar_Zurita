package com.espe.app.msvc_usuarios.services;


import com.espe.app.msvc_usuarios.models.Role;
import com.espe.app.msvc_usuarios.repositorys.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return (List<Role>) roleRepository.findAll();
    }
}