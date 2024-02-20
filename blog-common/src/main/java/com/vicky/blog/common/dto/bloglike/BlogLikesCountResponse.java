package com.vicky.blog.common.dto.bloglike;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BlogLikesCountResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time; 
    private int likesCount;
}
