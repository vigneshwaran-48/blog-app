package com.vicky.blog.common.dto.organization;

import java.time.LocalDateTime;

public class OrganizationUserResponseData {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private OrganizationUserDTO organizationUsers;

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public OrganizationUserDTO getOrganizationUsers() {
        return organizationUsers;
    }
    public void setOrganizationUsers(OrganizationUserDTO organizationUsers) {
        this.organizationUsers = organizationUsers;
    }
    
}
