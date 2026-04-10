package com.example.apigateway.filter;

import com.example.apigateway.dto.JwtParseRequestDto;
import com.example.apigateway.dto.JwtParseResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Collections;
import java.io.IOException;

/**
 * Filter that intercept requests to validate JWT tokens.
 * It delegatestoken parsing and validation to the user-service.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String JWT_PARSE_URL = "http://user-service/api/v1/users/jwt/parse";

    private final RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(AUTH_HEADER);

        // Skip if no token or wrong format
        if (header == null || !header.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER.length());

        try {
            log.debug("Authenticating request to: {}", request.getRequestURI());
            // 1. Call user-service to validate and parse the token
            JwtParseResponseDto parsed = parseJwt(token);

            // 2. Create Spring Security authentication object with the received user info
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    parsed.getEmail(),
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + parsed.getRole())));

            // 3. Set authentication in the context
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("Successfully authenticated user: {} with role: {}", parsed.getEmail(), parsed.getRole());

        } catch (Exception ex) {
            log.error("Authentication failed: {}", ex.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private JwtParseResponseDto parseJwt(String token) {
        JwtParseResponseDto dto = restTemplate.postForObject(
                JWT_PARSE_URL,
                new JwtParseRequestDto(token),
                JwtParseResponseDto.class);

        if (dto == null) {
            throw new IllegalStateException("User service returned null JWT parse response");
        }

        return dto;
    }
}