package com.vicky.blog.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.User;

public interface UserMongoRepository extends MongoRepository<User, String> {
    
    List<User> findByEmail(String email);
}
