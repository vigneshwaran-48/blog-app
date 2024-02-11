package com.vicky.blog.common.dto.blog;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BlogResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time; 
    private BlogDTO blog;
    
}
