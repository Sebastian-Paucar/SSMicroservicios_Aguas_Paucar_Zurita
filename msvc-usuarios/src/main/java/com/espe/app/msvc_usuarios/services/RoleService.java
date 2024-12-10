package com.espe.app.msvc_usuarios.services;


import com.espe.app.msvc_usuarios.models.Role;
import com.espe.app.msvc_usuarios.repositorys.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return (List<Role>) roleRepository.findAll();
    }
}