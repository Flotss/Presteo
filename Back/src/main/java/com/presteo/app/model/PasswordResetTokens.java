package com.presteo.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetTokens {

    private static final int EXPIRATION_TIME = 15 * 60 * 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String token;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    public Date createdAt = new Date();

    @Column(name = "expires_at", nullable = false)
    public Date expiresAt = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

    @Column(nullable = false)
    public boolean used = false;


    public boolean isExpired() {
        return expiresAt.before(new Date());
    }
}
