package com.vicky.blog.common.dto.blog;

import com.vicky.blog.common.dto.user.UserDTO;

import lombok.Data;

@Data
public class BlogDTO {
    
    private Long id;
    private String title;
    private String content;
    private String image;
    private UserDTO owner;
    private String description;

}
