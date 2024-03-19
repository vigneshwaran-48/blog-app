package com.vicky.blog.common.dto.organization;

import java.time.LocalDateTime;

import com.vicky.blog.common.dto.user.UserDTO;

import lombok.Data;

@Data
public class OrganizationDTO {

    public enum Visibility {
        PRIVATE,
        PUBLIC
    }
    public enum JoinType {
        MEMBERS_INVITE,
        INVITE,
        ANYONE
    }
    
    private String id;
    private String name;
    private String profileId;
    private String description;
    private LocalDateTime createdTime;
    private Visibility visibility;
    private JoinType joinType;
    private String image;
    private UserDTO owner;
    
}
