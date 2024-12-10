package com.espe.app.msvc_usuarios.controller;

import com.espe.app.msvc_usuarios.models.Direccion;
import com.espe.app.msvc_usuarios.services.DireccionService;
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
@RequestMapping("/direcciones")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public List<Direccion> listar() {
        return direccionService.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Direccion> direccionOptional = direccionService.porId(id);
        if (direccionOptional.isPresent()) {
            return ResponseEntity.ok().body(direccionOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{idUsuario}/crear")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> agregarDireccionAUsuario(@PathVariable Long idUsuario, @Valid @RequestBody Direccion direccion, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }

        try {
            // Asociar dirección con el usuario
            direccionService.asociarDireccionAUsuario(idUsuario, direccion);

            return ResponseEntity.status(HttpStatus.CREATED).body("Dirección agregada exitosamente al usuario.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Direccion direccion, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(direccionService.guardar(direccion));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> editar(@Valid @RequestBody Direccion direccion, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validar(result);
        }
        try {
            Direccion direccionActualizada = direccionService.actualizar(id, direccion);
            return ResponseEntity.status(HttpStatus.CREATED).body(direccionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Direccion> optionalDireccion = direccionService.porId(id);
        if (optionalDireccion.isPresent()) {
            direccionService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errores.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> listarPorUsuarioId(@PathVariable Long idUsuario) {
        try {
            List<Direccion> direcciones = direccionService.listarPorUsuarioId(idUsuario);
            return ResponseEntity.ok(direcciones);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

}
