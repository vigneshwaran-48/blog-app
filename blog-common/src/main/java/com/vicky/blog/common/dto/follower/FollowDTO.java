package com.vicky.blog.common.dto.follower;

import com.vicky.blog.common.dto.profile.ProfileIdDTO;

import lombok.Data;

@Data
public class FollowDTO {
    
    private String id;
    private ProfileIdDTO profile;
    private ProfileIdDTO follower;
}
