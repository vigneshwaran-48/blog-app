package com.vicky.blog.repository.firebase.model;

import com.vicky.blog.model.BlogLike;

import lombok.Data;

@Data
public class BlogLikeModal {
    
    private Long id;

    private Long blog_id;

    private String liked_user_id;

    public static BlogLikeModal build(BlogLike blogLike) {
        BlogLikeModal blogLikeModal = new BlogLikeModal();
        blogLikeModal.setBlog_id(blogLike.getBlog().getId());
        blogLikeModal.setId(blogLike.getId());
        blogLikeModal.setLiked_user_id(blogLike.getLikedBy().getId());
        return blogLikeModal;
    }

    public BlogLike toEntity() {
        BlogLike blogLike = new BlogLike();
        blogLike.setId(id);
        return blogLike;
    }

}
