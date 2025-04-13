package com.presteo.app.security.utils;

import com.presteo.app.model.RoleType;
import com.presteo.app.security.SecurityConstants;
import com.presteo.app.security.model.CustomUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static CustomUserDetails getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException(SecurityConstants.ACCESS_DENIED_MESSAGE);
        }

        return (CustomUserDetails) authentication.getPrincipal();
    }

    public static void checkUserRole(CustomUserDetails userDetails, RoleType[] roles) {
        if (roles.length == 0) {
            return;
        }

        boolean hasRequiredRole = userDetails.getAuthorities().stream()
                .anyMatch(authority -> {
                    String userRole = authority.getAuthority();
                    for (RoleType role : roles) {
                        if (userRole.equals("ROLE_" + role.name())) {
                            return true;
                        }
                    }
                    return false;
                });

        if (!hasRequiredRole) {
            throw new AccessDeniedException(SecurityConstants.ACCESS_DENIED_MESSAGE);
        }
    }
}
