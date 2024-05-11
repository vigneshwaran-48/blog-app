package com.vicky.blog.common.dto.profile;

import java.io.Serializable;

import lombok.Data;

@Data
public class ProfileDTO implements Serializable {

    public enum ProfileType {
        USER,
        ORGANIZATION
    }
    
    private String profileId;
    private String name;
    private String description;
    private ProfileType type;
    private String entityId;
    private String bannerImage;
}
