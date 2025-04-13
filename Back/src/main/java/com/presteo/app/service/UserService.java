package com.presteo.app.service;

import com.presteo.app.dto.UserDTO;
import com.presteo.app.model.Role;
import com.presteo.app.model.RoleType;
import com.presteo.app.model.User;
import com.presteo.app.model.UserDescription;
import com.presteo.app.repository.RoleRepository;
import com.presteo.app.repository.UserDescriptionRepository;
import com.presteo.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UserDescriptionRepository userDescriptionRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, UserDescriptionRepository userDescriptionRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userDescriptionRepository = userDescriptionRepository;
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

            return userRepository.save(existingUser);
        });
    }

    public boolean deleteUser(Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }
}