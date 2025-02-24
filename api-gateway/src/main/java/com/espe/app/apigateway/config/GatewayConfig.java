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
                        .uri("http://localhost:8004"))
                .route("service2", r -> r.path("/crud/**")
                        .filters(f -> f.stripPrefix(1)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(ipKeyResolver())))
                        .uri("${service2.url}"))

                .route("service4", r -> r.path("/seguridad/**")
                        .filters(f -> f.stripPrefix(1)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(ipKeyResolver())))
                        .uri("${service4.url}"))
                .build();
    }

    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        // Resolver basado en la IP del cliente (manejo de proxies incluido)
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getHeaders()
                        .getFirst("X-Forwarded-For") != null
                        ? exchange.getRequest().getHeaders().getFirst("X-Forwarded-For")
                        : exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
        );
    }
}
