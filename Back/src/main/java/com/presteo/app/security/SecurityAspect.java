package com.presteo.app.security;

import com.presteo.app.model.RoleType;
import com.presteo.app.security.annotation.SecuredRoute;
import com.presteo.app.security.model.CustomUserDetails;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityAspect {

    @Before("@annotation(securedRoute)")
    public void checkSecurity(SecuredRoute securedRoute) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if user is authenticated
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throwAccessDeniedException();
        }

        // Get user details and required roles
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        RoleType[] roles = securedRoute.roles();

        // Check if user has at least one of the required roles
        boolean hasRequiredRole = userDetails.getAuthorities().stream()
                .anyMatch(authority -> {
                    String userRole = authority.getAuthority();
                    for (RoleType requiredRole : roles) {
                        if (userRole.equals("ROLE_" + requiredRole.name())) {
                            return true;
                        }
                    }
                    return false;
                });

        if (!hasRequiredRole) {
            throwAccessDeniedRoleException();
        }
    }

    private void throwAccessDeniedRoleException() {
        throw new AccessDeniedException("You do not have the required role to access this resource");
    }

    private void throwAccessDeniedException() {
        throw new AccessDeniedException("You are not authenticated to access this resource");
    }
}
