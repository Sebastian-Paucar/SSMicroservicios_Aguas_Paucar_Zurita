package com.espe.app.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Value("${spring.cloud.gateway.redis-rate-limiter.replenishRate:5}")
    private int replenishRate;

    @Value("${spring.cloud.gateway.redis-rate-limiter.burstCapacity:10}")
    private int burstCapacity;

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        // Configuración de Redis Rate Limiter
        return new RedisRateLimiter(replenishRate, burstCapacity);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("service1", r -> r.path("/productos/**")
                        .filters(f -> f.stripPrefix(1)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(ipKeyResolver())))
                        .uri("http://186.101.180.67:8004"))
                .route("service2", r -> r.path("/crud/**")
                        .filters(f -> f.stripPrefix(1)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(ipKeyResolver())))
                        .uri("http://186.101.180.67:8002"))
                .route("service3", r -> r.path("/carrito/**")
                        .filters(f -> f.stripPrefix(1)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(ipKeyResolver())))
                        .uri("http://186.101.180.67:8003"))
                .build();
    }

    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getRemoteAddress())
                .map(addr -> addr.getAddress().getHostAddress());
    }

}
