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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * Controller responsible for authentication management and user account operations
 * such as login, registration, and password management.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API for user and session management")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CookieUtils cookieUtils;
    private final PasswordResetTokensRepository passwordResetTokensRepository;
    private final EmailService emailService;
    private final PasswordResetTokenService passwordResetTokenService;

    /**
     * Authenticates a user with their identifier (username or email) and password.
     * Generates a JWT token and adds it to the response cookies.
     *
     * @param request Authentication request containing the identifier and password
     * @param response HTTP response to add the JWT cookie
     * @return Authentication success confirmation message
     */
    @Operation(summary = "User authentication", description = "Allows a user to log in with their credentials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully authenticated"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/signin")
    public String authenticateUser(
            @Parameter(description = "Authentication request containing login and password") 
            @RequestBody AuthenticationRequest request, 
            HttpServletResponse response) {
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

    /**
     * Registers a new user in the system with the default CUSTOMER role.
     *
     * @param user Information for the new user
     * @return Registration success confirmation message
     * @throws EntityExistsException If the username is already taken
     */
    @Operation(summary = "User registration", description = "Allows a new user to create an account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Username already in use")
    })
    @PostMapping("/signup")
    public String registerUser(
            @Parameter(description = "Data for the new user to register") 
            @RequestBody User user) {
        user.setUsername(user.getUsername().toLowerCase());
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new EntityExistsException("Username is already taken!");
        }

        userService.createUser(user, RoleType.CUSTOMER.name());

        return "User registered successfully!";
    }

    /**
     * Handles password reset requests.
     * Sends an email with a reset link if the email address exists.
     *
     * @param email Email address of the user requesting the reset
     * @return Confirmation message (for security reasons, the message is the same whether the email exists or not)
     */
    @Operation(summary = "Password reset request", description = "Allows a user to request a password reset link")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Request processed - an email has been sent if the address exists")
    })
    @GetMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Parameter(description = "Email address associated with the account") 
            @RequestParam String email) {
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

    /**
     * Allows an authenticated user to change their current password.
     * Verifies the old password before authorizing the change.
     *
     * @param body Object containing the old password, new password, and user ID
     * @return Success confirmation message
     * @throws AccessDeniedException If the old password is incorrect
     * @throws EntityNotFoundException If the user is not found
     */
    @Operation(summary = "Password change", description = "Allows a connected user to change their password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password updated successfully"),
        @ApiResponse(responseCode = "403", description = "Old password incorrect"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @CheckCredential
    @SecuredRoute
    @PostMapping("/change-password")
    public ResponseEntity<String> updatePassword(
            @Parameter(description = "Information for password change") 
            @RequestBody @Valid UpdatePasswordUser body) {
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


    /**
     * Allows a user to reset their password using a reset token.
     * Verifies the validity and expiration of the token before authorizing the change.
     *
     * @param body Object containing the reset token and new password
     * @return Success confirmation message
     * @throws AccessDeniedException If the token is expired
     * @throws EntityNotFoundException If the token or user is not found
     */
    @Operation(summary = "Password reset with token", description = "Allows a user to set a new password using a reset token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password updated successfully"),
        @ApiResponse(responseCode = "403", description = "Token expired"),
        @ApiResponse(responseCode = "404", description = "Token or user not found")
    })
    @PostMapping("/change-password-token")
    public ResponseEntity<String> updatePasswordByToken(
            @Parameter(description = "Token and new password") 
            @RequestBody @Valid UpdatePasswordToken body) {
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
