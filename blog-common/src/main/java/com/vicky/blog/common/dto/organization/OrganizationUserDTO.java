package com.vicky.blog.common.dto.organization;

import java.util.List;

import com.vicky.blog.common.dto.user.UserDTO;

public class OrganizationUserDTO {
    
    public class OrgUser {

        private UserDTO details;
        private UserOrganizationRole role;

        public OrgUser() {}

        public OrgUser(UserDTO details, UserOrganizationRole role) {
            this.details = details;
            this.role = role;
        }

        public UserDTO getDetails() {
            return details;
        }
        public void setDetails(UserDTO details) {
            this.details = details;
        }
        public UserOrganizationRole getRole() {
            return role;
        }
        public void setRole(UserOrganizationRole role) {
            this.role = role;
        }
        
    }

    public enum UserOrganizationRole {
        ADMIN,
        MODERATOR,
        MEMEBER
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
