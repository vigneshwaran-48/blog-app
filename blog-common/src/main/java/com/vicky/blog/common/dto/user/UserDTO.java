package com.vicky.blog.common.dto.user;

import java.io.Serializable;

import com.vicky.blog.common.dto.preference.PreferenceDTO;

import lombok.Data;

@Data
public class UserDTO implements Serializable {

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
    private String description;
    private PreferenceDTO preferences;

}
