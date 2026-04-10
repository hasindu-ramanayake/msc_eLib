package com.example.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Initializing CORS configuration for ApiGateway");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
        // Specifies the HTTP methods that are allowed for cross-origin requests
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        // Specifies the HTTP headers that are allowed for cross-origin requests
        configuration.setAllowedHeaders(
                Arrays.asList("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
        // Specifies the HTTP headers that are allowed for cross-origin requests
        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
        // Specifies whether the browser should send cookies with cross-origin requests
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        // Register the CORS configuration with a URL pattern
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
