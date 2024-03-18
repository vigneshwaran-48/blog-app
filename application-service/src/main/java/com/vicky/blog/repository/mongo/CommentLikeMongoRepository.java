package com.vicky.blog.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.CommentLike;

public interface CommentLikeMongoRepository extends MongoRepository<CommentLike, Long> {
    
}
