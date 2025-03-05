package com.espe.app.msvc_usuarios.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Column(name = "imagen_perfil", columnDefinition = "TEXT")
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
    @JsonIgnore // <- Evita que la relación sea serializada en el JSON de salida
    private Set<Direccion> direcciones = new HashSet<>();



    public Set<Direccion> getDirecciones() {
        return direcciones;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name())) // <- Ahora usa getRole().name()
                .collect(Collectors.toList());
    }

    // Métodos de UserDetails
    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return email; }

    // Getters y Setters

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public @NotEmpty @NotBlank(message = "El campo nombre no puede estar vacío") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotEmpty @NotBlank(message = "El campo nombre no puede estar vacío") String nombre) {
        this.nombre = nombre;
    }

    public @NotEmpty @NotBlank(message = "El campo email no puede estar vacío") @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty @NotBlank(message = "El campo email no puede estar vacío") @Email String email) {
        this.email = email;
    }

    public void setPassword(@NotEmpty @NotBlank(message = "El campo contraseña no puede estar vacío") String password) {
        this.password = password;
    }

    public @NotEmpty @NotBlank(message = "El campo teléfono no puede estar vacío") @Length(min = 10, max = 10, message = "El teléfono debe tener exactamente 10 dígitos") String getTelefono() {
        return telefono;
    }

    public void setTelefono(@NotEmpty @NotBlank(message = "El campo teléfono no puede estar vacío") @Length(min = 10, max = 10, message = "El teléfono debe tener exactamente 10 dígitos") String telefono) {
        this.telefono = telefono;
    }

    public @NotEmpty @NotBlank(message = "El campo perfil no puede estar vacío") String getPerfil() {
        return perfil;
    }

    public void setPerfil(@NotEmpty @NotBlank(message = "El campo perfil no puede estar vacío") String perfil) {
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
