package com.vicky.blog.common.dto.profile;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private ProfileDTO profile;

}
