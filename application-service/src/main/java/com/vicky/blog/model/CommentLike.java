package com.vicky.blog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.Data;

@Document("comment_like")
@Data
public class CommentLike {
    
    @Id
    private Long id;

    @DocumentReference
    private Comment comment;

    @DocumentReference
    private User likedBy;
}
