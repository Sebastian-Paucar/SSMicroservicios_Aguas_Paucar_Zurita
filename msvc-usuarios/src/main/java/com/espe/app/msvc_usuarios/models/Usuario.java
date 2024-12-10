package com.espe.app.msvc_usuarios.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int idUsuario;

    @NotEmpty
    @NotBlank(message = "El campo nombre no puede estar vacío")
    private String nombre;

    @NotEmpty
    @NotBlank(message = "El campo email no puede estar vacío")
    @Email
    @Column(unique = true)
    private String email;

    @NotEmpty
    @NotBlank(message = "El campo contraseña no puede estar vacío")
    private String password;

    @NotEmpty
    @NotBlank(message = "El campo teléfono no puede estar vacío")
    @Length(min = 10, max = 10, message = "El teléfono debe tener exactamente 10 dígitos")
    private String telefono;

    @NotEmpty
    @NotBlank(message = "El campo perfil no puede estar vacío")
    private String perfil;

    @Column(name = "imagen_perfil")
    private String imagenPerfil;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rol_usuario",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "asocia",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_direccion")
    )
    @JsonManagedReference
    private Set<Direccion> direcciones = new HashSet<>();
    public Set<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(Set<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    // Métodos de la interfaz UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Utilizamos el email como nombre de usuario.
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Por defecto, la cuenta no está expirada.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Por defecto, la cuenta no está bloqueada.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Por defecto, las credenciales no están expiradas.
    }

    @Override
    public boolean isEnabled() {
        return true; // Por defecto, la cuenta está habilitada.
    }

    // Getters y setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
