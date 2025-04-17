package com.presteo.app.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

import static org.hibernate.annotations.CascadeType.REMOVE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // Specifies the table name in the database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(unique = true, nullable = false)
    @NotNull
    private String username;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
    @NotNull
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters")
    @NotNull
    private String firstName;

    @NotBlank(message = "Address is required")
    @Size(min = 3, message = "Address must be at least 3 characters")
    @NotNull
    @Column(unique = true, nullable = false)
    private String address;

    @NotBlank(message = "Birth date is required")
    @Size(min = 10, max = 10, message = "Birth date must be in the format YYYY-MM-DD")
    @NotNull
    @Column(nullable = false)
    private String birthDate;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    @NotNull
    @Column(nullable = false)
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    @NotNull
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    @NotNull
    @Column(nullable = false)
    private String password;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @OneToOne()
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user")
    @Cascade(value = REMOVE)
    @Nullable
    private UserDescription description;

    @OneToOne(mappedBy = "user")
    @Cascade(value = REMOVE)
    @Nullable
    private ProviderInformation providerInformation;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Date updatedAt;
}
