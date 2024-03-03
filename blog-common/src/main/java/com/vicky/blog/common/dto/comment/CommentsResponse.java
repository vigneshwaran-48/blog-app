package com.vicky.blog.common.dto.comment;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CommentsResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private List<CommentDTO> comments;

}
