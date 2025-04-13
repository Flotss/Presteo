package com.presteo.app.controller.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePasswordToken {
    @NotNull(message = "Token cannot be null")
    public String token;

    @NotNull(message = "New password cannot be null")
    public String newPassword;

    @NotNull(message = "Confirm password cannot be null")
    public String confirmPassword;

    @AssertTrue(message = "Passwords do not match")
    private boolean isPasswordsMatching() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}
