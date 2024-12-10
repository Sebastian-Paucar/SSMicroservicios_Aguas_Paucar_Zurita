package com.espe.app.security.entity;

import java.util.Set;

public class Usuario {
    private String nombre;
    private String email;
    private String password;

    public String getNombre() {
        return nombre;
    }


    public String getEmail() {
        return email;
    }



    public String getPassword() {
        return password != null ? new String(password) : null;
    }

    public Set<Rol> getRoles() {
        return roles;
    }


    private Set<Rol> roles;
}
