package com.vicky.blog.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.Follow;

public interface FollowMongoRepository extends MongoRepository<Follow, Long> {
    
}
