package com.presteo.app.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

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
    @Column(unique = true)
    @NotNull
    private String username;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
    @NotNull
    private String lastName;

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters")
    @NotNull
    private String firstName;

    @NotBlank(message = "Address is required")
    @Size(min = 3, max = 100, message = "Address must be between 3 and 100 characters")
    @NotNull
    private String address;

    @NotBlank(message = "Gender is required")
    @Size(min = 1, max = 10, message = "Gender must be between 1 and 10 characters")
    @NotNull
    private String gender;

    @NotBlank(message = "Birth date is required")
    @Size(min = 10, max = 10, message = "Birth date must be in the format YYYY-MM-DD")
    @NotNull
    private String birthDate;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    @NotNull
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    @NotNull
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    @NotNull
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @Nullable
    private Role role;

    @OneToOne(mappedBy = "user")
    @Nullable
    private UserDescription descriptions;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
