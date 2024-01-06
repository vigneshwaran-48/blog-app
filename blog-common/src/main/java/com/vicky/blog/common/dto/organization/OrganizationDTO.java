package com.vicky.blog.common.dto.organization;

import java.time.LocalDateTime;

import com.vicky.blog.common.dto.user.UserDTO;

import jakarta.validation.constraints.NotBlank;

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
    
    private Long id;
    @NotBlank
    private String name;
    private String description;
    private LocalDateTime createdTime;
    private Visibility visibility;
    private JoinType joinType;
    private String image;
    private UserDTO owner;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
    public Visibility getVisibility() {
        return visibility;
    }
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
    public JoinType getJoinType() {
        return joinType;
    }
    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public UserDTO getOwner() {
        return owner;
    }
    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    
}
