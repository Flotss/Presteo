package com.presteo.app.controller;

import com.presteo.app.CookieUtils;
import com.presteo.app.model.RoleType;
import com.presteo.app.model.User;
import com.presteo.app.repository.UserRepository;
import com.presteo.app.security.JwtUtil;
import com.presteo.app.security.model.AuthenticationRequest;
import com.presteo.app.security.model.CustomUserDetails;
import com.presteo.app.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CookieUtils cookieUtils;

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
//        response.addCookie(jwtCookie);

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
}
