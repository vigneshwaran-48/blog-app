package com.vicky.blog.common.dto.organization;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrganizationPermissionResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private boolean hasPermission;
    
}
