package com.presteo.app.controller;

import com.presteo.app.dto.UserDTO;
import com.presteo.app.model.RoleType;
import com.presteo.app.repository.UserRepository;
import com.presteo.app.security.annotation.CheckCredential;
import com.presteo.app.security.annotation.SecuredRoute;
import com.presteo.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsible for managing platform users.
 * Allows viewing, modifying, and deleting user profiles,
 * as well as managing profile pictures.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecuredRoute
@Tag(name = "Users", description = "API for managing platform users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * Retrieves all users registered in the system.
     * Accessible only to users with the ADMIN role.
     *
     * @return List of all users as DTOs
     */
    @Operation(summary = "Get all users", description = "Retrieves the complete list of registered users (admin only)")
    @ApiResponse(responseCode = "200", description = "List of users successfully retrieved")
    @SecuredRoute(roles = {RoleType.ADMIN})
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = userRepository.findAll()
                .stream()
                .map(UserDTO::build)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    /**
     * Retrieves information for the currently authenticated user.
     *
     * @param request HTTP request containing authentication information
     * @return Information for the connected user as a DTO
     */
    @Operation(summary = "Get current user profile", description = "Retrieves information for the currently authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User profile successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUserFromToken(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();

        return userRepository.findByUsername(username)
                .map(UserDTO::build)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves information for a specific user by their identifier.
     *
     * @param id User identifier
     * @return User information as a DTO
     */
    @Operation(summary = "Get user by ID", description = "Retrieves information for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found and successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID of the user to retrieve") 
            @PathVariable Long id) {
        return userRepository.findById(id)
                .map(UserDTO::build)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates information for an existing user.
     * Verifies that the authenticated user has the right to modify the requested profile.
     *
     * @param id Identifier of the user to update
     * @param userDetails New user information
     * @return The updated user as a DTO
     */
    @Operation(summary = "Update a user", description = "Modifies information for an existing user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "403", description = "Not authorized to modify this user"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @CheckCredential
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID of the user to update") 
            @PathVariable Long id,
            @Parameter(description = "New user data") 
            @Valid @RequestBody UserDTO userDetails) {
        return userService.updateUser(id, userDetails)
                .map(UserDTO::build)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a user from the system.
     * Verifies that the authenticated user has the right to delete the requested profile.
     *
     * @param id Identifier of the user to delete
     * @return Empty response with code 204 in case of success
     */
    @Operation(summary = "Delete a user", description = "Permanently removes a user from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Not authorized to delete this user"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @CheckCredential
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete") 
            @PathVariable Long id) {
        return userService.deleteUser(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Updates a user's profile picture.
     * Verifies that the authenticated user has the right to modify the requested profile.
     *
     * @param id User identifier
     * @param file New profile picture
     * @return The updated user as a DTO
     */
    @Operation(summary = "Update profile picture", description = "Changes a user's profile picture")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile picture updated successfully"),
        @ApiResponse(responseCode = "403", description = "Not authorized to modify this user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid image file")
    })
    @CheckCredential
    @PostMapping("/update-profile-picture")
    public ResponseEntity<UserDTO> updateProfilePicture(
            @Parameter(description = "ID of the user to update") 
            @RequestParam Long id,
            @Parameter(description = "New profile picture") 
            @RequestParam("file") MultipartFile file) {
        var userUpdated = userService.updateProfilePicture(id, file);
        return ResponseEntity.ok(UserDTO.build(userUpdated));
    }

    /**
     * Search users by first name, last name, or id (partial or exact match)
     * @param query The search string
     * @return List of matching users as DTOs
     */
    @Operation(summary = "Search users", description = "Search users by first name, last name, or id (partial or exact match)")
    @ApiResponse(responseCode = "200", description = "List of users matching the search query")
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String query) {
        var users = userService.searchUsers(query).stream().map(UserDTO::build).toList();
        return ResponseEntity.ok(users);
    }
}
