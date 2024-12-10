package com.espe.app.security.config;


import feign.Client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import feign.httpclient.ApacheHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


@Configuration
public class FeignClientConfig {

    @Bean
    public Client feignClient() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return new ApacheHttpClient(httpClient);
    }
}


