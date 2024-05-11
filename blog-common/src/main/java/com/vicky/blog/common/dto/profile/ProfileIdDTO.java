package com.vicky.blog.common.dto.profile;

import java.io.Serializable;

import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;

import lombok.Data;

@Data
public class ProfileIdDTO implements Serializable {
    private String id;
    private String profileId;
    private String entityId;
    private ProfileType type;
}
