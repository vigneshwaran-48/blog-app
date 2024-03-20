package com.vicky.blog.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.Blog;

import jakarta.transaction.Transactional;

public interface BlogMongoRepository extends MongoRepository<Blog, String> {
    
    Optional<Blog> findByOwnerIdAndId(String userId, String id);

    List<Blog> findByOwnerId(String userId);

    Optional<Blog> findByIdAndPublishedAtId(String id, String profileId);

    List<Blog> findByPublishedAtId(String id);

    @Transactional
    void deleteByOwnerIdAndId(String userId, String id);
}
