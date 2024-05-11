package com.vicky.blog.common.dto.organization;

import java.io.Serializable;
import java.util.List;

import com.vicky.blog.common.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OrganizationUserDTO implements Serializable {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class OrgUser implements Serializable {
        private UserDTO details;
        private UserOrganizationRole role;
    }

    public enum UserOrganizationRole {
        ADMIN,
        MODERATOR,
        MEMBER
    }

    private OrganizationDTO organization;
    private List<OrgUser> users;

    public OrganizationDTO getOrganization() {
        return organization;
    }
    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }
    public List<OrgUser> getUsers() {
        return users;
    }
    public void setUsers(List<OrgUser> users) {
        this.users = users;
    }

}
