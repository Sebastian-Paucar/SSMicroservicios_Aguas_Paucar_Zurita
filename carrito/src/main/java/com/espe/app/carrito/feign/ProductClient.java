package com.espe.app.carrito.feign;


import com.espe.app.carrito.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "productos", url = "http://localhost:2000", configuration = FeignClientConfig.class)
public interface ProductClient {
    @GetMapping("/api/products/{id}/price")
    BigDecimal getProductPriceById(@PathVariable Integer id);
}
