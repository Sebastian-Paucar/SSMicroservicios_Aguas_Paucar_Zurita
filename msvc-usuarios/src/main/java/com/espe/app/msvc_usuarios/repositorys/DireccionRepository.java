package com.espe.app.msvc_usuarios.repositorys;

import com.espe.app.msvc_usuarios.models.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
}

