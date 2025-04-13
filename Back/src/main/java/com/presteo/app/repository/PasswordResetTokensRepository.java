package com.presteo.app.repository;

import com.presteo.app.model.PasswordResetTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetTokensRepository extends JpaRepository<PasswordResetTokens, Long> {
    Optional<PasswordResetTokens> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetTokens t WHERE t.user.id = :userId AND t.expiresAt > CURRENT_TIMESTAMP")
    void deleteUnexpiredByUserId(@Param("userId") Long userId);
}
