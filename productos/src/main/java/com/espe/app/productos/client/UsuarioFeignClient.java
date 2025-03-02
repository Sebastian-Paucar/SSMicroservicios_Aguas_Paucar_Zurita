package com.espe.app.productos.client;

import com.espe.app.productos.models.Usuario;
import com.espe.app.security.config.FeignClientConfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-usuarios", url = "http://localhost:2000", configuration = FeignClientConfig.class)
public interface UsuarioFeignClient {
    @GetMapping("/crud/usuarios/email/{email}")
    Usuario obtenerUsuarioPorEmail(@PathVariable("email") String name);
}