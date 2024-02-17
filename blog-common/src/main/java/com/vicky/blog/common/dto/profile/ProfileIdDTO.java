package com.vicky.blog.common.dto.profile;

import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;

import lombok.Data;

@Data
public class ProfileIdDTO {
    private Long id;
    private String profileId;
    private String entityId;
    private ProfileType type;
}
