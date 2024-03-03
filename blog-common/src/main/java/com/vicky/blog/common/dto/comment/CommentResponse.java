package com.vicky.blog.common.dto.comment;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private CommentDTO comment;

}
