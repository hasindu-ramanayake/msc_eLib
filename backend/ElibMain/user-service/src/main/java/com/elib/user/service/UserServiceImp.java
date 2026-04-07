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

    @Transactional
    // Ensures atomicity: all database operations must succeed, or the transaction rolls back
    public UserDTO register(UserRegistrationDTO dto) {
        log.info("Attempting to register user with email: {}", dto.email());
        
        // 1. Check for duplicate email
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            log.warn("Registration failed: User with email {} already exists", dto.email());
            throw new UserAlreadyExistsException("User already exists with email: " + dto.email());
        }

        // 2. Map DTO to Entity
        User user = userMapper.toUserEntity(dto);
        
        // 3. Assign default role if none provided
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
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
                user.getLastName()
        );
    }

    public UserDTO updateUser(UUID id, UserUpdateDTO updateDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        
        user.setFirstName(updateDetails.firstName());
        user.setLastName(updateDetails.lastName());
        user.setEmail(updateDetails.email());
        user.setRole(updateDetails.role());
        user.setNotificationPreferences(updateDetails.notificationPreferences());
        user.setAddress(userMapper.toAddressEntity(updateDetails.address()));
        
        if (updateDetails.password() != null && !updateDetails.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateDetails.password()));
        }
        
        return userMapper.toUserDTO(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
