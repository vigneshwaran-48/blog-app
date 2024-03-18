package com.vicky.blog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.vicky.blog.common.dto.organization.OrganizationUserDTO.UserOrganizationRole;

@Document("organization_user")
public class OrganizationUser {
    
    @Id
    private Long id;

    @DocumentReference
    private Organization organization;

    @DocumentReference
    private User user;

    private UserOrganizationRole role = UserOrganizationRole.MEMBER;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserOrganizationRole getRole() {
        return role;
    }

    public void setRole(UserOrganizationRole role) {
        this.role = role;
    }
}
