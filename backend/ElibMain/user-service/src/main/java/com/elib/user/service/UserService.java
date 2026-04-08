package com.elib.user.service;

import com.elib.user.dto.AuthRequestDTO;
import com.elib.user.dto.AuthResponseDTO;
import com.elib.user.dto.UserDTO;
import com.elib.user.dto.UserRegistrationDTO;
import com.elib.user.dto.UserUpdateDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing user accounts and authentication.
 */
public interface UserService {
    // Retrieves all registered users in the system
    List<UserDTO> getAllUsers();

    // Finds a specific user by their unique identifier
    UserDTO getUserById(UUID id);

    // Finds a specific user by their email address (useful for profile retrieval)
    UserDTO getUserByEmail(String email);

    // Processes a new user registration, including password encryption and default role assignment
    UserDTO register(UserRegistrationDTO dto);

    // Authenticates a user and generates JWT tokens upon success
    AuthResponseDTO login(AuthRequestDTO authRequest);

    // Updates existing user profile information
    UserDTO updateUser(UUID id, UserUpdateDTO updateDetails);

    // Deletes a user account from the system
    void deleteUser(UUID id);
}
