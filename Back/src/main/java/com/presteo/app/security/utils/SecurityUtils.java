package com.presteo.app.security.utils;

import com.presteo.app.model.RoleType;
import com.presteo.app.security.SecurityConstants;
import com.presteo.app.security.model.CustomUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.function.Function;

public class SecurityUtils {

    public static CustomUserDetails getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException(SecurityConstants.ACCESS_DENIED_MESSAGE);
        }

        return (CustomUserDetails) authentication.getPrincipal();
    }

    public static boolean checkUserRole(CustomUserDetails userDetails, RoleType[] roles) {
        if (roles.length == 0) {
            return true;
        }

        if (hasRole(userDetails, RoleType.ADMIN)) {
            return true;
        }

        boolean hasRequiredRole = hasRole(userDetails, roles);

        if (!hasRequiredRole) {
            throw new AccessDeniedException(SecurityConstants.ACCESS_DENIED_MESSAGE);
        }

        return true;
    }

    public static <T> void verifyOwnershipOrAdmin(T model , Function<T, Long>... getUserId ) {
        CustomUserDetails userDetails = getAuthenticatedUser();

        if (hasRole(userDetails, RoleType.ADMIN)) {
            return;
        }

        boolean isNotAccepted =  Arrays.stream(getUserId).allMatch(function -> function.apply(model) != userDetails.getId());
        if (isNotAccepted) {
            throw new AccessDeniedException(SecurityConstants.CANNOT_CREATE_OR_UPDATE_FOR_OTHERS_MESSAGE);
        }
    }

    private static boolean hasRole(CustomUserDetails userDetails, RoleType role) {
        return userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role.name()));
    }

    private static boolean hasRole(CustomUserDetails userDetails, RoleType[] roles) {
        for (RoleType role : roles) {
            if (hasRole(userDetails, role)) {
                return true;
            }
        }
        return false;
    }
}
