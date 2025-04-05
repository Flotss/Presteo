package com.presteo.app.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class AuthenticationResponse {
    private final String token;
}
