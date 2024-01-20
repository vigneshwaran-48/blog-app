package com.vicky.blog.model;

import java.util.Arrays;
import java.util.List;

import com.vicky.blog.common.dto.organization.OrganizationUserDTO;
import com.vicky.blog.common.dto.organization.OrganizationUserDTO.OrgUser;
import com.vicky.blog.common.dto.organization.OrganizationUserDTO.UserOrganizationRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "organization_user")
public class OrganizationUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
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

    public OrganizationUserDTO toDTO() {

        OrganizationUserDTO organizationUserDTO = new OrganizationUserDTO();
        organizationUserDTO.setOrganization(organization.toDTO());

        OrgUser orgUser = organizationUserDTO.new OrgUser(user.toDTO(), role);
        organizationUserDTO.setUsers(Arrays.asList(orgUser));

        return organizationUserDTO;
    }
}
