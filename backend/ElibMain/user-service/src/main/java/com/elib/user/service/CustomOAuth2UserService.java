package com.elib.user.service;

import com.elib.user.entity.Role;
import com.elib.user.entity.User;
import com.elib.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(oAuth2User);
        } catch (Exception ex) {
            log.error("Error processing OAuth2 user: ", ex);
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        log.info("Processing OAuth2 user for email: {}", email);

        if (email == null) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        // Check the intent (login vs register) from the cookie set by the frontend
        boolean isRegisterIntent = false;
        jakarta.servlet.http.HttpServletRequest request = 
            ((org.springframework.web.context.request.ServletRequestAttributes) 
                org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();
        
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (jakarta.servlet.http.Cookie cookie : cookies) {
                if ("oauth_intent".equals(cookie.getName())) {
                    isRegisterIntent = "register".equals(cookie.getValue());
                    break;
                }
            }
        }

        log.info("OAuth2 registration intent: {}", isRegisterIntent);

        // Check if user already exists in our database
        boolean userExists = userRepository.findByEmail(email).isPresent();

        if (!userExists && !isRegisterIntent) {
            log.warn("User {} attempted to login but is not registered.", email);
            throw new org.springframework.security.oauth2.core.OAuth2AuthenticationException(
                new org.springframework.security.oauth2.core.OAuth2Error("user_not_found"), "Account not found. Please register first.");
        }

        if (!userExists && isRegisterIntent) {
            log.info("First time OAuth2 registration. Creating new user for email: {}", email);
            User newUser = User.builder()
                    .email(email)
                    .firstName(oAuth2User.getAttribute("given_name"))
                    .lastName(oAuth2User.getAttribute("family_name"))
                    .role(com.elib.user.entity.Role.CUSTOMER)
                    .password(java.util.UUID.randomUUID().toString())
                    .build();
            userRepository.save(newUser);
        }

        return oAuth2User;
    }
}
