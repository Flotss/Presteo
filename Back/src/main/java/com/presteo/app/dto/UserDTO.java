package com.presteo.app.dto;

import com.presteo.app.model.Role;
import com.presteo.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour transférer les données utilisateur sans exposer d'informations sensibles
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private String birthDate;
    private String phoneNumber;
    private Role role;

    public static UserDTO Build(User user) {
        return builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole()).build();
    }
}