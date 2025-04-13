package com.presteo.app.controller;

import com.presteo.app.dto.UserDTO;
import com.presteo.app.model.RoleType;
import com.presteo.app.repository.UserRepository;
import com.presteo.app.security.annotation.CheckCredential;
import com.presteo.app.security.annotation.SecuredRoute;
import com.presteo.app.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @SecuredRoute(roles = {RoleType.ADMIN})
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = userRepository.findAll()
                .stream()
                .map(UserDTO::build)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }


    @SecuredRoute
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUserFromToken(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();

        return userRepository.findByUsername(username)
                .map(UserDTO::build)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @SecuredRoute
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID of the user to retrieve") @PathVariable Long id) {
        return userRepository.findById(id)
                .map(UserDTO::build)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CheckCredential
    @SecuredRoute
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID of the user to update") @PathVariable Long id,
            @Parameter(description = "New user data") @Valid @RequestBody UserDTO userDetails) {
        return userService.updateUser(id, userDetails)
                .map(UserDTO::build)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CheckCredential
    @SecuredRoute
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete") @PathVariable Long id) {
//        return ResponseEntity.ok().build();
        return userService.deleteUser(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
