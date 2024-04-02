package com.vicky.blog.common.dto.user;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserDTO implements Serializable {

    public enum Theme {
        DARK,
        LIGHT
    }

    public enum UserType {
        GUEST,
        NORMAL,
        PREMIUM
    }
    
    private String id;
    private String name;
    private String profileId;
    private int age;
    private String email;
    private String image;
    private Theme theme;
    private String description;
    
    @Override
    public String toString() {
        return "UserDTO [id=" + id + ", name=" + name + ", age=" + age + ", email=" + email + ", image=" + image
                + ", theme=" + theme + "]";
    }
}
