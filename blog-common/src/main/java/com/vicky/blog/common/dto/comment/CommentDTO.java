package com.vicky.blog.common.dto.comment;

import java.util.List;

import com.vicky.blog.common.dto.user.UserDTO;

import lombok.Data;

@Data
public class CommentDTO {
    
    private Long id;
    private String content;
    private UserDTO commentBy;
    private CommentDTO parentComment;
    private Long blogId;
    private List<CommentDTO> threads;

}
