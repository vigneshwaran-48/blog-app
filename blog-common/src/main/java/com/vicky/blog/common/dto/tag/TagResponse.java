package com.vicky.blog.common.dto.tag;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TagResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private TagDTO tag;
    
}
