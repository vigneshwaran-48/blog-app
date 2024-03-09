package com.vicky.blog.model;

import java.time.LocalDateTime;

import com.vicky.blog.common.dto.comment.CommentDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "comment_by", nullable = false)
    private User commentBy;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;
    
    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    @Column(name = "commented_time", nullable = false)
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
