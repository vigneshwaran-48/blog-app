package com.vicky.blog.common.utility;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserIdExtracter {

    @Value("${spring.profiles.active}")
    private String mode;

    public String getUserId(Principal principal) {

        if(!mode.equals("single-user")) {
            return principal == null ? null : principal.getName();
        }
        
        return "DEVELOPMENT";
    }
}
