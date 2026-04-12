package com.elib.user.security;

import com.elib.user.entity.User;
import com.elib.user.repository.UserRepository;
import com.elib.user.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found after OAuth2 login"));

        // Check if user was just created (within the last 30 seconds) to determine if it's a registration or a login
        boolean isNewUser = ChronoUnit.SECONDS.between(user.getCreatedAt(), LocalDateTime.now()) < 30;

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getRole().name());

        String targetUrl;
        if (isNewUser) {
            log.info("Google registration successful for: {}. Redirecting to home (logged out).", email);
            targetUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/")
                    .queryParam("registered", "true")
                    .build().toUriString();
        } else {
            log.info("Google login successful for: {}. Redirecting with tokens.", email);
            targetUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/oauth2/redirect")
                    .queryParam("token", token)
                    .queryParam("refreshToken", refreshToken)
                    .queryParam("email", user.getEmail()) // Added to fix frontend missing data
                    .queryParam("firstName", user.getFirstName()) // Added to fix frontend missing data
                    .queryParam("lastName", user.getLastName()) // Added to fix frontend missing data
                    .queryParam("role", user.getRole().name()) // Added to fix frontend missing data
                    .queryParam("id", user.getId().toString()) // Added to fix frontend missing data
                    .build().toUriString();
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
