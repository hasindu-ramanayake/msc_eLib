package com.example.apigateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        log.info("Creating load-balanced RestTemplate bean");
        return new RestTemplate();
    }
}
