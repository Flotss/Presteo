package com.presteo.app.service;

import com.google.cloud.storage.StorageException;
import com.presteo.app.dto.UserDTO;
import com.presteo.app.model.*;
import com.presteo.app.repository.RoleRepository;
import com.presteo.app.repository.UserDescriptionRepository;
import com.presteo.app.repository.UserProviderInformationRepository;
import com.presteo.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UserDescriptionRepository userDescriptionRepository;
    private UserProviderInformationRepository userProviderInformationRepository;
    private ImageStorageService imageStorageService;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, UserDescriptionRepository userDescriptionRepository, UserProviderInformationRepository userProviderInformationRepository, ImageStorageService imageStorageService) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userDescriptionRepository = userDescriptionRepository;
        this.userProviderInformationRepository = userProviderInformationRepository;
        this.imageStorageService = imageStorageService;
    }


    public User createUser(User user, String roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(RoleType.fromName(roleName));
        user.setRole(role);
        return userRepository.save(user);
    }

    public void setPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
    }

    public Optional<User> updateUser(Long id, UserDTO userDetails) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(userDetails.getUsername());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setFirstName(userDetails.getFirstName());
            existingUser.setLastName(userDetails.getLastName());
            existingUser.setAddress(userDetails.getAddress());
            existingUser.setBirthDate(userDetails.getBirthDate());
            existingUser.setPhoneNumber(userDetails.getPhoneNumber());

            if (userDetails.getDescription() != null) {
                userDescriptionRepository.findByUser(existingUser).ifPresentOrElse(userDescription -> {
                    userDescription.setDescription(userDetails.getDescription());
                    userDescriptionRepository.save(userDescription);
                }, () -> {
                    UserDescription newDescription = new UserDescription();
                    newDescription.setUser(existingUser);
                    newDescription.setDescription(userDetails.getDescription());
                    userDescriptionRepository.save(newDescription);
                });
            }

            if (userDetails.getExperience() != null) {
                userProviderInformationRepository.findByUser(existingUser).ifPresentOrElse(providerInformation -> {
                    providerInformation.setExperience(userDetails.getExperience());
                    userProviderInformationRepository.save(providerInformation);
                }, () -> {
                    ProviderInformation newDescription = new ProviderInformation();
                    newDescription.setUser(existingUser);
                    newDescription.setExperience(userDetails.getExperience());
                    userProviderInformationRepository.save(newDescription);
                });
            }

            return userRepository.save(existingUser);
        });
    }

    public boolean deleteUser(Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }

    public User updateProfilePicture(Long id, MultipartFile file) {
        return userRepository.findById(id).map(user -> {
            String url;
            try {
                url = imageStorageService.uploadUserProfileImage(file);
            } catch (IOException | StorageException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
            user.setProfileImageUrl(url);
            userRepository.save(user);
            return user;
        }).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    /**
     * Search users by first name, last name, or id (partial or exact match)
     */
    public List<User> searchUsers(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        String lowerQuery = query.toLowerCase();
        return userRepository.findAll().stream()
                .filter(user ->
                        (user.getFirstName() != null && user.getFirstName().toLowerCase().contains(lowerQuery)) ||
                        (user.getLastName() != null && user.getLastName().toLowerCase().contains(lowerQuery)) ||
                        (user.getId() != null && user.getId().toString().contains(lowerQuery))
                )
                .toList();
    }
}