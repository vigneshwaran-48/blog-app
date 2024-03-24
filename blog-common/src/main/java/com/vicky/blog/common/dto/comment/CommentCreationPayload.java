package com.vicky.blog.common.dto.comment;

import lombok.Data;

@Data
public class CommentCreationPayload {
    
    private String content;
    private String parentCommentId;

}
