package com.vicky.blog.repository.firebase.model;

import java.time.LocalDateTime;

import com.vicky.blog.model.Comment;

import lombok.Data;

@Data
public class CommentModal {
    
    private Long id;
    private String content;
    private String comment_by;
    private Long parent_comment_id;
    private Long blog_id;
    private LocalDateTime commented_time;

    public static CommentModal build(Comment comment) {
        CommentModal commentModal = new CommentModal();
        commentModal.setId(comment.getId());
        commentModal.setBlog_id(comment.getBlog().getId());
        commentModal.setComment_by(comment.getCommentBy().getId());
        commentModal.setCommented_time(comment.getCommentedTime());
        commentModal.setContent(comment.getContent());
        if(comment.getParentComment() != null) {
            commentModal.setParent_comment_id(comment.getParentComment().getId());
        }
        return commentModal;
    }

    public Comment toEntity() {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setCommentedTime(commented_time);
        comment.setContent(content);
        return comment;
    }
}
