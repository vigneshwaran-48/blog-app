package com.vicky.blog.common.dto.organization;

import java.time.LocalDateTime;
import java.util.List;

public class OrganizationsResponseData {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private List<OrganizationDTO> organizations;

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
    public List<OrganizationDTO> getOrganization() {
        return organizations;
    }
    public void setOrganization(List<OrganizationDTO> organizations) {
        this.organizations = organizations;
    }
    
}
