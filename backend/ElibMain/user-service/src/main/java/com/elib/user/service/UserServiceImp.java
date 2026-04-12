
package com.elib.user.service;

import com.elib.user.dto.AuthRequestDTO;
import com.elib.user.dto.AuthResponseDTO;
import com.elib.user.dto.UserRegistrationDTO;
import com.elib.user.dto.UserUpdateDTO;
import com.elib.user.dto.UserDTO;
import com.elib.user.dto.UserMapper;
import com.elib.user.entity.Role;
import com.elib.user.entity.User;
import com.elib.user.repository.UserRepository;
import com.elib.user.util.JwtUtil;
import com.elib.user.exception.InvalidCredentialsException;
import com.elib.user.exception.UserAlreadyExistsException;
import com.elib.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder; // Added for self-deletion check
import com.elib.user.entity.NotificationPreference;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toUserDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Transactional
    // Ensures atomicity: all database operations must succeed, or the transaction rolls back
    @CircuitBreaker(name = "userService", fallbackMethod = "registerFallback")
    public UserDTO register(UserRegistrationDTO dto) {
        log.info("Attempting to register user with email: {}", dto.email());
        
        // 1. Check for duplicate email
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            log.warn("Registration failed: User with email {} already exists", dto.email());
            throw new UserAlreadyExistsException("User already exists with email: " + dto.email());
        }

        // 2. Map DTO to Entity
        User user = userMapper.toUserEntity(dto);
        
        // 3. Assign default role and notification preferences if none provided (e.g., from Postman or public registration)
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        }
        
        if (user.getNotificationPreferences() == null || user.getNotificationPreferences().isEmpty()) {
            user.setNotificationPreferences(new HashSet<>(Collections.singletonList(NotificationPreference.EMAIL)));
        }
        
        // 4. Securely hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        try {
            // 5. Persist the user to the database
            User savedUser = userRepository.save(user);
            log.info("User registered successfully with ID: {}", savedUser.getId());
            return userMapper.toUserDTO(savedUser);
        } catch (Exception e) {
            log.error("Unexpected error during registration for {}: {}", dto.email(), e.getMessage());
            throw e;
        }
    }

    // Fallback method for the circuit breaker
    public UserDTO registerFallback(UserRegistrationDTO dto, Throwable t) {
        log.error("Circuit breaker 'userService' triggered during registration for: {}. Error: {}", dto.email(), t.getMessage());
        // For a critical operation like registration, we might return a custom error or throw a specific exception
        // that the frontend can handle appropriately.
        if (t instanceof UserAlreadyExistsException) {
            throw (UserAlreadyExistsException) t;
        }
        throw new RuntimeException("User registration is temporarily unavailable due to system load or dependency failure. Please try again later.", t);
    }

    public AuthResponseDTO login(AuthRequestDTO authRequest) {
        log.info("Login attempt for email: {}", authRequest.email());
        
        // 1. Verify user exists
        User user = userRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + authRequest.email()));

        // 2. Validate password
        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            log.warn("Login failed: Invalid credentials for email {}", authRequest.email());
            throw new InvalidCredentialsException("Invalid credentials");
        }

        // 3. Generate JWT access and refresh tokens
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getRole().name());
        
        return new AuthResponseDTO(
                token,
                refreshToken,
                user.getEmail(),
                user.getRole().name(),
                user.getFirstName(),
                user.getLastName(),
                user.getId() // Added user ID to the response
        );
    }

    public UserDTO updateUser(UUID id, UserUpdateDTO updateDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        
        user.setFirstName(updateDetails.firstName());
        user.setLastName(updateDetails.lastName());
        user.setEmail(updateDetails.email());
        
        // Prevent overwriting role and notification preferences with null if not provided in the request
        if (updateDetails.role() != null) {
            user.setRole(updateDetails.role());
        }
        
        if (updateDetails.notificationPreferences() != null) {
            user.setNotificationPreferences(updateDetails.notificationPreferences());
        }
        
        user.setAddress(userMapper.toAddressEntity(updateDetails.address()));
        
        if (updateDetails.password() != null && !updateDetails.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateDetails.password()));
        }
        
        return userMapper.toUserDTO(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        // 1. Get the current user's email from the security context
        String currentUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // 2. Find the user to be deleted
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        
        // 3. Prevent admin from deleting themselves
        if (userToDelete.getEmail().equals(currentUserEmail)) {
            log.warn("Access Denied: Admin {} tried to delete their own account", currentUserEmail);
            throw new AccessDeniedException("You cannot delete your own account");
        }
        
        log.info("Deleting user: {} (ID: {})", userToDelete.getEmail(), id);
        userRepository.deleteById(id);
    }
}
