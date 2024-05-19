package com.vicky.blog.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.Tag;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    
    Optional<Tag> findByName(String name);
}
