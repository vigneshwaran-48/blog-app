package com.vicky.blog.model;

import com.vicky.blog.common.dto.bloglike.BlogLikeDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class BlogLike {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "liked_user_id", nullable = false)
    private User likedBy;

    public BlogLikeDTO toDTO() {
        BlogLikeDTO blogLikeDTO = new BlogLikeDTO();
        blogLikeDTO.setBlog(blog.toDTO());
        blogLikeDTO.setUser(likedBy.toDTO());
        blogLikeDTO.setId(id);
        return blogLikeDTO;
    }
}
