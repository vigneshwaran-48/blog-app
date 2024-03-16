package com.vicky.blog.repository.firebase.model;

import java.time.LocalDateTime;

import com.vicky.blog.model.Blog;

import lombok.Data;

@Data
public class BlogModal {
    
    private Long id;

    private String title;

    private String image = "";

    private String content;

    private String owner_id;

    private String description;

    private LocalDateTime posted_time;

    private boolean is_published;

    private Long published_at;

    public static BlogModal build(Blog blog) {

        BlogModal blogModal = new BlogModal();
        blogModal.setContent(blog.getContent());
        blogModal.setDescription(blog.getDescription());
        blogModal.setId(blog.getId());
        blogModal.setImage(blog.getImage());
        blogModal.setOwner_id(blog.getOwner().getId());
        blogModal.setPosted_time(blog.getPostedTime());
        blogModal.setPublished_at(blog.getPublishedAt().getId());
        blogModal.setTitle(blog.getTitle());
        blogModal.set_published(blog.isPublised());
        return blogModal;

    }

    public Blog toEntity() {
        Blog blog = new Blog();
        blog.setId(id);
        blog.setContent(content);
        blog.setDescription(description);
        blog.setImage(image);
        blog.setPostedTime(posted_time);
        blog.setPublised(is_published);
        blog.setTitle(title);
        return blog;
    }
}
