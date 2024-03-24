package com.vicky.blog.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.User;

public interface UserMongoRepository extends MongoRepository<User, String> {
    
    Optional<User> findByEmail(String email);
}
