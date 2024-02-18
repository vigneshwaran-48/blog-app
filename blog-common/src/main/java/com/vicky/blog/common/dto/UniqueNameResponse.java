package com.vicky.blog.common.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UniqueNameResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private boolean isUnique;
}
