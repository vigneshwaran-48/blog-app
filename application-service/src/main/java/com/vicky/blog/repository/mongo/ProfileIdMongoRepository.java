package com.vicky.blog.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.ProfileId;

public interface ProfileIdMongoRepository extends MongoRepository<ProfileId, Long> {
    
}
