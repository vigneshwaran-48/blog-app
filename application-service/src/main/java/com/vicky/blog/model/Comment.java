package com.vicky.blog.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.vicky.blog.common.dto.comment.CommentDTO;

import lombok.Data;

@Document
@Data
public class Comment {
    
    @Id
    private Long id;

    private String content;

    @DocumentReference
    private User commentBy;

    @DocumentReference
    private Comment parentComment;
    
    @DocumentReference
    private Blog blog;

    private LocalDateTime commentedTime;

    public static Comment build(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        Blog blog = new Blog();
        blog.setId(commentDTO.getBlogId());
        comment.setBlog(blog);

        comment.setCommentBy(User.build(commentDTO.getCommentBy()));
        comment.setContent(commentDTO.getContent());
        comment.setCommentedTime(commentDTO.getCommentedTime());
        if(commentDTO.getParentComment() != null) {
            comment.setParentComment(Comment.build(commentDTO.getParentComment()));
        }
        return comment;
    }

    public CommentDTO toDTO() {

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(id);
        commentDTO.setCommentBy(commentBy.toDTO());
        commentDTO.setBlogId(blog.getId());
        commentDTO.setContent(content);
        commentDTO.setCommentedTime(commentedTime);
        if(parentComment != null) {
            commentDTO.setParentComment(parentComment.toDTO());
        }
        return commentDTO;
    }
}
