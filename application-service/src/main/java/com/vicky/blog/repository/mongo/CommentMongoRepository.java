package com.vicky.blog.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.Comment;

public interface CommentMongoRepository extends MongoRepository<Comment, Long> {
    
}
