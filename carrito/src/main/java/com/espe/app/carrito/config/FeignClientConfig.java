package com.espe.app.carrito.config;

import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public Client feignClient() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return new ApacheHttpClient(httpClient);
    }
}