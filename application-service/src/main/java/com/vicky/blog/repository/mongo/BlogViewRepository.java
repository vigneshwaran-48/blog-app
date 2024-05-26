package com.vicky.blog.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.BlogView;

public interface BlogViewRepository extends MongoRepository<BlogView, String> {
    
    List<BlogView> findByBlogIdAndUserIdOrderByViewedTimeDesc(String blogId, String userId);

    List<BlogView> findByBlogId(String blogId);
}
