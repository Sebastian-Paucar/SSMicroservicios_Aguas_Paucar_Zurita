package com.espe.app.security.client;


import com.espe.app.security.config.FeignClientConfig;
import com.espe.app.security.entity.Usuario;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@FeignClient(name = "msvc-usuarios", url = "${client.usuarios.uri}", configuration = FeignClientConfig.class)
public interface UsuarioClient {

    @GetMapping("/crud/usuarios/email/{email}")
    Usuario buscarPorEmail(@PathVariable("email") String name);
}


