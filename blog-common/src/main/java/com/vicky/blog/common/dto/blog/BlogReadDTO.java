package com.vicky.blog.common.dto.blog;

import java.time.LocalDateTime;

import com.vicky.blog.common.dto.user.UserDTO;

import lombok.Data;

@Data
public class BlogReadDTO {
    
    private String id;
    private BlogDTO blog;
    private UserDTO reader;
    private long readTime;
    private LocalDateTime readedTime;
    
}
