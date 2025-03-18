package com.presteo.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.presteo.app.dto.UserDTO;
import com.presteo.app.model.User;
import com.presteo.app.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "API de gestion des utilisateurs")
public class UserController {

    private final UserService userService;
    
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @GetMapping
    @Operation(summary = "Obtenir tous les utilisateurs", description = "Récupère la liste de tous les utilisateurs enregistrés")
    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée avec succès")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = userService.getAllUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un utilisateur par ID", description = "Récupère les détails d'un utilisateur à partir de son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur trouvé", 
                content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @Content)
    })
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID de l'utilisateur à rechercher") @PathVariable Long id) {
        return userService.getUserById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un utilisateur", description = "Création d'un nouvel utilisateur dans le système")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données d'utilisateur invalides")
    })
    public ResponseEntity<UserDTO> createUser(
            @Parameter(description = "Données de l'utilisateur à créer") @Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdUser));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un utilisateur", description = "Met à jour les informations d'un utilisateur existant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
        @ApiResponse(responseCode = "400", description = "Données d'utilisateur invalides")
    })
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID de l'utilisateur à mettre à jour") @PathVariable Long id, 
            @Parameter(description = "Nouvelles données de l'utilisateur") @Valid @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur", description = "Supprime un utilisateur du système")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Utilisateur supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID de l'utilisateur à supprimer") @PathVariable Long id) {
        return userService.deleteUser(id) 
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher par nom d'utilisateur", description = "Recherche un utilisateur par son nom d'utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<UserDTO> findByUsername(
            @Parameter(description = "Nom d'utilisateur à rechercher") @RequestParam String username) {
        return userService.findByUsername(username)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/domain")
    @Operation(summary = "Rechercher par domaine d'email", description = "Recherche des utilisateurs par domaine d'email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateurs trouvés"),
        @ApiResponse(responseCode = "204", description = "Aucun utilisateur trouvé pour ce domaine")
    })
    public ResponseEntity<List<UserDTO>> findByEmailDomain(
            @Parameter(description = "Domaine d'email à rechercher") @RequestParam String domain) {
        List<UserDTO> userDTOs = userService.findUsersByEmailDomain(domain).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
        return userDTOs.isEmpty() 
                ? ResponseEntity.noContent().build() 
                : ResponseEntity.ok(userDTOs);
    }
}