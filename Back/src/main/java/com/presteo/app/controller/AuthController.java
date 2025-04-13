package com.presteo.app.controller;

import com.presteo.app.CookieUtils;
import com.presteo.app.controller.model.UpdatePasswordToken;
import com.presteo.app.controller.model.UpdatePasswordUser;
import com.presteo.app.model.PasswordResetTokens;
import com.presteo.app.model.RoleType;
import com.presteo.app.model.User;
import com.presteo.app.repository.PasswordResetTokensRepository;
import com.presteo.app.repository.UserRepository;
import com.presteo.app.security.JwtUtils;
import com.presteo.app.security.annotation.CheckCredential;
import com.presteo.app.security.annotation.SecuredRoute;
import com.presteo.app.security.model.AuthenticationRequest;
import com.presteo.app.security.model.CustomUserDetails;
import com.presteo.app.security.utils.SecurityUtils;
import com.presteo.app.service.EmailService;
import com.presteo.app.service.PasswordResetTokenService;
import com.presteo.app.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CookieUtils cookieUtils;
    private final PasswordResetTokensRepository passwordResetTokensRepository;
    private final EmailService emailService;
    private final PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/signin")
    public String authenticateUser(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        String login = request.getLogin().toLowerCase();

        String username = login.contains("@") ?
                userRepository.findByEmail(login).map(User::getUsername).orElseThrow(
                        () -> new RuntimeException("Identity or password is incorrect")
                ) :
                login;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails.getUsername(), userDetails.getRole());

        // Set the JWT token in the response header
        Cookie jwtCookie = jwtUtils.createCookie(jwt);
        cookieUtils.setCookie(response, jwtCookie);

        return "Authentication successful !";
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody User user) {
        user.setUsername(user.getUsername().toLowerCase());
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new EntityExistsException("Username is already taken!");
        }

        userService.createUser(user, RoleType.CUSTOMER.name());

        return "User registered successfully!";
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new EntityNotFoundException("User not found")
            );

            passwordResetTokenService.deleteUnexpiredByUserId(user.getId());
            PasswordResetTokens token = passwordResetTokenService.createToken(user);

            emailService.sendEmail(email, "Password Reset Request", token);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }

        return ResponseEntity.ok("If the email is registered, a password reset link has been sent.");
    }

    @CheckCredential
    @SecuredRoute
    @PostMapping("/change-password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UpdatePasswordUser body) {
        CustomUserDetails userDetails = SecurityUtils.getAuthenticatedUser();

        // try old password is correct
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            body.getOldPassword()
                    )
            );
        } catch (Exception e) {
            throw new AccessDeniedException("Old password is incorrect");
        }

        User user = userRepository.findById(body.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        userService.setPassword(user, body.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }


    @PostMapping("/change-password-token")
    public ResponseEntity<String> updatePasswordByToken(@RequestBody @Valid UpdatePasswordToken body) {
        PasswordResetTokens passwordResetToken = passwordResetTokensRepository.findByToken(body.token)
                .map(token -> {
                    if (token.isExpired()) {
                        passwordResetTokensRepository.delete(token);
                        throw new AccessDeniedException("Token expired");
                    }
                    return token;
                })
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));

        User user = userRepository.findById(passwordResetToken.getUser().getId()).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        userService.setPassword(user, body.getNewPassword());
        userRepository.save(user);

        passwordResetToken.setUsed(true);
        passwordResetTokensRepository.save(passwordResetToken);

        return ResponseEntity.ok("Password updated successfully");
    }
}
