package com.elib.user.controller;

import com.elib.user.dto.*;
import com.elib.user.service.UserService;
import com.elib.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/test")
    public String test() {
        return "user service is working";
    }

    @PostMapping("/jwt/parse")
    public ResponseEntity<JwtParseResponseDto> parseJwt(@RequestBody JwtParseRequestDto request) {
        String token = request.getToken();
        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        log.debug("Parsing JWT for email: {}, role: {}", email, role);

        return ResponseEntity.ok(JwtParseResponseDto.builder()
                .email(email)
                .role(role)
                .build());
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        return ResponseEntity.ok(userService.register(userRegistrationDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
        return ResponseEntity.ok(userService.login(authRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String token) {
        String bareToken = token.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractEmail(bareToken);
        log.info("Fetching profile for user: {}", email);
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping("/edit-profile")
    public ResponseEntity<UserDTO> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UserUpdateDTO userUpdateDTO) {
        String bareToken = token.substring(7);
        String email = jwtUtil.extractEmail(bareToken);
        
        // Find user by email first to get the ID
        UserDTO currentUser = userService.getUserByEmail(email);
        log.info("Updating profile for user: {}", email);
        
        return ResponseEntity.ok(userService.updateUser(currentUser.id(), userUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
