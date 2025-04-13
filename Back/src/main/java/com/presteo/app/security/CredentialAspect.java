package com.presteo.app.security;

import com.presteo.app.model.RoleType;
import com.presteo.app.security.annotation.CheckCredential;
import com.presteo.app.security.model.CustomUserDetails;
import com.presteo.app.security.utils.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;

import java.lang.reflect.Field;

@Aspect
public class CredentialAspect {

    @Before("@annotation(checkCredential)")
    public void checkUserCredential(JoinPoint joinPoint, CheckCredential checkCredential) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            Long userId = extractUserId(arg);
            if (userId != null) {
                checkUserId(userId);
                break;
            }
        }
    }

    private Long extractUserId(Object arg) {
        if (arg == null) {
            return null;
        }

        if (arg instanceof Long id) {
            return id;
        }

        try {
            Field field = arg.getClass().getDeclaredField("userId");
            Object value = field.get(arg);
            if (value instanceof Long id) {
                return id;
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return null;
    }

    private void checkUserId(Long userId) {
        CustomUserDetails user = SecurityUtils.getAuthenticatedUser();
        if (!user.getRole().equals(RoleType.ADMIN.getName()) && user.getId() != userId) {
            throw new AccessDeniedException(SecurityConstants.ACCESS_DENIED_MESSAGE);
        }
    }
}
