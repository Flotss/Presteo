package com.presteo.app.security;

import com.presteo.app.security.annotation.SecuredRoute;
import com.presteo.app.security.model.CustomUserDetails;
import com.presteo.app.security.utils.SecurityUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import static com.presteo.app.security.utils.SecurityUtils.checkUserRole;

@Aspect
@Component
public class SecurityAspect {

    @Before("@annotation(securedRoute)")
    public void checkSecurity(SecuredRoute securedRoute) {
        CustomUserDetails userDetails = SecurityUtils.getAuthenticatedUser();
        checkUserRole(userDetails, securedRoute.roles());
    }
}