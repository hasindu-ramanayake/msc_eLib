package com.example.apigateway.config;

import com.example.apigateway.filter.AuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Main security configuration for the API Gateway.
 * Manages CORS, CSRF, session policy, and endpoint access rules.
 */
@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class GatewaySecurityConfig {

    private final AuthenticationFilter authenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .anonymous(anonymous -> anonymous.authorities("ROLE_ANONYMOUS"))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> 
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/v1/users/register").permitAll()
                        .requestMatchers("/api/v1/users/login").permitAll()
                        .requestMatchers("/api/v1/users/test").permitAll()
                        .requestMatchers("/api/v1/search/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/login/oauth2/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/eureka/**").permitAll()

                        // Admin Only endpoints
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/users").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")

                        // Staff & Admin Inventory Management
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/item/**").hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/v1/item/**").hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/v1/item/**").hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/v1/item/**").hasAnyRole("STAFF", "ADMIN")
                        
                        // All other authenticated requests
                        .anyRequest().hasAnyRole("CUSTOMER", "ADMIN", "STAFF") // Added STAFF role to allow profile updates and general access
                );

        return http.build();
    }
}