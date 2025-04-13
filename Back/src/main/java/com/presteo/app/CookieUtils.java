package com.presteo.app;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    @Value("${hostname}")
    private String hostname;

    public void setCookie(HttpServletResponse response, Cookie cookie) {
        boolean isLocalHost = hostname.contains("localhost");

        ResponseCookieBuilder responseCookieBuilder = ResponseCookie.from("bearer", cookie.getValue())
                .path("/")
                .maxAge(cookie.getMaxAge());

        if (!isLocalHost) {
            responseCookieBuilder.domain(hostname);
            responseCookieBuilder.secure(true);
            responseCookieBuilder.httpOnly(true);
            responseCookieBuilder.sameSite("None");
        }

        ResponseCookie cookieResponse = responseCookieBuilder.build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookieResponse.toString());
    }
}
