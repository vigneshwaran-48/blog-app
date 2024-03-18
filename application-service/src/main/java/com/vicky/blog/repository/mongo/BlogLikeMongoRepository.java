package com.vicky.blog.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.BlogLike;

public interface BlogLikeMongoRepository extends MongoRepository<BlogLike, Long> {
    
}
