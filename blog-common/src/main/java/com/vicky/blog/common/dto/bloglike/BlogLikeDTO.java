package com.vicky.blog.common.dto.bloglike;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.user.UserDTO;

import lombok.Data;

@Data
public class BlogLikeDTO {
    
    private Long id;
    private BlogDTO blog;
    private UserDTO user;
    
}
