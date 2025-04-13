package com.presteo.app.service;

import com.presteo.app.model.PasswordResetTokens;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${fronturl}")
    private String fronturl;

    @Value("${environment}")
    private String environment;

    public boolean sendEmail(String email, String subject, String body) {
        Resend resend = new Resend(apiKey);

        CreateEmailOptions emailOptions = CreateEmailOptions.builder()
                .from("Presteo <administration@presteo.flotss.me>")
                .to(email)
                .subject(subject)
                .html(body)
                .build();

        try {
            resend.emails().send(emailOptions);
            return true;
        } catch (ResendException e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendEmail(String email, String subject, PasswordResetTokens passwordResetToken) {
        String body = getEmailContent(passwordResetToken);
        return sendEmail(email, subject, body);
    }

    private String getEmailContent(PasswordResetTokens passwordResetToken) {
        String urlScheme = environment.equals("production") ? "https" : "http";

        String resetLink = urlScheme + "://" + fronturl + "/user/" + passwordResetToken.getToken() + "/change-password";
        return "<html><body>" +
                "<h2>Password Reset Request</h2>" +
                "<p>Hello,</p>" +
                "<p>You have requested to reset your password. Please click the link below to set a new password:</p>" +
                "<p><a href=\"" + resetLink + "\">Reset My Password</a></p>" +
                "<p>This link will expire in 15 minutes for security reasons.</p>" +
                "<p>If you did not request this password reset, please ignore this email.</p>" +
                "<p>Thank you,<br/>Presteo Team</p>" +
                "</body></html>";
    }
}
