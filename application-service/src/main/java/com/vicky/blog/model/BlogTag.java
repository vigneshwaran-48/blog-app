package com.vicky.blog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.Data;

@Document
@Data
public class BlogTag {
    
    @Id
    private String id;

    @DocumentReference
    private Blog blog;

    @DocumentReference
    private Tag tag;
}
