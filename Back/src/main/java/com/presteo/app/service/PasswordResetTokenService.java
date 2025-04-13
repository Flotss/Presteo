package com.presteo.app.service;

import com.presteo.app.model.PasswordResetTokens;
import com.presteo.app.model.User;
import com.presteo.app.repository.PasswordResetTokensRepository;
import com.presteo.app.security.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokensRepository passwordResetTokensRepository;
    private final JwtUtils jwtUtils;

    public PasswordResetTokenService(PasswordResetTokensRepository passwordResetTokensRepository, JwtUtils jwtUtils) {
        this.passwordResetTokensRepository = passwordResetTokensRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional()
    public void deleteUnexpiredByUserId(Long userId) {
        passwordResetTokensRepository.deleteUnexpiredByUserId(userId);
    }

    @Transactional()
    public PasswordResetTokens createToken(User user) {
        PasswordResetTokens passwordResetToken = new PasswordResetTokens();
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(jwtUtils.generateToken(user.getUsername(), passwordResetToken.getCreatedAt(), passwordResetToken.getExpiresAt()));
        return passwordResetTokensRepository.save(passwordResetToken);
    }
}
