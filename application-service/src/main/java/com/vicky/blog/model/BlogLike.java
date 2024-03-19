package com.vicky.blog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.vicky.blog.common.dto.bloglike.BlogLikeDTO;

import lombok.Data;

@Document("blog_like")
@Data
public class BlogLike {
    
    @Id
    private String id;

    @DocumentReference
    private Blog blog;

    @DocumentReference
    private User likedBy;

    public BlogLikeDTO toDTO() {
        BlogLikeDTO blogLikeDTO = new BlogLikeDTO();
        blogLikeDTO.setBlog(blog.toDTO());
        blogLikeDTO.setUser(likedBy.toDTO());
        blogLikeDTO.setId(id);
        return blogLikeDTO;
    }
}
