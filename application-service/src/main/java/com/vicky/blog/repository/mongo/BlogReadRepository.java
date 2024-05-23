package com.vicky.blog.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.BlogRead;

public interface BlogReadRepository extends MongoRepository<BlogRead, String> {
    
    Optional<BlogRead> findByBlogIdAndReaderId(String blogId, String userId);

    List<BlogRead> findByBlogId(String blogId);
}
