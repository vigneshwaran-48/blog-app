package com.vicky.blog.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.vicky.blog.common.dto.blog.BlogReadDTO;

import lombok.Data;

@Data
@Document
public class BlogRead {
    
    @Id
    private String id;

    @DocumentReference
    private Blog blog;

    @DocumentReference
    private User reader;

    private long timeSpent;

    private LocalDateTime readedTime;

    public BlogReadDTO toDTO() {
        BlogReadDTO blogReadDTO = new BlogReadDTO();
        blogReadDTO.setId(id);
        blogReadDTO.setBlog(blog.toDTO());
        blogReadDTO.setReadTime(timeSpent);
        blogReadDTO.setReader(reader.toDTO());
        blogReadDTO.setReadedTime(readedTime);
        return blogReadDTO;
    }
}
