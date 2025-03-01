package com.espe.app.apigateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Configuration
public class GatewayConfig {

    private static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);

    private final Environment env;

    public GatewayConfig(Environment env) {
        this.env = env;
    }

    /**
     * Configuración del Rate Limiter con Redis, usando valores desde el `application.properties` o valores por defecto.
     *
     * @return Instancia de RedisRateLimiter con parámetros configurados.
     */
    @Bean
    public RedisRateLimiter redisRateLimiter() {
        int replenishRate = env.getProperty("spring.cloud.gateway.redis-rate-limiter.replenishRate", Integer.class, 5);
        int burstCapacity = env.getProperty("spring.cloud.gateway.redis-rate-limiter.burstCapacity", Integer.class, 10);

        logger.info("Configurando Redis Rate Limiter: replenishRate={}, burstCapacity={}", replenishRate, burstCapacity);
        return new RedisRateLimiter(replenishRate, burstCapacity);
    }

    /**
     * Definición de rutas para el Gateway, aplicando límites de tasa por IP.
     *
     * @param builder RouteLocatorBuilder proporcionado por Spring Cloud Gateway.
     * @return RouteLocator con rutas configuradas dinámicamente.
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("service1", r -> r.path("/productos/**")
                        .filters(f -> f.stripPrefix(1)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(ipKeyResolver())))
                        .uri(env.getProperty("service1.url", "http://localhost:8004"))) // Se puede configurar vía env

                .route("service2", r -> r.path("/crud/**")
                        .filters(f -> f.stripPrefix(1)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(ipKeyResolver())))
                        .uri(env.getProperty("service2.url", "http://localhost:8002"))) // Valor por defecto

                .route("service4", r -> r.path("/seguridad/**")
                        .filters(f -> f.stripPrefix(1)
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(ipKeyResolver())))
                        .uri(env.getProperty("service4.url", "http://localhost:8003"))) // Valor por defecto

                .build();
    }

    /**
     * KeyResolver basado en la IP del cliente, considerando proxies (`X-Forwarded-For`).
     *
     * @return KeyResolver que devuelve la dirección IP del cliente o proxy.
     */
    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String forwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");

            // Si hay múltiples ips en X-Forwarded-For, tomamos la primera (IP original del cliente)
            String clientIp = Optional.ofNullable(forwardedFor)
                    .map(ip -> ip.split(",")[0].trim()) // Obtiene la primera IP de la lista
                    .orElseGet(() -> Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                            .map(addr -> addr.getAddress().getHostAddress())
                            .orElse("UNKNOWN_IP")); // Prevención de nulls

            logger.info("Resolviendo IP del cliente: {}", clientIp);
            return Mono.just(clientIp);
        };
    }
}
