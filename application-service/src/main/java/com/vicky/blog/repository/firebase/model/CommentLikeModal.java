package com.vicky.blog.repository.firebase.model;

import com.vicky.blog.model.CommentLike;

import lombok.Data;

@Data
public class CommentLikeModal {
    
    private Long id;

    private Long comment_id;

    private String liked_user_id;

    public static CommentLikeModal build(CommentLike commentLike) {
        CommentLikeModal commentLikeModal = new CommentLikeModal();
        commentLikeModal.setComment_id(commentLike.getComment().getId());
        commentLikeModal.setId(commentLike.getId());
        commentLikeModal.setLiked_user_id(commentLike.getLikedBy().getId());
        return commentLikeModal;
    }

}
