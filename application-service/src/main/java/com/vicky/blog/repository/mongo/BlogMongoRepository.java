package com.vicky.blog.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.Blog;

public interface BlogMongoRepository extends MongoRepository<Blog, Long> {
    
}
