package com.vicky.blog.common.utility;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserIdExtracter {

    public static final String GUEST_USER_ID_PREFIX = "X_Guest_";

    @Value("${spring.profiles.active}")
    private String mode;

    public String getUserId(Principal principal) {

        String userId = "";

        if(mode.equals("single-user")) {
            userId = "DEVELOPMENT";
        } else if (principal != null) {
            userId = principal.getName();
        } else {
            userId = GUEST_USER_ID_PREFIX + UUID.randomUUID().toString();
        }
        
        return userId;
    }
}
