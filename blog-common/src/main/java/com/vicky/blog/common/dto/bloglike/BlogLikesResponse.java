package com.vicky.blog.common.dto.bloglike;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BlogLikesResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time; 
    List<BlogLikeDTO> likes;
    
}
