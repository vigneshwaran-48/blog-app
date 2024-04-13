package com.vicky.blog.common.dto.preference;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PreferenceResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private PreferenceDTO preferences;
}
