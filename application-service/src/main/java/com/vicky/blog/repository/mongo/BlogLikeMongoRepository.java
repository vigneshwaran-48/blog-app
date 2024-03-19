package com.vicky.blog.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.BlogLike;

import jakarta.transaction.Transactional;

public interface BlogLikeMongoRepository extends MongoRepository<BlogLike, String> {
    
    @Transactional
    void deleteByBlogIdAndLikedById(String blogId, String userId);

    List<BlogLike> findByBlogId(String blogId);

    Optional<BlogLike> findByBlogIdAndLikedById(String blogId, String userId);
}
