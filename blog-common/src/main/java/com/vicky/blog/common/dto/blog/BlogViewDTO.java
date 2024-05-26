package com.vicky.blog.common.dto.blog;

import java.time.LocalDateTime;

import com.vicky.blog.common.dto.user.UserDTO;

import lombok.Data;

@Data
public class BlogViewDTO {
    
    private String id;
    private BlogDTO blog;
    private UserDTO user;
    private LocalDateTime viewedTime;
}
