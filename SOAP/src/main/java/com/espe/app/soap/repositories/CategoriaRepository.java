package com.espe.app.soap.repositories;


import com.espe.app.soap.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}

