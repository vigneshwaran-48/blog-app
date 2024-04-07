package com.vicky.blog.common.utility;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserIdExtracter {

    public static final String GUEST_USER_ID_PREFIX = "X_Guest_";

    @Value("${spring.profiles.active}")
    private String mode;

    public String getUserId(Principal principal) {

        String userId = "";

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        if (mode.equals("single-user")) {
            userId = "DEVELOPMENT";
        } else if (principal != null) {
            userId = principal.getName();
        } else {
            String requestUserIp = request.getRemoteAddr();
            userId = GUEST_USER_ID_PREFIX + requestUserIp;
        }

        return userId;
    }
}
