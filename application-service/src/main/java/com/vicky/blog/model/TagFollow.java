package com.vicky.blog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.Data;

@Data
@Document
public class TagFollow {
    
    @Id
    private String id;

    @DocumentReference
    private Tag tag;

    @DocumentReference
    private User follower;
}
