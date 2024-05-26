package com.vicky.blog.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.vicky.blog.common.dto.blog.BlogViewDTO;

import lombok.Data;

@Data
@Document
public class BlogView {
    
    @Id
    private String id;

    @DocumentReference
    private Blog blog;

    @DocumentReference
    private User user;

    private LocalDateTime viewedTime;

    public BlogViewDTO toDTO() {
        BlogViewDTO blogViewDTO = new BlogViewDTO();
        blogViewDTO.setId(id);
        blogViewDTO.setBlog(blog.toDTO());
        blogViewDTO.setUser(user.toDTO());
        blogViewDTO.setViewedTime(viewedTime);
        return blogViewDTO;
    }
}
