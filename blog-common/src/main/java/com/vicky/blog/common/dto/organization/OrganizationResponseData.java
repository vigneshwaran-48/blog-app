package com.vicky.blog.common.dto.organization;

import java.time.LocalDateTime;

public class OrganizationResponseData {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private OrganizationDTO organization;

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
    public OrganizationDTO getOrganization() {
        return organization;
    }
    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }
    
}
