package com.espe.app.msvc_usuarios.controller;

import com.espe.app.msvc_usuarios.models.Direccion;
import com.espe.app.msvc_usuarios.models.Usuario;
import com.espe.app.msvc_usuarios.services.DireccionService;
import com.espe.app.msvc_usuarios.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/crear-usuario")
public class publicController {
    @Autowired
    private UsuarioService service;
     @Autowired
     private DireccionService direccionService;


    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }
    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errores.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }
}
